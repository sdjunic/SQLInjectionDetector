package object.values;

import symbol.object.Obj;
import symbol.object.Class;
import symbol.object.Field;

public class ClassValue extends ObjValue {
	
	private Class objectType;
	private ValuesHolder fields;
	
	public ClassValue(Class objectType, boolean isSafe) {
		super(false, isSafe);
		this.objectType = objectType;
		this.fields = new ValuesHolder(this);
	}
	
	public Class getObjectType() {
		return objectType;
	}

	public boolean setField(String name, ObjValue obj) {
		if (objectType instanceof Class) {
			Obj o = ((Class)objectType).getLocals().searchKey(name);
			if (!(o instanceof Field)) return false;
			fields.put(name, obj);
		}
		return false;
	}
	
	public ObjValue getField(String name) {
		return fields.get(name);
	}

	public ValuesHolder getFields() {
		return fields;
	}

	public void setFields(ValuesHolder fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		return (this.isSafe() ? "S " : "U ") + objectType.getName();
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		sb.append(this);
		fields.print(sb, tabNum);
	}
}
