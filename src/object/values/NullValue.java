package object.values;

import symbol.object.Class;

public class NullValue extends ObjValue {

	public NullValue() {
		super(false, true);
	}
	
	@Override
	public String toString() {
		return "S null";
	}
	
	@Override
	public Class getObjectType() {
		return null;
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		sb.append(this).append("\r\n");
	}

}
