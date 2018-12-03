import java.sql.SQLException;

public class SimpleIfTest3 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		String a = null;
		String b = "safeStr";
		
		__check_1();
		
		if (random == true)
		{
			__check_1();
			return;
			__check_100();
		}
		else
		{
			a = "safeStr";
			__check_1();
			return;
			__check_100();
		}
		
		__check_100();
		
		b = a;
	}
}
