package main;

import java.util.*;

public class LibraryMethodDecl {
	
	public String packageName;
	public String className;
	public String methodName;
	public String retType;
	public boolean isStatic;
	public List<String> methodArgs;
	
	public List<SpecialArg> specialArguments;
	
	public LibraryMethodDecl() {
		this.packageName = null;
		this.className = null;
		this.methodName = null;
		this.retType = null;
		this.isStatic = false;
		this.methodArgs = new LinkedList<String>();
		this.specialArguments = new LinkedList<SpecialArg>();;
	}
	
	public LibraryMethodDecl(String packageName, String className, String methodName, String retType, boolean isStatic, List<String> methodArgs,  List<SpecialArg> specialArguments) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		this.retType = retType;
		this.isStatic = isStatic;
		this.methodArgs = methodArgs;
		this.specialArguments = specialArguments;
	}

	public LibraryMethodDecl(String packageName, String methodName, String className) {
		this(packageName, methodName, className, null, false, new LinkedList<String>(), new LinkedList<SpecialArg>());
	}
	
	public boolean isCriticalOutput() {
		for (SpecialArg arg : specialArguments) {
			if (arg.type == SpecialArg.TYPE_CRITICAL_OUTPUT) return true;
		}
		return false;
	}
	
	public String getMethSign() {
		String methSign = "";
		if (retType == null || retType.isEmpty() || retType.equals("void")) methSign+= "void ";
		else methSign += retType + " ";
		
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
