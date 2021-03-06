package Parse;

import java.util.*;

import symbol.Scope;
import symbol.Table;
import symbol.object.*;
import symbol.object.Class;
import symbol.object.Package;

public class ParseData {
	
	public static Set<Method> riskyMethods = null;
	public static Set<Method> mainMethods = null;
	public static Set<Method> allProjectMethods = null;
	
	public static Package currentPackage = null;
	public static Method currentMethod = null;
	
	public static final String checkStmtString = "__check_";

	public static String makeFullNameWithDots(List<String> name) {
		if (name == null || name.isEmpty()) return null;
		String fullName = name.get(0);
		for (int i=1; i<name.size(); ++i){
			fullName+="."+name.get(i);
		}
		return fullName;
	}
	
	public static boolean isSimpleName(List<String> name) {
		return name.size() == 1;
	}
	
	public static void lookForMainMethod() {
		allProjectMethods.add(currentMethod);
		if (currentMethod.getName().startsWith("main(") && currentMethod.isStatic() && currentMethod.getRetType() == null) {
			List<MethParam> args = currentMethod.getMethParamList();
			if (args.size() == 1 && args.get(0).getType() != null && args.get(0).getType().type != null) {
				Type argType = args.get(0).getType().type;
				if (argType instanceof ArrayType) {
					ArrayType arrayType = (ArrayType)argType;
					if (arrayType.getArrayLevel() == 1 && arrayType.getType().type.getName().equals("String")) {
						mainMethods.add(currentMethod);
					}
				}
			}
		}
	}
	
	public static boolean isClassImported(String packageName, String className, List<List<String>> importedObjects, List<List<String>> importedScopes) {
		for (List<String> importedObj : importedObjects) {
			if (makeFullNameWithDots(importedObj).equals(packageName + "." + className)) return true;
		}
		for (List<String> importedScope : importedScopes) {
			if (makeFullNameWithDots(importedScope).equals(packageName)) return true;
		}
		return false;
	}
	
	public static Package findPackage(String packageToFind) {
		Obj ret = Table.universe().findSymbol(packageToFind);
		if (ret != null && ret instanceof Package) return (Package)ret;
		else return null;
	}
	
	public static Obj findObjectByAbsolutePath(List<String> name) {
		Obj obj = Table.universe().findSymbol(makeFullNameWithDots(name));
		if (obj != null) return obj;
		String packageName =  "";
		for (int i=0; i<name.size()-1; ++i){
			packageName += (i==0?"":".") + name.get(i);
			obj = findPackage(packageName);
			int j = i+1;
			while (obj != null && j < name.size()) {
				if (obj.getLocals() != null)
					obj = obj.getLocals().searchKey(name.get(j++));
				else
					break;
			}
			if (obj != null && j == name.size()) return obj;
		}
		obj = Table.universe().findSymbol("_defaultPackage");
		int i=0;
		while (obj != null && i < name.size()) {
			if (obj.getLocals() != null)
				obj = obj.getLocals().searchKey(name.get(i++));
			else
				break;
		}
		if (i == name.size()) return obj;
		else return null;
	}
	
	public static Package getOuterPackageForScope(Scope s) {
		while (s != null && s.getParrentObj() != null && !(s.getParrentObj() instanceof Package)) {
			s = s.getOuter();
		}
		if (s != null && s.getParrentObj() != null)	return (Package)s.getParrentObj();
		else return null;
	}
	
	public static Class getOuterClassForScope(Scope s) {
		while (s != null && s.getParrentObj() != null && !(s.getParrentObj() instanceof Class)) {
			s = s.getOuter();
		}
		if (s != null && s.getParrentObj() != null)	return (Class)s.getParrentObj();
		else return null;
	}
	
