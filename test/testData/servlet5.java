package test;

import javax.servlet.http.*;
import java.sql.*;

public class Test extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		
		__check_100();
		
	     String id = req.getParameter("realname");
	     String password = req.getParameter("mypassword");
	     
	     Statement stmt = null;
	     
	     try {
	    	 Connection con = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
	    	 stmt = con.createStatement();
	    	 ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username LIKE '" + id + "'");
	     
	    	 if (rs.next()) {
	             req.getSession().setAttribute("username", rs.getString("username"));
	             res.sendRedirect("home");
	         }
	         else {
	             req.setAttribute("error", "Unknown user, please try again");
	             req.getreqDispatcher("/login.jsp").forward(req, res);
	         }

	     } catch (Exception e) {
	    	// TODO Auto-generated catch block
			e.printStackTrace();
	     } finally {
	    	 if (stmt != null) stmt.close();
	     }
		 
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		
		__check_100();
		
	     String id = req.getParameter("realname");
	     String password = req.getParameter("mypassword");
	     
	     Statement stmt = null;
	     
	     try {
	    	 Connection con = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
	    	 stmt = con.createStatement();
	    	 ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username LIKE '" + id + "'");
	     
	    	 if (rs.next()) {
	             req.getSession().setAttribute("username", rs.getString("username"));
	             res.sendRedirect("home");
	         }
	         else {
	             req.setAttribute("error", "Unknown user, please try again");
	             req.getreqDispatcher("/login.jsp").forward(req, res);
	         }

	     } catch (Exception e) {
	    	// TODO Auto-generated catch block
			e.printStackTrace();
	     } finally {
	    	 if (stmt != null) stmt.close();
	     }
		 
	}
}