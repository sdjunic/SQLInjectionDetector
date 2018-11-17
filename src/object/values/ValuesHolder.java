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

	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		Set<Entry<String, ObjValue>> entrySet = values.entrySet();

		Iterator<Entry<String, ObjValue>> it = entrySet.iterator();
		boolean newLine = true;
		while (it.hasNext()) {
			Entry<String, ObjValue> objValue = it.next();
			if (newLine) sb.append("\r\n");
			sb.append(tab).append(objValue.getKey()).append(": ");
			if (objValue.getValue() != null) objValue.getValue().print(sb, tabNum+1);
			else sb.append("null\r\n");
			newLine = false;
		}
		if (newLine) sb.append("\r\n");
	}
}
