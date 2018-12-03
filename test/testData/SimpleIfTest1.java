import java.sql.SQLException;

public class SimpleIfTest1 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		String a = null;
		String b = "safeStr";
		
		__check_1();
		
		if (random == true)
		{
			a = null;
			__check_1();
		}
		else
		{
			a = "safeStr";
			__check_1();
		}
		
		b = a;
		__check_2();
	}
}
