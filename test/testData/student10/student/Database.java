package student;

import java.sql.*;
import java.util.Scanner;

public class Database {
	
	private Connection con = null;
	private String table = "tabela";
	
	public Database(Connection con, String table) {
		this.con = con;
		this.table = table;
	}
	
	public User getUser(String username) {
		PreparedStatement stmt = null; 
		User u = null;
		try {
			Connection con2 = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
			stmt = con2.prepareStatement("SELECT * FROM " + table + " WHERE username LIKE ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				u = new User(rs.getInt("id"), username);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stmt != null) stmt.close();
		}
		return u;
	}
	
	public boolean checkIfUserExists(User user) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM " + table + " WHERE username LIKE ?");
			stmt.setString(1, user.getName());
			ResultSet rs = stmt.executeQuery();
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
