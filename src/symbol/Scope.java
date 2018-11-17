package symbol;

import symbol.object.*;
import java.util.Collection;

public class Scope {

	private Scope outer;
	private int level;
	private Obj parrentObj = null;
	
	private SymbolDataStructure locals; 
	
	public Scope(Scope outer, int level, Obj parrentObj) {
		this.outer = outer;
		this.level = level;
		this.parrentObj =parrentObj;
		this.locals = new SymbolDataStructure();
	}
	
	public boolean addToLocals(Obj o) {
		return locals.insertKey(o);
	}
	
	public Obj findSymbol(String objName) {
		return locals.searchKey(objName);
	}
	
	public Scope getOuter() {
		return outer;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Obj getParrentObj() {
		return parrentObj;
	}

	public void setParrentObj(Obj parrentObj) {
		this.parrentObj = parrentObj;
	}

	public SymbolDataStructure getLocals() {
		return locals;
	}
	
	public Collection<Obj> values() {
		return locals.symbols();
	}
	
	public void print(StringBuilder sb){
		locals.print(sb, 0);
	}
}
