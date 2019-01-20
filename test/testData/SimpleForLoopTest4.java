import java.sql.SQLException;

public class SimpleForLoopTest4 {
	
	class A
	{
		String init;
		String condition;
		String update;
		String body;
		
		public boolean init()
		{
			this.init = "safe";
			return true;
		}
		
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
		
		for (a.init(); a.condition(); a.update())
		{
			a.body();
		}
		
		__check_1();
	}
}
