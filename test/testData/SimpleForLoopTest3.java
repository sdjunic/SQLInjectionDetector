import java.sql.SQLException;

public class SimpleForLoopTest3 {
	
	class A
	{
		String init;
		String condition;
		String update;
		String body;
		
		public boolean init()
		{
			return true;
		}
		
		public boolean condition()
		{
			return true;
		}
		
		public boolean update()
		{
			this.update = "safe";
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
		
		__check_2();
	}
}