	public static Obj findName(List<String> name, List<List<String>> importedObjects, List<List<String>> importedScopes, Scope currentScope) {		
		// Look in the current scope.
		int i=0;
		Obj obj = null;
		boolean nameStartsWithThisOrSuper = true;
		while (nameStartsWithThisOrSuper && i < name.size()) {
			if ("this".equals(name.get(i))) {
				obj = getOuterClassForScope(currentScope);
			} else if ("super".equals(name.get(i))){
				Class cl = getOuterClassForScope(currentScope);
				if (cl.getSuperClass() != null && cl.getSuperClass().type != null) {
					obj = cl.getSuperClass().type;
				}
			} else {
				Scope scope = currentScope;
				if (obj != null) scope = obj.getScope();
				obj = scope.findSymbol(name.get(i));
				nameStartsWithThisOrSuper = false;
				
				if (i < name.size() - 1) {
					if (obj instanceof symbol.object.Variable) obj = ((symbol.object.Variable)obj).getType().type;
					else if (obj instanceof MethParam) obj = ((MethParam)obj).getType().type;
					else if (obj instanceof Field) obj = ((Field)obj).getType().type;
				}
			}
			++i;
		}
		while (obj != null && i < name.size()){
			if (obj.getLocals() != null)
				obj = obj.getLocals().searchKey(name.get(i++));
			else
				break;
		}
		if (obj != null && i == name.size()) return obj;
		
		// Look in the parent scopes.
		Scope parentScope = currentScope.getOuter();
		while (parentScope != null) {
			i=0;
			obj = parentScope.findSymbol(name.get(i++));
			while (obj != null && i < name.size()){
				if (obj.getLocals() != null)
					obj = obj.getLocals().searchKey(name.get(i++));
				else
					break;
			}
			if (obj != null && i == name.size()) return obj;
			parentScope = parentScope.getOuter();
		}
		
		// Look for the absolute path.
		obj = findObjectByAbsolutePath(name);
		if (obj != null) return obj;
		
		// Look in the current package.
		i=0;
		obj = getOuterPackageForScope(currentScope);
		while (obj != null && i < name.size()){
			if (obj.getLocals() != null)
				obj = obj.getLocals().searchKey(name.get(i++));
			else
				break;
		}
		if (obj != null && i == name.size()) return obj;
		
		// Look in the imported objects.
		if (importedObjects != null) {
			Iterator<List<String>> it = importedObjects.iterator();
			while (it.hasNext()) {
				List<String> importedObj = it.next();
				if ((importedObj.get(importedObj.size()-1)).equals(name.get(0))){
					List<String> absolutePath = new LinkedList<>(importedObj);
					for (int j=1; j<name.size(); ++j) absolutePath.add(name.get(j));
					obj = findObjectByAbsolutePath(absolutePath);
					if (obj != null) return obj;
				}
			}
		}
		
		// Look in the imported scopes.
		if (importedScopes != null) {
			Iterator<List<String>> it = importedScopes.iterator();
			while (it.hasNext()) {
				List<String> importedObj = it.next();
				List<String> absolutePath = new LinkedList<>(importedObj);
				for (int j=0; j<name.size(); ++j) absolutePath.add(name.get(j));
				obj = findObjectByAbsolutePath(absolutePath);
				if (obj != null) return obj;
			}
		}
		
		return null;	
	}
	
	public static boolean isCheckStmt(List<String> methName)
	{
		String fullMethName = makeFullNameWithDots(methName);
		return fullMethName.startsWith(checkStmtString);
	}
	
	public static int getCheckThreadNum(List<String> methName)
	{
		assert isCheckStmt(methName);
		String fullMethName = makeFullNameWithDots(methName);
		String num = fullMethName.substring(checkStmtString.length());
		return Integer.parseInt(num);
	}
	
	public static String getTypeIfArray(String type)
	{
		int i = (type + "[").indexOf("[");
		return type.substring(0, i);
	}
	
	public static int getTypeArrayLevel(String type)
	{
		int leftBr = 0;
		int rightBr = 0;
		for (int i = 0; i < type.length(); ++i)
		{
			if (type.charAt(i) == '[') ++leftBr;
			else if (type.charAt(i) == ']') ++rightBr;
		}
		assert leftBr == rightBr;
		return leftBr;
	}
}
