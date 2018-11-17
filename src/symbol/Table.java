package symbol;

import symbol.object.*;
import symbol.object.Class;
import symbol.object.Package;
import Parse.ParseData;

public class Table {
	
	public static Table currentTable = null;
	
	private Scope currentScope, universe;
	private Class stringClass;
	private int currentLevel;
	
	public static void makeNewTable() {
		currentTable = new Table();
		currentTable.currentLevel = -1;
		currentTable.universe = currentTable.currentScope = new Scope(null, currentTable.currentLevel, null);
		currentTable.stringClass = new Class("String", null);
		insert(new PrimitiveType("boolean"));
		insert(new PrimitiveType("byte"));
		insert(new PrimitiveType("short"));
		insert(new PrimitiveType("int"));
		insert(new PrimitiveType("long"));
		insert(new PrimitiveType("char"));
		insert(new PrimitiveType("float"));
		insert(new PrimitiveType("double"));
		insert(currentTable.stringClass);
	}
	
	public static void init() {
		Obj defPack = currentTable.universe.findSymbol("_defaultPackage");
		if (defPack != null && defPack instanceof Package){
			currentTable.currentScope = ((Package)defPack).getScope();
			ParseData.currentPackage = (Package)defPack;
		} else {
			Package defPackage = new Package("_defaultPackage");
			currentTable.universe.addToLocals(defPackage);
			openScope(defPackage);
			defPackage.setScope(currentTable.currentScope);
			ParseData.currentPackage = defPackage;
		}
	}
	
	public static void openScope(Obj outerScopeObj) {
		currentTable.currentScope = new Scope(currentTable.currentScope, ++currentTable.currentLevel, outerScopeObj);
	}

	public static void closeScope() {
		currentTable.currentScope = currentTable.currentScope.getOuter();
		--currentTable.currentLevel;
	}

	public static void setScope(Scope s) {
		currentTable.currentScope = s;
		currentTable.currentLevel = s.getLevel();
	}
	
	public static boolean insert(Obj newObj) {
		return currentTable.currentScope.addToLocals(newObj);
	}
	
	public static Obj find(String name) {
		Obj resultObj = null;
		for (Scope s = currentTable.currentScope; s != null; s = s.getOuter()) {
			if (s.getLocals() != null) {
				resultObj = s.getLocals().searchKey(name);
				if (resultObj != null) break;
			}
		}
		return resultObj;
	}
	
	public static Scope currentScope() {
		return currentTable.currentScope;
	}
	
	public static Scope parrentScope() {
		if (currentTable.currentScope == null) return null;
		return currentTable.currentScope.getOuter();
	}
	
	public static void setCurrentScope(Scope newScope) {
		currentTable.currentScope = newScope;
	}
	
	public static Scope universe() {
		return currentTable.universe;
	}
	
	public static void setUniverse(Scope newScope) {
		currentTable.universe = newScope;
	}
	
	public static Class getStringClass() {
		return currentTable.stringClass;
	}
	
	public static void print(StringBuilder sb){
		sb.append("------------------- SYMBOL TABLE -------------------\r\n\r\n");
		currentTable.universe.print(sb);
		sb.append("\r\n----------------- END SYMBOL TABLE -----------------\r\n");
	}
	
}