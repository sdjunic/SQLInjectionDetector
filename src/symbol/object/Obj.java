package symbol.object;

import symbol.*;

public interface Obj {
	
	public String getName();
	
	public SymbolDataStructure getLocals();
	
	public void setScope(Scope scope);
	
	public Scope getScope();
	
	public void print(StringBuilder sb, int tabNum);
}
