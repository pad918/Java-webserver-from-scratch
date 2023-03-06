package database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

/*
* 	Hur ska user filer sparas?
* 		Lösnig 1:
*		filerna sparas i databasen, detta gör det möjligt
* 		att enklare säga vem / vilka som har tillgång till en
* 		specifik fil. Blir dock jobbigt med folders o.s.v.
*
* 		Lösning 2:
* 		En folder skapas för varje användare. Alla filer i foldern
* 		tillhör användaren.
* 		+ Enklare
* 		- Ett extra system utanför databasen att hålla koll på.
*
* 		Lösning 3:
* 		En folder för varje användare, med filer som tillhör användaren.
* 		Databasen har koll på filerna
*
*
* 		HUR LÖSA:
* 		När man går in på main page inloggad hämtar den en lista över alla filer man sökt på
* 		(eller sorterat efter) CSV. De skriv ut i en lista med namn, datum preview o.s.v.
* 		När man trycker på en bild läses den in i full storlek.
* 		Varje användare har därmed en map /user/imgs typ där alla bilder finns
* 		och en /user/prev med previews till varje bild med samma namn som bilden.
* 		En förfrågan kan då se ut som
* 		GET /img?img=3333&preview=true
*
* */


public class Database {
	private static Database instance;

	private Connection conn;

	static{
		instance = new Database();
	}

	private Database(){
		if(instance!=null)
			throw new IllegalStateException("Only one instance of singleton class Database allowed");

		try{
			connect();
			System.out.println("Running example SQL command and printing output:");
			ResultSet rs = run("SELECT * FROM test");
			while(rs.next())
				System.out.println(rs.getInt(1)+"  "+rs.getString(2));

		}catch(Exception e){ e.printStackTrace(); }
	}

	public static Database getInstance(){
		return instance;
	}

	//	Loads class to memory, if not already in memory ==> makes shure
	// static block is executed
	public static void init(){}

	public void connect() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("Started");
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql:cloud";
		Properties props = new Properties();
		props.setProperty("user", "postgres");

		// To avoid storing passwords in source code
		System.out.print("Enter database password: ");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String password = in.readLine();

		props.setProperty("password", password);
		//props.setProperty("ssl", "false");
		conn = DriverManager.getConnection(url, props);
	}

	public void closeConnection() throws SQLException {
		if(conn==null)
			throw new NullPointerException();

		conn.close();
	}

	public ResultSet run(String SQL_code) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(SQL_code);
		return rs;
	}

	public void update(String SQL_code, String[] params) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_code);
			for (int i = 0; i < params.length; i++) {
				stmt.setString(i + 1, params[i]);
			}
			stmt.executeUpdate();
		}catch (Exception e){

		}
		finally {
			if(stmt!=null)
				stmt.close();
		}
	}

	// -------- BIG SQL FUNCTIONS ----------

	public int get_user_id(String username){
		int user_id = -1;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT id FROM account WHERE username = (?);");
			stmt.setString(1, username);
			//stmt.executeUpdate();
			ResultSet result = stmt.executeQuery();
			result.next();
			user_id = result.getInt(1);
		}
		catch (Exception e){
			//failed
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		return user_id;
	}

	//Returns the name of the authenticated user
	public String getUsernameOfCookieAuthentication(String authCookie){
		String authenticatedUserName = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT username FROM (account_authentication join account on id=account_id)" +
					"where cookie=(?);");
			stmt.setString(1, authCookie);
			ResultSet result = stmt.executeQuery();
			if(result.next())
				authenticatedUserName = result.getString(1);
		}
		catch (Exception e){
			//failed
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		return authenticatedUserName;
	}

	public boolean add_cookie_authentication(String username, String cookie){
		int user_id = get_user_id(username);
		if(user_id<0)
			return false;

		boolean success = true;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO account_authentication values ((?), (?), '2023-02-22');");
			stmt.setString(1, cookie);
			stmt.setInt(2, user_id);
			stmt.executeUpdate();
		}
		catch (Exception e){
			//e.printStackTrace();
			success = false;
		}
		finally {
			try{
				if(stmt!=null)
					stmt.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		return success;
	}

	public boolean try_login(String username, String hashed_password) {
		// WARNING!!! YOU HAVE TO SANITIZE USERNAME AND EVERYTHING
		// IN SQL QUERIES!!!
		boolean success = false;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT pwd_hash FROM account WHERE username = (?);");
			stmt.setString(1, username);
			//stmt.executeUpdate();
			ResultSet result = stmt.executeQuery();
			result.next();
			String database_user_password_hash = result.getString(1);
			success = database_user_password_hash.equals(hashed_password);
		}
		catch (Exception e){
			//failed
			e.printStackTrace();
			success = false;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
			}
			catch (Exception e){
				e.printStackTrace();
				success = false;
			}
		}

		return success;
	}

}
