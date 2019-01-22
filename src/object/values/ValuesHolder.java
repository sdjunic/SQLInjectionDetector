package object.values;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import symbol.object.ArrayType;
import symbol.object.Class;
import symbol.object.Type;

class LexicographicComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}
}

public abstract class ValuesHolder {
	
	protected SortedMap<String, ObjValue> values;

	public ValuesHolder() {
		values = new TreeMap<String, ObjValue>(new LexicographicComparator());
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	public Map<String, ObjValue> getValues() {
		return values;
	}

	public void put(String name, ObjValue value) {
		assert name != null && value != null;
		if (name == null) return;
		values.put(name, value);
	}

	public abstract ObjValue get(String name);
	
	public void remove(String var)
	{
		values.remove(var);
	}
	
	public void put(List<String> name, ObjValue value) {
		// It's OK to have value==null, because of unknown types, static fields, executing from risky methods...
		assert name != null && !name.isEmpty();
		if (name == null || name.isEmpty() || value == null) return;
		if (name.size() == 1) {
			values.put(name.get(0), value);
			return;
		}
		ObjValue obj = get(name.get(0));
		if (obj == null || !(obj instanceof ClassValue)) {
			assert false;
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
				return StringVal.getString(isSafe);
			} else {
				return new ClassValue(cl, isSafe);
			}
		}
		return null;
	}
	
	public static ObjValue makeDefaultObjValue(Type objectType, boolean isSafe) {
		if (objectType.isValueType()) return null;
		if (objectType instanceof Class) {
			Class cl = ((Class)objectType);
			if ("String".equals(cl.getName())) {
				return StringVal.getString(isSafe);
			} else {
				return cl.getDefaultObject(isSafe);
			}
		}
		if (objectType instanceof ArrayType)
		{
			Type arraySourceType = ((ArrayType)objectType).getType().type;
			if (arraySourceType instanceof Class) {
				Class cl = ((Class)arraySourceType);
				ObjValue elem = null;
				if ("String".equals(cl.getName())) {
					elem = StringVal.getString(isSafe);
				} else {
					elem = cl.getDefaultObject(isSafe);
				}
				return new ArrayValue(isSafe, elem, ((ArrayType)objectType));
			}
		}
		return null;
	}

	private boolean isTempVariable(String s)
	{
		assert s != null && s.length() > 0;
		return Character.isDigit(s.charAt(0)) && s.endsWith("_temp");
	}
	
	protected void numerateAllObjects(HashMap<ObjValue, Integer> objHash, List<ObjValue> sortedObjects, boolean skipTempVariables)
	{		
		for (Entry<String, ObjValue> var : values.entrySet())
		{
			if (skipTempVariables && isTempVariable(var.getKey())) continue;
			
			ObjValue obj = var.getValue();
			if (obj instanceof ClassValue && !objHash.containsKey(obj))
			{
				objHash.put(obj, sortedObjects.size());
				sortedObjects.add(obj);
				((ClassValue)obj).getFields().numerateAllObjects(objHash, sortedObjects, skipTempVariables);
			}
		}
	}
	
	public void print(StringBuilder sb, String tab, boolean skipTempVariables)
	{
		HashMap<ObjValue, Integer> objHash = new HashMap<>();
		List<ObjValue> sortedObjects = new LinkedList<>();
		numerateAllObjects(objHash, sortedObjects, skipTempVariables);
		
		for (Entry<String, ObjValue> var : values.entrySet())
		{
			if (skipTempVariables && isTempVariable(var.getKey())) continue;
			
			if (var.getValue() instanceof ClassValue)
			{
				sb.append(tab).append(var.getKey()).append(": ").append("#").append(objHash.get(var.getValue())).append("\n");
			}
			else
			{
				sb.append(tab).append(var.getKey()).append(": ").append(var.getValue()).append("\n");
			}
		}
		
		int order = 0;
		for (ObjValue obj : sortedObjects)
		{
			sb.append(tab).append("#").append(order++).append(": ").append(obj.toString()).append("\n");
			if (obj instanceof ClassValue)
			{
				((ClassValue)obj).print(sb, "   ", objHash); sb.append("\n");
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
}
