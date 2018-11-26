package test;

import javax.servlet.http.*;
import java.sql.*;

public class Test extends HttpServlet {
	
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
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
	     String id = req.getParameter("realname");
	     String password = req.getParameter("mypassword");
	     
	     Statement stmt = null;
	     
	     stmt = con.createStatement();
	     ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username LIKE '" + id + "'");
	}
}