import java.sql.SQLException;

public class SimpleIfTest3 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		String a = null;
		String b = "safeStr";
		
		if (random == true)
		{
			return;
		}
		else
		{
			a = "safeStr";
			return;
		}
		
		b = a;
	}
}
