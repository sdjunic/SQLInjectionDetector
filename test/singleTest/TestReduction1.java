import java.sql.SQLException;

// ispitati zasto test ne radi kad se stavi "aa.b.s = null", a radi kad se stavi "aa.b.s = "saa"";
// detaljno ispitati kloniranje taskova i if stmt
//

class BB
{
	String s = "safe";
};

class AA
{
	String a = "safe";
	BB b;
	
	public AA()
	{
		b = new BB();
	}
};

public class TestReduction1 {

	public static void main(String[] args) throws SQLException {
		
		String c = null;
		String a = "asf";
		String d = "dsadas";
		
		AA aa = new AA();
		BB bb = new BB();
		
		if (c == null)
		{
			aa.b.s = "saa";
		}
		
		__check_1();
	}
};