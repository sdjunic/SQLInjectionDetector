import java.sql.SQLException;

public class SimpleForLoopTest6 {
	
	class Check
	{
		void check()
		{
			__check_1();
		}
	}
	
	class Recursion
	{
		Check a = new Check();
		Check b = null;
		Check c = null;
		Check d = null;
		Check res = null;
		
		String methX()
		{
			this.c = this.b;
			this.res = this.d;
			
			methY();
			
			this.res.check();
			__check_1();
		}
		
		String methY()
		{
			boolean b = true;
			
			this.b = this.a;
			this.d = this.c;
			
			if (b)
			{
				methX();
			}
				
			this.res.check();
			__check_1();
		}
	};

	
	public static void main(String[] args) throws SQLException {
			
		Recursion r = new Recursion();
		
		r.methX();		
		r.res.check();
		__check_1();
		
		r = new Recursion();
		
		r.methY();
		r.res.check();
		__check_1();
	}
}
