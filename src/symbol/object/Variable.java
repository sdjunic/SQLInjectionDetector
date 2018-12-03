package symbol.object;

import symbol.Scope;
import symbol.SymbolDataStructure;

public class Variable implements Obj {

	private Modifiers modifers;
	private TypeReference type;
	private String name;
	private Scope myScope;
	
	public boolean sure;

	public Variable(String name, TypeReference type){
		this(name, type, null);
	}
	
	public Variable(String name, TypeReference type, Modifiers modifers){
		this.type = type;
		this.name = name;
		this.modifers = modifers;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public SymbolDataStructure getLocals() {
		if (type.isRefType() && type.type instanceof Class)
		{
			return ((Class)type.type).getLocals();
		}
		return null;
	}

	@Override
	public void setScope(Scope scope){
		this.myScope = scope;
	}
	
	@Override
	public Scope getScope(){
		return this.myScope;
	}
	
	public TypeReference getType() {
		return type;
	}

	public void setType(TypeReference type) {
		this.type = type;
	}

	@Override
	public String toString(){
		String ret = "";
		if (modifers != null) ret = modifers + " ";
		if(type != null) ret += type.toString();
		else ret += "_unknown";
		ret += " " + name;
		return ret;
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append("var: ").append(this);
	}
	
}
