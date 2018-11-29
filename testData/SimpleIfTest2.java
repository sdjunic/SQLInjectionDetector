import java.sql.SQLException;

public class SimpleIfTest2 {

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
			return;
			a = "safeStr";
		}
		
		b = a;
	}
}
