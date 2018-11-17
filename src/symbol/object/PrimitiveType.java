package symbol.object;

import symbol.Scope;
import symbol.SymbolDataStructure;
import symbol.Table;

public class PrimitiveType implements Type {
	
	private String name;
	
	public PrimitiveType(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public SymbolDataStructure getLocals() {
		return null;
	}

	@Override
	public void setScope(Scope scope){}
	
	@Override
	public Scope getScope(){
		return Table.universe();
	}
	
	@Override
	public boolean isValueType() {
		return true;
	}

	@Override
	public boolean isRefType() {
		return false;
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append(name);
	}
	
	@Override
	public String toString(){
		return name;
	}

}
