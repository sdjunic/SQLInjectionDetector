import java.sql.SQLException;

public class SimpleForLoopTest6 {
	
	class A
	{
		String x = safe;
		String init = null;
		String condition = null;
		String update = null;
		String body = null;

		public boolean init()
		{
			String temp = this.x;
			this.x = this.init;
			this.init = temp;
			return true;
		}
		
		public boolean condition()
		{
			this.condition = this.body;
			return true;
		}
		
		public boolean update()
		{
			this.update = this.init;
			return true;
		}
		
		public boolean body()
		{
			this.body = this.update;
			return true;
		}
	};

	
	public static void main(String[] args) throws SQLException {
			
		A a = new A();

		for (a.init(); a.condition(); a.update())
		{
			a.body();
		}
		
		__check_3();
	}
}
