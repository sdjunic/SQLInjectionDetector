package object;

import java.util.*;

import Parse.ParseData;
import object.values.NullValue;
import object.values.ObjValue;
import symbol.object.*;
import symbol.object.Class;

public class VariableExec {
	
	public List<String> name = null;
	public Obj object = null;
	public ObjValue value = null;
	
	public VariableExec(List<String> name, Obj object) {
		this.name = name;
		this.object = object;
	}
	
	public VariableExec(String name, Obj object) {
		List<String> lname = new LinkedList<String>();
		lname.add(name);
		this.name = lname;
		this.object = object;
	}
	
	public VariableExec(ObjValue value) {
		if (value == null)
		{
			this.value = new NullValue();
		}
		else
		{
			this.value = value;
		}
	}
	
	public Class getObjectType() {
		if (object == null) return null;
		if (object instanceof Class) return (Class)object;
		if (object instanceof Field) return (Class)(((Field)object).getType().type);
		if (object instanceof Variable) return (Class)(((Variable)object).getType().type);
		if (object instanceof MethParam) return (Class)(((MethParam)object).getType().type);
		return null;
	}
	
	@Override
	public String toString(){
		if (name != null) return ParseData.makeFullNameWithDots(name);
		if (object != null) return object.getName();
		if (value != null) return value.toString();
		return "UNKNOWN";
	}
}
