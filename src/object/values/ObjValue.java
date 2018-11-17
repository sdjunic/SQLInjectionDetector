package object.values;

import symbol.object.Class;

public abstract class ObjValue {
	
	private boolean isString;
	private boolean isSafe;
	
	public ObjValue(boolean isString, boolean isSafe) {
		this.isString = isString;
		this.isSafe = isSafe;
	}
	
	public boolean isStringValue() {
		return isString;
	}
	
	public boolean isClassValue() {
		return !isString;
	}
	
	public boolean isSafe() {
		return isSafe;
	}

	public void setSafe(boolean isSafe) {
		this.isSafe = isSafe;
	}
	
	public abstract Class getObjectType();
	
	public abstract void print(StringBuilder sb, int tabNum);
}
