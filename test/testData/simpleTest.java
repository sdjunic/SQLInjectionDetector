import java.sql.SQLException;

public class Test
{
	public String fld1 = null;
	public String test2;
	
	public Test()
	{
		test2 = "safe";
	}
	
	public static String getSafe()
	{
		return "safe";
	}
	
	public String getStr()
	{
		return test2;
	}
};

public class singleTest {
 	
	public static void main(String[] args) throws SQLException {
			
		String s = "safe";
		String notInit;
		Test t = new Test();
		String a = Test.getSafe();
		String b = t.getStr();
		String c = a + b;
		__check_1();
	}

};