package object.values;

import symbol.object.Class;

public class NullValue extends ObjValue {

	public NullValue() {
		super(false, true);
	}
	
	@Override
	public ObjValue shallowCopy()
	{
		// TODO: consider returning the same object, since NullValue object shouldn't be changes
		return new NullValue();
	}
	
	@Override
	public String toString() {
		return "S null";
	}
	
	@Override
	public Class getObjectType() {
		return null;
	}

}
