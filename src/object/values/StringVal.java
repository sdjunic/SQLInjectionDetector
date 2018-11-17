package object.values;

import symbol.Table;
import symbol.object.Class;

public class StringVal extends ObjValue {
	
	public StringVal(boolean isSafe) {
		super(true, isSafe);
	}

	@Override
	public String toString() {
		return (this.isSafe() ? "S" : "U" )+ " String";
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		sb.append(this).append("\r\n");
	}

	@Override
	public Class getObjectType() {
		return Table.getStringClass();
	}
	
}
