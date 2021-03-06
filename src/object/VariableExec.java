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
	
	public VariableExec next = null; // Used for ternary operator.
	
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
			this.value = NullValue.getNullValue();
		}
		else
		{
			this.value = value;
		}
	}
	
	public boolean isTempVariable()
	{
		return this.name != null && this.name.size() == 1 && this.name.get(0) != null && this.name.get(0).endsWith("_temp");
	}
	
	public boolean haveSameVariableName(List<String> tempVariable)
	{
		if (tempVariable != null && tempVariable.size() == 1 && this.name != null && this.name.size() == 1)
		{
			if (tempVariable.get(0) != null && tempVariable.get(0).equals(this.name.get(0)))
			{
				if (tempVariable.get(0).endsWith("_temp"))
				{
					return true;
				}
			}
		}	
		return false;
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
		if (object != null) assert false; //return object.getName();
		if (value != null) return value.toString();
		return "UNKNOWN";
	}
}
