package symbol.object;

import symbol.Scope;
import symbol.SymbolDataStructure;

public class Block implements Obj {

	private static int blockCounter = 0;
	
	private int myNumber;
	private Scope myScope;
	
	public Block() {
		myNumber = ++blockCounter;
	}
	
	@Override
	public String getName() {
		return String.valueOf(myNumber);
	}

	@Override
	public SymbolDataStructure getLocals() {
		if (this.myScope != null) return this.myScope.getLocals();
		else return null;
	}

	@Override
	public void setScope(Scope scope) {
		this.myScope = scope;
	}

	@Override
	public Scope getScope() {
		return myScope;
	}

	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append("{").append("\r\n");
		if(getLocals() != null) getLocals().print(sb, tabNum+1);
		sb.append("\r\n").append(tab).append("}");
	}
	
	
	
}
