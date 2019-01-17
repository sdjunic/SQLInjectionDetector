package symbol;

import symbol.object.*;
import symbol.object.Class;
import symbol.object.Package;
import symbol.object.Modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

import Parse.ParseData;
import libraryMethod.SpecialAction;
import main.LibraryClassDecl;
import main.LibraryMethodDecl;

public class Table {
	
	public static Table currentTable = null;
	
	private Scope currentScope, universe;
	private Class stringClass;
	private int currentLevel;
	
	public static void makeNewTable(List<LibraryClassDecl> libClassDecl, List<LibraryMethodDecl> libMethodDecl) {
		currentTable = new Table();
		currentTable.currentLevel = -1;
		currentTable.universe = currentTable.currentScope = new Scope(null, currentTable.currentLevel, null);
		
		currentTable.stringClass = new Class("String", null);
		openScope(currentTable.stringClass);
		currentTable.stringClass.setScope(currentTable.currentScope);
		closeScope();
		
		insert(new PrimitiveType("boolean"));
		insert(new PrimitiveType("byte"));
		insert(new PrimitiveType("short"));
		insert(new PrimitiveType("int"));
		insert(new PrimitiveType("long"));
		insert(new PrimitiveType("char"));
		insert(new PrimitiveType("float"));
		insert(new PrimitiveType("double"));
		insert(currentTable.stringClass);
		
		for (LibraryClassDecl classDecl : libClassDecl)
		{
			Table.setScope(Table.universe());
			
			if (classDecl.packageName != null)
			{
				Obj packageObj = Table.find(classDecl.packageName);
				symbol.object.Package currentPackage = null;
				if (packageObj != null && packageObj instanceof symbol.object.Package) {
					currentPackage = (symbol.object.Package)packageObj;
					Table.setScope(currentPackage.getScope());
				} else {
					currentPackage = new symbol.object.Package(classDecl.packageName);
					Table.insert(currentPackage);
					Table.openScope(currentPackage);		
					currentPackage.setScope(Table.currentScope());
				}
			}
			
			Obj classObj = Table.find(classDecl.className);
			if (classObj != null && classObj instanceof symbol.object.Class) {
				continue;
			}
			
			Class currentClass = new symbol.object.Class(classDecl.className, null);
			Table.insert(currentClass);
			Table.openScope(currentClass);		
			currentClass.setScope(Table.currentScope());
			
			if (classDecl.fields != null && !classDecl.fields.isEmpty())
			{
				for (LibraryClassDecl.LibField f : classDecl.fields)
				{
					if (currentClass.containsField(f.name))
					{
						continue;
					}
					
					Obj obj = Table.find(f.type);
					symbol.object.Type type = null;
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
						type = (symbol.object.Type)obj;
					else {
						type = new UnknownType(f.type, null, null, Table.currentScope());
					}
					
					Table.insert(new Field(f.name, new TypeReference(type), null, null));
				}
			}
		}
		
		for (LibraryMethodDecl methDecl : libMethodDecl) {
			Table.setScope(Table.universe());
			
			if (methDecl.packageName != null)
			{
				Obj packageObj = Table.find(methDecl.packageName);
				symbol.object.Package currentPackage = null;
				if (packageObj != null && packageObj instanceof symbol.object.Package) {
					currentPackage = (symbol.object.Package)packageObj;
					Table.setScope(currentPackage.getScope());
				} else {
					currentPackage = new symbol.object.Package(methDecl.packageName);
					Table.insert(currentPackage);
					Table.openScope(currentPackage);		
					currentPackage.setScope(Table.currentScope());
				}
			}
			
			Obj classObj = Table.find(methDecl.className);
			Class currentClass = null;
			if (classObj != null && classObj instanceof symbol.object.Class) {
				currentClass = (symbol.object.Class)classObj;
				Table.setScope(currentClass.getScope());
			} else {
				currentClass = new symbol.object.Class(methDecl.className, null);
				Table.insert(currentClass);
				Table.openScope(currentClass);		
				currentClass.setScope(Table.currentScope());
			}
			
			Obj methodObj = Table.find(methDecl.methodName);
			if (methodObj != null && methodObj instanceof symbol.object.Method) {
				continue;
			}
			
			Method currentMethod = new symbol.object.Method(methDecl.methodName, methDecl.className.equals(methDecl.methodName));
			int i = 0;
			for (String arg : methDecl.methodArgs) {
				Obj obj = Table.find(arg);
				symbol.object.Type type = null;
				if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
					type = (symbol.object.Type)obj;
				else {
					type = new UnknownType(arg, null, null, Table.currentScope());
				}
				currentMethod.addFormalParam(new MethParam(new TypeReference(type), "temp" + ++i));
			}
			currentMethod.complFormalParamAdding();
			
			if (methDecl.retTypeName != null) {
				if (methDecl.retTypePackage == null || methDecl.retTypePackage.isEmpty())
				{
					Obj obj = Table.find(methDecl.retTypeName);
					symbol.object.Type retType = null;
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
						retType = (symbol.object.Type)obj;
					else {
						retType = new UnknownType(methDecl.retTypeName, null, null, Table.currentScope());
					}
					currentMethod.setRetType(new TypeReference(retType));
				}
				else
				{
					symbol.object.Type retType = null;
					
					List<String> returnTypeAbsolutePath = new LinkedList<>();
					returnTypeAbsolutePath.add(methDecl.retTypePackage);
					returnTypeAbsolutePath.add(methDecl.retTypeName);		
					
					symbol.object.Obj obj = ParseData.findObjectByAbsolutePath(returnTypeAbsolutePath);
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class) {
						retType = (symbol.object.Type)obj;
					}
					else {
						List<List<String>> importedObjects = new LinkedList<>();
						importedObjects.add(returnTypeAbsolutePath);
						retType = new UnknownType(methDecl.retTypeName, importedObjects, null, Table.currentScope());
					}
					currentMethod.setRetType(new TypeReference(retType));
				}
			}
			if (methDecl.isStatic) {
				currentMethod.setModifiers(new Modifiers(Modifier.STATIC));
			}
			
			for (SpecialAction specAction : methDecl.getSpecialActions())
			{
				specAction.setReturnType(currentMethod.getRetType());
			}
			
			currentMethod.setDefined(false);
			currentMethod.setSpecialActions(methDecl.getSpecialActions());
			Table.insert(currentMethod);
			Table.openScope(currentMethod);		
			currentMethod.setScope(Table.currentScope());
		}
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