package database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

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

}
