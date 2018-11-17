package symbol.object;

import java.util.*;

import Parse.ParseData;
import symbol.Scope;
import symbol.SymbolDataStructure;

public class UnknownType implements Type {

	private List<String> name = null;
	private Scope myScope = null;
	private List<List<String>> importedObjects;
	private List<List<String>> importedScopes;
	
	public UnknownType(List<String> name, List<List<String>> importedObjects, List<List<String>> importedScopes, Scope myScope){
		this.name = name;
		this.myScope = myScope;
		this.importedObjects = importedObjects;
		this.importedScopes = importedScopes;
	}
	
	public UnknownType(String name, List<List<String>> importedObjects, List<List<String>> importedScopes, Scope myScope){
		List<String> listName = new LinkedList<String>();
		listName.add(name);
		this.name = listName;
		this.myScope = myScope;
		this.importedObjects = importedObjects;
		this.importedScopes = importedScopes;
	}
	
	@Override
	public String getName() {
		return ParseData.makeFullNameWithDots(name);
	}

	public List<String> getFullName() {
		return this.name;
	}
	
	@Override
	public SymbolDataStructure getLocals() {
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

	public List<List<String>> getImportedObjects() {
		return importedObjects;
	}

	public void setImportedObjects(List<List<String>> importedObjects) {
		this.importedObjects = importedObjects;
	}

	public List<List<String>> getImportedScopes() {
		return importedScopes;
	}

	public void setImportedScopes(List<List<String>> importedScopes) {
		this.importedScopes = importedScopes;
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
	public String toString() {
		return getName();
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) { sb.append("UNKNOWN TYPE SHOULDN'T BE PRINTED"); }

}
