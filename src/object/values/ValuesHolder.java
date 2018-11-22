package object.values;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import symbol.object.Class;
import symbol.object.Type;

public class ValuesHolder {
	
	private Map<String, ObjValue> values;
	private ClassValue parentObj;
	
	public ValuesHolder(ClassValue parentObj) {
		values = new HashMap<String, ObjValue>();
		this.parentObj = parentObj;
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public Map<String, ObjValue> getValues() {
		return values;
	}

	public void put(String name, ObjValue value) {
		if (name == null) return;
		values.put(name, value);
	}
	
	public ObjValue get(String name) {
		if (name == null) return null;
		ObjValue result = values.get(name);
		if (result == null && parentObj != null && (parentObj.getObjectType().findField(name) != null || 
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
	
	public void put(List<String> name, ObjValue value) {
		if (name == null || name.isEmpty()) return;
		if (name.size() == 1) {
			values.put(name.get(0), value);
			return;
		}
		ObjValue obj = get(name.get(0));
		if (obj == null || !(obj instanceof ClassValue)) {
			System.err.println("GRESKA U METODI put KLASE ValueHolder!");
			return;
		}
	
		if (((ClassValue)obj).getObjectType().containsField(name.get(1))) {
			((ClassValue)obj).getFields().put(name.subList(1, name.size()), value);
		} else {
			name.add(1, "super");
			((ClassValue)obj).getFields().put(name.subList(1, name.size()), value);
		}
		
	}
	
	public ObjValue get(List<String> name) {
		if (name == null || name.isEmpty()) return null;
		if (name.size() == 1) return get(name.get(0));
		ObjValue obj = get(name.get(0));
		if (obj == null || !(obj instanceof ClassValue)) {
			return null;
		}
		if (((ClassValue)obj).getObjectType().containsField(name.get(1))) {
			return ((ClassValue)obj).getFields().get(name.subList(1, name.size()));
		} else {
			name.add(1, "super");
			return ((ClassValue)obj).getFields().get(name.subList(1, name.size()));
		}
	}
	
	public void addObject(Type objectType, String name, boolean isSafe) {
		ObjValue objValue = makeObjValue(objectType, isSafe);
		if (objValue != null) values.put(name, objValue);
	}
	
	public static ObjValue makeObjValue(Type objectType, boolean isSafe) {
		if (objectType.isValueType()) return null;
		if (objectType instanceof Class) {
			Class cl = ((Class)objectType);
			if ("String".equals(cl.getName())) {
				return new StringVal(isSafe);
			} else {
				return new ClassValue(cl, isSafe);
			}
		}
		return null;
	}

	private void numberAllObjects(HashMap<ObjValue, Integer> objHash, IntRef num)
	{
		for (ObjValue obj : values.values())
		{
			if (!objHash.containsKey(obj))
			{
				objHash.put(obj, num.inc());
				if (obj instanceof ClassValue)
				{
					((ClassValue)obj).getFields().numberAllObjects(objHash, num);
				}
			}
		}
	}
	
	public void print(StringBuilder sb)
	{
		HashMap<ObjValue, Integer> objHash = new HashMap<>();
		IntRef num = new IntRef();
		numberAllObjects(objHash, num);
		
		for (Entry<String, ObjValue> var : values.entrySet())
		{
			sb.append(var.getKey()).append(": ").append("#").append(objHash.get(var.getValue())).append("\n");
		}
		
		for (Entry<ObjValue, Integer> val : objHash.entrySet())
		{
			sb.append("#").append(val.getValue()).append(":").append(val.getKey().toString()).append("\n");
			if (val.getKey() instanceof ClassValue)
			{
				((ClassValue)val.getKey()).print(sb, "    ", objHash); sb.append("\n");
			}
		}
	}
	
	// Import all mappings from sourceMap.
	//
	public void importMappings(ValuesHolder sourceMap)
	{
		for (Entry<String, ObjValue> it : sourceMap.values.entrySet())
		{
			this.values.put(it.getKey(), it.getValue());
		}
	}
	
	// Copy all objects from this ValuesHolder and populate copyMap table,
	// to map original objects into newly created ones.
	//
	private void copyAllObjects(HashMap<ObjValue, ObjValue> copyMap)
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
	private void updateReferences(HashMap<ObjValue, ObjValue> copyMap)
	{
		for (Entry<String, ObjValue> vhEntry : this.values.entrySet())
		{
			ObjValue prevObj = vhEntry.getValue();
			assert copyMap.containsKey(prevObj);
			ObjValue nextObj = copyMap.get(prevObj);
			vhEntry.setValue(nextObj);
		}
	}
	
	// Copy the whole values holder map recursively, and set all references 
	// between newly created objects to get the same relations as between original objects.
	//
	public ValuesHolder deepCopy()
	{
		HashMap<ObjValue, ObjValue> copyMap = new HashMap<>();
		ValuesHolder copyValuesHolder = new ValuesHolder(this.parentObj);
		
		// Copy all objects and make capyMap table, which is mapping between
		// previous and new object. Also populate new ValuesHolder map, with
		// references to new objects that we are creating in this method.
		//
		for (Entry<String, ObjValue> var : this.values.entrySet())
		{
			ObjValue prevObj = var.getValue();
			ObjValue newObj = prevObj.copy();
			copyValuesHolder.values.put(var.getKey(), newObj);
			if (prevObj instanceof ClassValue)
			{
				((ClassValue)prevObj).getFields().copyAllObjects(copyMap);
			}
		}
		
		// Update field references for all new objects of type ClassValue, using the
		// previously created mapping in copyMap.
		//
		for (ObjValue newValue : copyMap.values())
		{
			if (newValue instanceof ClassValue)
			{
				((ClassValue)newValue).getFields().updateReferences(copyMap);
			}
		}
		
		return copyValuesHolder;
	}
}
