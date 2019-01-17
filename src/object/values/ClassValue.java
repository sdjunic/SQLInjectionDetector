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
		super(isSafe);
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
	public ObjValue shallowCopy()
	{
		ClassValue copy = new ClassValue(this.objectType, this.isSafe());
		copy.fields.importMappings(this.fields);
		return copy;
	}
	
	public void deepCopyFields(HashMap<ObjValue, ObjValue> copyMap)
	{
		assert (copyMap != null);
		
		for (Entry<String, ObjValue> var : this.fields.values.entrySet())
		{
			ObjValue originalField = var.getValue();
			ObjValue copyField = copyMap.get(originalField); 
			if (copyField == null)
			{
				copyField = originalField.shallowCopy();
				copyMap.put(originalField, copyField); 
				
				if (originalField instanceof ClassValue)
				{
					assert (copyField instanceof ClassValue);
					((ClassValue)copyField).deepCopyFields(copyMap);
				}
			}
			assert copyField != null;
			var.setValue(copyField);
		}
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
			if (var.getValue() instanceof ClassValue)
			{
				sb.append(tab).append(var.getKey()).append(": #").append(objHash.get(var.getValue()));
			}
			else
			{
				sb.append(tab).append(var.getKey()).append(": ").append(var.getValue());
			}
			newLine = true;
		}
	}
}
