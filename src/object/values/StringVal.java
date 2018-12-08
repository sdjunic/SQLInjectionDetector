package object.values;

import symbol.Table;
import symbol.object.Class;

public class StringVal extends ObjValue {
	
	public StringVal(boolean isSafe) {
		super(true, isSafe);
	}

	@Override
	public ObjValue shallowCopy()
	{
		return new StringVal(isSafe());
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
