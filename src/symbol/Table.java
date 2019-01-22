package symbol;

import symbol.object.*;
import symbol.object.Class;
import symbol.object.Package;
import symbol.object.Modifiers.Modifier;

import java.time.chrono.IsoChronology;
import java.util.LinkedList;
import java.util.List;

import Parse.ParseData;
import javaLibrary.SpecialAction;
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
		
		Class objectClass = new Class("Object", null);
		openScope(objectClass);
		objectClass.setScope(currentTable.currentScope);
		closeScope();
		
		currentTable.stringClass = new Class("String", new TypeReference(objectClass));
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
		insert(objectClass);
		insert(currentTable.stringClass);
		
		for (LibraryClassDecl classDecl : libClassDecl)
		{
			// find super class if it exists
			Table.setScope(Table.universe());
			
			if (classDecl.superClassPackageName != null)
			{
				Obj superClassPackageObj = Table.find(classDecl.superClassPackageName);
				symbol.object.Package currentPackage = null;
				if (superClassPackageObj != null && superClassPackageObj instanceof symbol.object.Package) {
					currentPackage = (symbol.object.Package)superClassPackageObj;
					Table.setScope(currentPackage.getScope());
				} else {
					assert false; // super class and its package must be created before this point
				}
			}			
			Obj superClassObj = Table.find(classDecl.superClass);
			assert (superClassObj != null && superClassObj instanceof symbol.object.Class);		
			Class superClass = (Class)superClassObj;
			
			// find or make actual class
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
			
			Class currentClass = new symbol.object.Class(classDecl.className, new TypeReference(superClass));
			Table.insert(currentClass);
			Table.openScope(currentClass);		
			currentClass.setScope(Table.currentScope());
			
			currentClass.setAlwaysUnsafe(classDecl.alwaysUnsafe);
			
			if (classDecl.fields != null && !classDecl.fields.isEmpty())
			{
				for (LibraryClassDecl.LibField f : classDecl.fields)
				{
					if (currentClass.containsField(f.name))
					{
						continue;
					}
					
					if (f.typePackage == null || f.typePackage.isEmpty())
					{
						String typeName = ParseData.getTypeIfArray(f.typeName);				
						Obj obj = Table.find(typeName);
						
						symbol.object.Type type = null;
						if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
							type = (symbol.object.Type)obj;
						else {
							type = new UnknownType(typeName, null, null, Table.currentScope());
						}
						
						TypeReference typeRef = new TypeReference(type);
						int arrayLevel = ParseData.getTypeArrayLevel(f.typeName);
						typeRef.addArrayLevel(arrayLevel);
						
						Table.insert(new Field(f.name, typeRef, null, null));
					}
					else
					{
						symbol.object.Type type = null;
						String typeName = ParseData.getTypeIfArray(f.typeName);
						
						List<String> typeAbsolutePath = new LinkedList<>();
						typeAbsolutePath.add(f.typePackage);
						typeAbsolutePath.add(typeName);	
						
						symbol.object.Obj obj = ParseData.findObjectByAbsolutePath(typeAbsolutePath);
						if (obj != null || obj instanceof PrimitiveType || obj instanceof Class) {
							type = (symbol.object.Type)obj;
						}
						else {
							List<List<String>> importedObjects = new LinkedList<>();
							importedObjects.add(typeAbsolutePath);
							type = new UnknownType(typeName, importedObjects, null, Table.currentScope());
						}
						
						TypeReference typeRef = new TypeReference(type);
						int arrayLevel = ParseData.getTypeArrayLevel(f.typeName);
						typeRef.addArrayLevel(arrayLevel);
						
						Table.insert(new Field(f.name, typeRef, null, null));
					}
				}
			}
			
			closeScope();
		}
		
		for (LibraryMethodDecl methDecl : libMethodDecl) {
			if (methDecl.isConstructor)
			{
				assert methDecl.className.equals(methDecl.methodName);
				assert methDecl.retTypePackage == methDecl.packageName && methDecl.retTypeName == methDecl.className;
			}
			
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
				currentClass = new symbol.object.Class(methDecl.className, new TypeReference(objectClass));
				Table.insert(currentClass);
				Table.openScope(currentClass);		
				currentClass.setScope(Table.currentScope());
			}
			
			Obj methodObj = Table.find(methDecl.methodName);
			if (methodObj != null && methodObj instanceof symbol.object.Method) {
				continue;
			}
			
			Method currentMethod = new symbol.object.Method(methDecl.methodName, methDecl.isConstructor);
			int i = 0;
			for (String argTypeStr : methDecl.methodArgs) {
				TypeReference typeRef = SpecialAction.getTypeForClassName(argTypeStr);
				currentMethod.addFormalParam(new MethParam(typeRef, "temp" + ++i));
			}
			currentMethod.complFormalParamAdding();
			
			if (methDecl.retTypeName != null) {
				if (methDecl.retTypePackage == null || methDecl.retTypePackage.isEmpty())
				{
					String typeName = ParseData.getTypeIfArray(methDecl.retTypeName);
					Obj obj = Table.find(typeName);
					
					symbol.object.Type retType = null;
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
						retType = (symbol.object.Type)obj;
					else {
						retType = new UnknownType(typeName, null, null, Table.currentScope());
					}
					
					TypeReference typeRef = new TypeReference(retType);
					int arrayLevel = ParseData.getTypeArrayLevel(methDecl.retTypeName);
					typeRef.addArrayLevel(arrayLevel);
					
					currentMethod.setRetType(typeRef);
				}
				else
				{
					symbol.object.Type retType = null;
					String typeName = ParseData.getTypeIfArray(methDecl.retTypeName);
					
					List<String> returnTypeAbsolutePath = new LinkedList<>();
					returnTypeAbsolutePath.add(methDecl.retTypePackage);
					returnTypeAbsolutePath.add(typeName);		
					
					symbol.object.Obj obj = ParseData.findObjectByAbsolutePath(returnTypeAbsolutePath);
					if (obj != null || obj instanceof PrimitiveType || obj instanceof Class) {
						retType = (symbol.object.Type)obj;
					}
					else {
						List<List<String>> importedObjects = new LinkedList<>();
						importedObjects.add(returnTypeAbsolutePath);
						retType = new UnknownType(typeName, importedObjects, null, Table.currentScope());
					}
					
					TypeReference typeRef = new TypeReference(retType);
					int arrayLevel = ParseData.getTypeArrayLevel(methDecl.retTypeName);
					typeRef.addArrayLevel(arrayLevel);
					
					currentMethod.setRetType(typeRef);
				}
			}
			if (methDecl.isStatic) {
				currentMethod.setModifiers(new Modifiers(Modifier.STATIC));
			}
			
			currentMethod.setDefined(false);
			Table.insert(currentMethod);
			Table.openScope(currentMethod);
			currentMethod.setSpecialActions(methDecl.getSpecialActions());
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