package prevoz;

import prevoz.kopno.*;
import java.sql.*;

public class Main {

	public static void main(String[] args) throws SQLException {
			
		Bicikl a = new Bicikl("bemix", str);
		a = new Bicikl("bemix", str);
		
		Bicikl b = new Bicikl("bemix", "mojBajs") {
			
			@Override
			public String getImeVozila(){
				java.util.Scanner sc = new java.util.Scanner(System.in);
				return sc.nextLine();
			}
			
		};
		
		Prevoz p = a;
		
		String query = "SELECT * FROM VOZILO WHERE name LIKE '" + p.getImeVozila() + "'";
		
		Connection con = DriverManager.getConnection("");
		Statement stmt = con.createStatement();
		stmt.executeQuery(query);
		
	}

}
