package tests;

import java.sql.*;

class Test {
 	
	private Connection con = null;
	
	public void getConnection(String serverName,String instanceName,String port,String databaseName,String userName,String password) {
		String connectoinString = "jdbc:sqlserver://" + serverName + 
	            "\\" + instanceName + 
	            ":" + port +
	            ";databaseName=" + databaseName +
	            ";username=" + userName +
	            ";password=" + password;
		
		con = DriverManager.getConnection(connectoinString);
	}
	
	public void rek(int a, String s) {
		if (a > 0) rek (a-1, s+s);
		else {
			Statement stmt = null;
		     
		     try {
		    	 stmt = con.createStatement();
		    	 stmt.executeUpdate("INSERT INTO rekTable VALUES ('" + s + "')");
		     } catch (Exception e) {
		    	// TODO Auto-generated catch block
				e.printStackTrace();
		     } finally {
		    	 if (stmt != null) stmt.close();
		     }
		}
	}
	
}