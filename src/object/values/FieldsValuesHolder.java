package object.values;

import java.util.HashMap;
import java.util.Map.Entry;

import symbol.object.Type;

public class FieldsValuesHolder extends ValuesHolder {

	private ClassValue parentObj; // this is set only for ValuesHolders of the object's fields

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

	// Copy all objects from this ValuesHolder and populate copyMap table,
	// to map original objects into newly created ones.
	//
	public void copyAllObjects(HashMap<ObjValue, ObjValue> copyMap)
	{
		for (ObjValue obj : values.values())
		{
			if (!copyMap.containsKey(obj))
			{
				copyMap.put(obj, obj.copy());
				if (obj instanceof ClassValue)
				{
					((ClassValue)obj).getFields().copyAllObjects(copyMap);
				}
			}
		}
	}
	
	// Based on copyMap, update all references. 
	// This method should be called for fields of newly created ClassValue,
	// to replace references to original fields with references to the newly created fields.
	//
	protected void updateReferences(HashMap<ObjValue, ObjValue> copyMap)
	{
		for (Entry<String, ObjValue> vhEntry : this.values.entrySet())
		{
			ObjValue prevObj = vhEntry.getValue();
			assert copyMap.containsKey(prevObj);
			ObjValue nextObj = copyMap.get(prevObj);
			vhEntry.setValue(nextObj);
		}
	}
	
}
