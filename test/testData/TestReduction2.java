import java.sql.SQLException;

class BB
{
	String s = null;
};

class AA
{
	String a = "safe";
	BB bb;
	
	public AA()
	{
		bb = new BB();
	}
};

public class TestReduction2 {

	public static void main(String[] args) throws SQLException {
		AA aa = new AA();
		BB bb = aa.bb;
		int c = 5;
		
		__check_1();
				
		if (c == 0)
		{
			__check_1();
			aa.bb.s = "saa";
		}
		
		__check_2();
		
		bb.s = null;
		
		__check_2();
		
		if (c == 0)
		{} // just for reduction
		
		__check_1();
		
		if (c == 0)
		{
			__check_1();
			aa.bb = new BB();
		}
		
		__check_2();
		
		BB bb = aa.bb;
		
		__check_2();
		
		if (c == 0)
		{} // just for reduction
		
		__check_1();
	}
};