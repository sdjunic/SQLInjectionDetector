package symbol.object;

import symbol.Scope;
import symbol.SymbolDataStructure;

public class ArrayType implements Type {
	
	private TypeReference type;
	private int arrayLevel;
	
	public ArrayType(TypeReference type, int arrayLevel){
		this.type = type;
		this.arrayLevel = arrayLevel;
	}
	
	@Override
	public String getName() {
		return type.getName();
	}

	public int getArrayLevel(){
		return arrayLevel;
	}
	
	public void setArrayLevel(int arrayLevel) {
		this.arrayLevel = arrayLevel;
	}
	
	@Override
	public SymbolDataStructure getLocals() {
		return null;
	}

	@Override
	public void setScope(Scope scope) {}

	@Override
	public Scope getScope() {
		return null;
	}
	
	public TypeReference getType() {
		return type;
	}

	public void setType(TypeReference type) {
		this.type = type;
	}

	@Override
	public boolean isValueType() {
		return false;
	}

	@Override
	public boolean isRefType() {
		return true;
	}
	
	@Override
	public String toString(){
		String ret = type.toString();
		for (int i=0; i<arrayLevel; ++i){
			ret += "[]";
		}
		return ret;
	}

	@Override
	public void print(StringBuilder sb, int tabNum) { sb.append("ARRAY TYPE SHOULDN'T PRINTED"); }
	
}
