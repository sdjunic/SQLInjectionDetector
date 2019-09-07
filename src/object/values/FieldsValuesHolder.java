package object.values;

import symbol.object.Type;

public class FieldsValuesHolder extends ValuesHolder {

	private ClassValue parentObj;

	public FieldsValuesHolder(ClassValue parentObj) {
		super();
		assert parentObj != null;
		this.parentObj = parentObj;
	}
	
	public ObjValue get(String name) {
		if (name == null) return null;
		ObjValue result = values.get(name);
		if (result == null && (parentObj.getObjectType().findField(name) != null || 
		    name.equals("this") || (name.equals("super") && parentObj.getObjectType().getSuperClass() != null)))
		{
			Type t = null;
			if (name.equals("this")) t = parentObj.getObjectType();
			else if (name.equals("super")) t = parentObj.getObjectType().getSuperClass().type;
			else t = parentObj.getObjectType().findField(name).getType().type;
			result = makeObjValue(t, parentObj.isSafe());
			put(name, result);
		}
		return result;
	}
}
