import java.sql.SQLException;

public class TestReduction1 {

	public static void main(String[] args) throws SQLException {
			
		String a = "asf";
		
		__check_1();
		
		if (a == null)
		{
			String b = null;
			
			__check_1();
		}
		else
		{
			String c = "slobo";
			
			__check_1();
		}
		
		__check_1();
		
		String d = "dsadas";
		
		if (a != null)
		{
			a = null;
			
			__check_1();
		}

		__check_2();
		
		a = null;
		
		__check_2();
		
		if (a == null) {} // just for reduction
		
		__check_1();
		
		if (a == null)
		{
			a = "dsada";
			
			__check_1();
		}
		else
		{
			a = "aDJIAF";
			
			__check_1();
		}
		
		__check_1();
	}
};
