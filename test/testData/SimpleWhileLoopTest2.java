import java.sql.SQLException;

public class SimpleWhileLoopTest1 {
	
	public static void main(String[] args) throws SQLException {
			
		boolean cond = true;
		String a = null;
		String b = null;
		String c = null;
		String d = null;
		
		if (cond)
		{
			b = "safe";
		}
		
		while(cond)
		{
			a = b;
			b = c;
			c = d;
			d = "safe";
		}
		
		__check_7();
	}
}
