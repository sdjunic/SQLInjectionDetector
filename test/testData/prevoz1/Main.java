package prevoz;

import prevoz.kopno.*;
import java.sql.*;

public class Main {

	public static void main(String[] args) throws SQLException {
			
		class InnerKlasa {
			
			public int a = 5;
			public int b = 7;
			public String k = "safeString";
			public String notInit;
			public int c;
			
			public InnerKlasa() {}
			
			public String metodaUInnerKlasi() { 
				
				class InnerKlasaUMetodi { 
					public int a = 5;
					public int b = 7;
					public String k = "safeString";
					
					public String dohvatiString() { return k; }
					
				};
				
				InnerKlasaUMetodi ikum = new InnerKlasaUMetodi();
				return ikum.dohvatiString();
			}
			
		};
		
		InnerKlasa ik = new InnerKlasa();
		String str = ik.metodaUInnerKlasi();
		
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
