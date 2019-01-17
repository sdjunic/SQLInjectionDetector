package main;

import java.util.*;

import javax.swing.SpinnerDateModel;

import libraryMethod.SpecialAction;

public class LibraryMethodDecl {
	
	public String packageName;
	public String className;
	public String methodName;
	public String retTypePackage;
	public String retTypeName;
	public boolean isStatic;
	public List<String> methodArgs;
	
	private List<SpecialAction> specialActions;
	
	public LibraryMethodDecl() {
		this.packageName = null;
		this.className = null;
		this.methodName = null;
		this.retTypePackage = null;
		this.retTypeName = null;
		this.isStatic = false;
		this.methodArgs = new LinkedList<String>();
		this.specialActions = new LinkedList<SpecialAction>();;
	}
	
	public LibraryMethodDecl(String packageName, String className, String methodName, String retTypePackage, String retTypeName, boolean isStatic, List<String> methodArgs,  List<SpecialAction> specialActions) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		this.retTypePackage = retTypePackage;
		this.retTypeName = retTypeName;
		this.isStatic = isStatic;
		this.methodArgs = methodArgs;
		this.specialActions = specialActions;
	}

	public LibraryMethodDecl(String packageName, String methodName, String className) {
		this(packageName, methodName, className, null, null, false, new LinkedList<String>(), new LinkedList<SpecialAction>());
	}
	
	public boolean isCriticalOutput() {
		for (SpecialAction action : specialActions) {
			if (action.isCriticalOutput()) return true;
		}
		return false;
	}
	
	public void addSpecialAction(String left, String expression)
	{		
		SpecialAction specAction = new SpecialAction(left, expression, this);
		this.specialActions.add(specAction);
	}
	
	public List<SpecialAction> getSpecialActions()
	{
		return this.specialActions;
	}
	
	public String getMethSign() {
		String methSign = "";
		if (retTypeName == null || retTypeName.isEmpty() || retTypeName.equals("void")) methSign+= "void ";
		else methSign += (retTypePackage == null ? "" : retTypePackage) + retTypeName + " ";
		
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
}
