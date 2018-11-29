import java.sql.SQLException;

public class SimpleIfTest1 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		String a = null;
		String b = "safeStr";
		
		if (random == true)
		{
			a = null;
		}
		else
		{
			a = "safeStr";
		}
		
		b = a;
	}
}
