import java.sql.SQLException;

public class superTest
{
	public String superTest = "dsad";
};

public class Test extends superTest
{
	public String fld1 = null;
	public Test2 test2;
	
	public Test()
	{
		test2 = new Test2(this);
	}
};

public class Test2
{
	public String fld2 = "Slobo";
	public Test t;
	public Test2 t2 = this;
	
	public Test2(Test test)
	{
		this.t = test;
	}
};

public class singleTest {

	public 
	
	public static void main(String[] args) throws SQLException {
			
		String s = "safe";
		String notInit;
		Test t = new Test();
		
		
	}

}
