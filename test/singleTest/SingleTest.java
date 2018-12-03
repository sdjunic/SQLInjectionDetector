import java.sql.SQLException;

class Val
{
	public String a = null;
	public String b = null;
};

abstract class Parent
{
	public void method(Val v)
	{
		v.a = "aa";
		v.b = "bb";
	}
};

class A extends Parent
{
	public void method(Val v) // bad parsing of this method
	{
		__check_1();
		v.a = "safe"; 
	}
};

class B extends Parent
{
	public void method(Val v) // bad parsing of this method
	{
		__check_1();
		v.b = "safe"; 
	}
};

public class SplitOnVirtualCallTest {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		Parent p = null;
		
		__check_1();
		
		if (random == true)
		{
			p = new A();
			__check_1();
		}
		else
		{
			p = new B();
			__check_1();
		}
		
		__check_2();
		
		Val v = new Val();
		p.method(v);
		
		String a = v.a;
		String b = v.b;
		
		__check_2();
	}
};
