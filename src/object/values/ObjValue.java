package object.values;

import symbol.object.Type;

public abstract class ObjValue {
	
	private boolean isSafe;
	
	public ObjValue(boolean isSafe) {
		this.isSafe = isSafe;
	}
	
	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		assert !(this instanceof NullValue);
		assert !(this instanceof StringVal);
		this.isSafe = isSafe;
	}
	
	public abstract Type getObjectType();
	
	public abstract ObjValue shallowCopy();
	
}
