package student;

import java.sql.*;
import java.util.Scanner;

public class Database {
	
	private Connection con = program.callMeth(new SQLConnection());
	private String table = "tabela";
	
	public Database(Connection con, String table) {
		this.con = con;
		this.table = table;
	}
	
	public User getUser(String username) {
		
		__check_1();
		
		Statement stmt = null;
		User u = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE username LIKE '" + username + "'");
			if (rs.next()) {
				u = new User(rs.getInt("id"), username);
			}
			
			__check_2();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stmt != null) stmt.close();
		}
		
		return u;
		__check_100();
	}
	
	public boolean checkIfUserExists(User user) {
		
		__check_100();
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " +table+ " WHERE username LIKE '" + user.getName() + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stmt != null) stmt.close();
		}
		return false;
	}
	
	public String readSQLSafeString() {
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		return Statement.escapeSQL(str);
	}
	
	public String makeSafeSQL(String str) {
		return Sratement.escapeSQL(str);
	}
	
}
