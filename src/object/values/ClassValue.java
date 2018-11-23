package object.values;

import symbol.object.Obj;

import java.util.HashMap;
import java.util.Map.Entry;

import symbol.object.Class;
import symbol.object.Field;

public class ClassValue extends ObjValue {
	
	private Class objectType;
	private FieldsValuesHolder fields;
	
	public ClassValue(Class objectType, boolean isSafe) {
		super(false, isSafe);
		this.objectType = objectType;
		this.fields = new FieldsValuesHolder(this);
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

	public FieldsValuesHolder getFields() {
		return fields;
	}

	public void setFields(FieldsValuesHolder fields) {
		this.fields = fields;
	}
	
	@Override
	public ObjValue copy()
	{
		ClassValue copy = new ClassValue(this.objectType, this.isSafe());
		copy.fields.importMappings(this.fields);
		return copy;
	}
	
	@Override
	public String toString() {
		return (this.isSafe() ? "S " : "U ") + objectType.getName();
	}
	
	public void print(StringBuilder sb, String tab, HashMap<ObjValue, Integer> objHash) {
		boolean newLine = false;
		for (Entry<String, ObjValue> var : fields.getValues().entrySet())
		{
			if (newLine) sb.append("\n");
			sb.append(tab).append(var.getKey()).append(": #").append(objHash.get(var.getValue()));
			newLine = true;
		}
	}
}
