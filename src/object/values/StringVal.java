package object.values;

import symbol.Table;
import symbol.object.Class;

public class StringVal extends ObjValue {
	
	private static StringVal safeStr = new StringVal(true);
	private static StringVal unsafeStr = new StringVal(false);
	
	private StringVal(boolean isSafe) {
		super(isSafe);
	}
	
	public static StringVal getString(boolean isSafe)
	{
		return (isSafe ? safeStr : unsafeStr);
	}

	@Override
	public ObjValue shallowCopy()
	{
		return this;
	}
	
	@Override
	public String toString() {
		return (this.isSafe() ? "S" : "U" ) + " String";
	}
	
	@Override
	public Class getObjectType() {
		return Table.getStringClass();
	}
	
}
