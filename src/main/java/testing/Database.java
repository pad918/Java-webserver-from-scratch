package testing;
import java.sql.*;
import java.util.Properties;

public class Database {
	public static void main(String args[]){
		try{
			System.out.println("Started");
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql:cloud";
			Properties props = new Properties();
			props.setProperty("user", "postgres");
			props.setProperty("password", "**********");
			//props.setProperty("ssl", "false");
			Connection conn = DriverManager.getConnection(url, props);
			//here sonoo is database name, root is username and password
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM test");
			while(rs.next())
				System.out.println(rs.getInt(1)+"  "+rs.getString(2));
			conn.close();
		}catch(Exception e){ System.out.println(e);}
	}
}
