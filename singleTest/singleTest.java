package singleTest;

public class Temp {
	String s;
	
	public boolean cond(String s) { return true; }
	public String update(String s) { return null; }
	public String loopBody(String s) { return null; }
};

public class Main {

	public static void main(String[] args) throws SQLException {
			
		String s = "safe";
		int b = 0;
		int c;
		String notInit;
		
		if (b != 0)
		{
			notInit = s;
			String s2 = null;
			
			String k = args[0];
			String k2 = s2;
		}
		
		Temp t2 = new Temp();
		while(t2.cond(s))
		{
			++b;
			notInit = s;
		}
		
		for (Temp t = new Temp(); t.cond(s); t.update(s))
		{
			t.loopBody(s);
		}
		
		if (b != 0)
		{
			notInit = s;
			String s2 = null;
			
			String k = args[0];
			String k2 = s2;
		}
		else
		{
			if (aa != 0)
			{
				notInit = null;
			}
			else
			{
				notInit = s;
			}
		}
		
		s = k;
		s = notInit;
		
	}

}
