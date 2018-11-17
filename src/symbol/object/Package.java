package symbol.object;

import symbol.*;

public class Package implements Obj {

	private String name;	
	private Scope myScope = null;
	
	public Package(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public SymbolDataStructure getLocals() {
		if (this.myScope != null) return this.myScope.getLocals();
		else return null;
	}
	
	@Override
	public void setScope(Scope scope){
		this.myScope = scope;
	}
	
	@Override
	public Scope getScope(){
		return this.myScope;
	}

	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append("\r\n").append(tab).append("package: ").append(name).append("\r\n").append("\r\n");
		if(getLocals() != null) getLocals().print(sb, tabNum+1);
	}

}
