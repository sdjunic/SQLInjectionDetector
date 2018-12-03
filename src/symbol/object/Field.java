package symbol.object;

import symbol.Scope;
import symbol.SymbolDataStructure;

public class Field implements Obj {

	private String name;
	private TypeReference type;
	private Modifiers modifiers;
	private Scope myScope;
	
	private String initExpression = null;
	
	public Field(String name, TypeReference type, Modifiers modifiers) {
		this(name, type, modifiers, null);
	}

	public Field(String name, TypeReference type, Modifiers modifiers, String initExpression) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
		this.initExpression = initExpression;
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
	public void setScope(Scope scope) {
		this.myScope = scope;
	}

	@Override
	public Scope getScope() {
		return this.myScope;
	}

	public TypeReference getType() {
		return type;
	}

	public void setType(TypeReference type) {
		this.type = type;
	}

	public Modifiers getModifiers() {
		return modifiers;
	}

	public void setModifiers(Modifiers modifiers) {
		this.modifiers = modifiers;
	}

	public String getInitExpression() {
		return initExpression;
	}

	public void setInitExpression(String initExpression) {
		this.initExpression = initExpression;
	}

	@Override
	public String toString(){
		String ret = (modifiers!=null ? modifiers.toString() : "");
		ret += " ";
		if(type != null) ret += type.toString();
		else ret += "_unknown";
		ret += " " + name;
		return ret;
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append("field: ").append(this);
		if (initExpression != null) {
			sb.append(" = ").append(initExpression);
		}
	}

}
