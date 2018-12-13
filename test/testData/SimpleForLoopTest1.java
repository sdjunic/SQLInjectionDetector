import java.sql.SQLException;

public class SimpleForLoopTest1 {
	
	class A
	{
		Stirng init;
		Stirng condition;
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
			this.update = "safe";
			return true;
		}
		
		public boolean body()
		{
			this.body = "safe";
			return true;
		}
	};

	
	public static void main(String[] args) throws SQLException {
			
		A a = new A();
		
		for (a.init(); a.condition(); a.update())
		{
			a.body();
		}
		
		__check_2();
	}
}
