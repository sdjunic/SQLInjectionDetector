package object.values;

import symbol.object.Class;

public class NullValue extends ObjValue {

	private static NullValue nullValue = new NullValue();
	
	private NullValue() {
		super(true);
	}
	
	public static NullValue getNullValue()
	{
		return nullValue;
	}
	
	@Override
	public ObjValue shallowCopy()
	{
		return this;
	}
	
	@Override
	public String toString() {
		return "NULL";
	}
	
	@Override
	public Class getObjectType() {
		return null;
	}

}
