package symbol;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import symbol.object.Obj;

public class SymbolDataStructure {

	protected Map<String, Obj> hashTable = new LinkedHashMap<String, Obj>();
	
	public Obj searchKey(String key) {
		return hashTable.get(key);
	}

	public Obj deleteKey(String key) {
		Obj o = null;
		if (hashTable.containsKey(key)) {
			o = hashTable.remove(key);
		}
		
		return o;
	}

	public boolean insertKey(Obj node) {
		if (hashTable.containsKey(node.getName())) 
			return false;
		else{
			hashTable.put(node.getName(), node);
			return true;
		}
	}
	
	public Collection<Obj> symbols() {
		return hashTable.values();
	}

	public int numSymbols() {
		return hashTable.size();
	}
	
	public void print(StringBuilder sb, int tabNum) {
		Iterator<Obj> it = hashTable.values().iterator();
		Obj obj = null;
		while(it.hasNext()){
			obj = it.next();
			obj.print(sb, tabNum);
			if (it.hasNext()) sb.append("\r\n");
		}
	}
	

}
