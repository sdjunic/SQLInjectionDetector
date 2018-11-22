package object.values;

public class IntRef {

	private int num  = 100;
	
	public IntRef() {}
	
	public int inc()
	{
		return num++;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(num);
	}

}
