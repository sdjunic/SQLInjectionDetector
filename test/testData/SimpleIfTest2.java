import java.sql.SQLException;

public class SimpleIfTest2 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		String a = null;
		String b = "safeStr";
		
		__check_1();
		
		if (random == true)
		{
			__check_1();
			a = null;
		}
		else
		{
			__check_1();
			return;
			__check_100();
			a = "safeStr";
		}
		
		__check_1();
		
		b = a;
	}
}
