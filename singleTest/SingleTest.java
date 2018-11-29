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
		v.a = "safe"; 
	}
};

class B extends Parent
{
	public void method(Val v) // bad parsing of this method
	{
		v.b = "safe"; 
	}
};

public class SimpleIfTest2 {

	public static void main(String[] args) throws SQLException {
			
		boolean random = true;
		Parent p = null;
		
		if (random == true)
		{
			p = new A();
		}
		else
		{
			p = new B();
		}
		
		Val v = new Val();
		p.method(v);
		
		String a = v.a;
		String b = v.b;
	}
};
