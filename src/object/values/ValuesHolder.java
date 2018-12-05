package object.values;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import symbol.object.Class;
import symbol.object.Type;

public abstract class ValuesHolder {
	
	protected Map<String, ObjValue> values;

	public ValuesHolder() {
		values = new HashMap<String, ObjValue>();
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

	public abstract ObjValue get(String name);
	
	public void remove(String var)
	{
		values.remove(var);
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

	protected void numberAllObjects(HashMap<ObjValue, Integer> objHash, IntRef num)
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
}
