import java.sql.SQLException;

public class SimpleForLoopTest5 {
	
	class A
	{
		Stirng condition;
		String update;
		String body;
		
		public boolean condition()
		{
			this.condition = "safe";
			return true;
		}
		
		public boolean update()
		{
			return true;
		}
		
		public boolean body()
		{
			return true;
		}
	};

	
	public static void main(String[] args) throws SQLException {
			
		A a = new A();
		
		String str = null;
		boolean b = true;
		
		if (b)
		{
			str = "safe";
		}
		
		__check_2();
		
		for (String forLoopStr = str; a.condition(); a.update())
		{
			a.body();
		}
		
		__check_2();
		
		str = null;
		if(b) {} // just for reduction
		
		__check_1();
	}
}
