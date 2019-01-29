package main;

import java.util.*;

import javaLibrary.SpecialAction;

public class LibraryMethodDecl {
	
	public String packageName;
	public String className;
	public String methodName;
	public String retTypePackage;
	public String retTypeName;
	public boolean isConstructor;
	public boolean isStatic;
	public List<String> methodArgs;
	
	private List<SpecialAction> specialActions;
	
	public LibraryMethodDecl() {
		this.packageName = null;
		this.className = null;
		this.methodName = null;
		this.retTypePackage = null;
		this.retTypeName = null;
		this.isConstructor = false;
		this.isStatic = false;
		this.methodArgs = new LinkedList<String>();
		this.specialActions = new LinkedList<SpecialAction>();;
	}
	
	public LibraryMethodDecl(String packageName, String className, String methodName, String retTypePackage, String retTypeName, boolean isConstructor, boolean isStatic, List<String> methodArgs,  List<SpecialAction> specialActions) {
		this.packageName = packageName == null || packageName.trim().isEmpty() ? null : packageName.trim();
		this.className = className == null || className.trim().isEmpty() ? null : className.trim();
		this.methodName = methodName == null || methodName.trim().isEmpty() ? null : methodName.trim();
		this.retTypePackage = retTypePackage == null || retTypePackage.trim().isEmpty() ? null : retTypePackage.trim();
		this.retTypeName = retTypeName == null || retTypeName.trim().isEmpty() ? null : retTypeName.trim();
		this.isConstructor = isConstructor;
		this.isStatic = isStatic;
		this.methodArgs = methodArgs;
		this.specialActions = specialActions;
	}

	public LibraryMethodDecl(String packageName, String methodName, String className) {
		this(packageName, methodName, className, null, null, false, false, new LinkedList<String>(), new LinkedList<SpecialAction>());
	}
	
	public boolean isCriticalOutput() {
		for (SpecialAction action : specialActions) {
			if (action.isCriticalOutput()) return true;
		}
		return false;
	}
	
	public void addSpecialAction(String left, String assign)
	{
		assert assign != null && assign.equals(SpecialAction.CRITICAL_OUTPUT);
		addSpecialAction(left, assign, null);
	}
	
	public void addSpecialAction(String left, String assign, String expression)
	{		
		SpecialAction specAction = new SpecialAction(left, assign, expression, this);
		this.specialActions.add(specAction);
	}
	
	public List<SpecialAction> getSpecialActions()
	{
		return this.specialActions;
	}
	
	public String getMethSign() {
		String methSign = "";
		
		if (!isConstructor)
		{
			if (retTypeName == null || retTypeName.isEmpty() || retTypeName.equals("void")) methSign+= "void ";
			else methSign += (retTypePackage == null ? "" : retTypePackage) + retTypeName + " ";
		}
		
		if (methodName != null && !methodName.isEmpty()) methSign += methodName;
		else methSign += "noName";
		methSign += "(";
		for (int i=0; i<methodArgs.size()-1; ++i) {
			methSign += methodArgs.get(i) + ", ";
		}
		if (!methodArgs.isEmpty()) methSign += methodArgs.get(methodArgs.size()-1);
		methSign += ")";
		return methSign;
	}
	
	@Override
	public String toString() {
		StringBuilder methSign = new StringBuilder(getMethSign());
		for (SpecialAction specAction : specialActions)
		{
			methSign.append("\n\t").append(specAction.toString());
		}
		return methSign.toString();
	}
}
