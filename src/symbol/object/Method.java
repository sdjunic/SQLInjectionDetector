package symbol.object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import Parse.MethodParser;
import execution.Task;
import main.Main;
import main.SpecialArg;
import main.exception.SQLInjection;
import object.*;
import object.values.*;
import symbol.Scope;
import symbol.SymbolDataStructure;
import symbol.Table;
import symbol.object.Modifiers.Modifier;

public class Method implements Obj {

	private String name = null;
	private Modifiers modifiers = null;
	private TypeReference retType = null;
	private Scope myScope = null;
	
	private boolean constructor = false;
	private boolean containsExplThisConstructorCall = false;
	private boolean containsExplSuperConstructorCall = false;
	
	private String methodDefFilePath = null;
	
	private int bracksAfterParamsNum = 0;

	private List<MethParam> formalParams = new LinkedList<MethParam>();
	private int formalParamsNum = 0;
	
	private List<List<String>> importedObjects = null;
	private List<List<String>> importedScopes = null;

	private boolean isDefined = true;
	private List<SpecialArg> specialArguments = null; //arguments starts from 0, 'this' object is -1, return value is -2

	private StatementsBlock body = new StatementsBlock();
	private StatementsBlock currentBlock = null;
	
	private boolean parsed = false;	
	
	public static Stack<Method> methCallStack = null;
	
	public boolean isParsed() {
		return parsed;
	}

	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}

	public Method(String name){
		this(name, false);
	}
	
	public Method(String name, boolean constructor){
		this.name = name + "(";
		this.modifiers = null;
		this.retType = null;
		this.constructor = constructor;
		this.bracksAfterParamsNum = 0;
		this.formalParamsNum = 0;
		this.currentBlock = this.body;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public boolean isContainsExplSuperConstructorCall() {
		return containsExplSuperConstructorCall;
	}

	public void setContainsExplSuperConstructorCall(boolean containsExplConstructorCall) {
		this.containsExplSuperConstructorCall = containsExplConstructorCall;
	}
	
	public boolean isContainsExplThisConstructorCall() {
		return containsExplThisConstructorCall;
	}

	public void setContainsExplThisConstructorCall(boolean containsExplConstructorCall) {
		this.containsExplThisConstructorCall = containsExplConstructorCall;
	}

	public void incBracksAfterParamsNum(){
		bracksAfterParamsNum++;
	}
	
	public Modifiers getModifiers() {
		return modifiers;
	}

	public void setModifiers(Modifiers modifiers) {
		this.modifiers = modifiers;
	}

	public TypeReference getRetType() {
		return retType;
	}

	public void setRetType(TypeReference retType) {
		if (bracksAfterParamsNum > 0 && retType != null) {
			if (retType.type instanceof ArrayType) {
				((ArrayType)retType.type).setArrayLevel(((ArrayType)retType.type).getArrayLevel()+bracksAfterParamsNum);
			} else {
				retType.type = new ArrayType(retType, bracksAfterParamsNum); 
			}
		}
		this.retType = retType;
		bracksAfterParamsNum = 0;
	}

	public void addFormalParam(MethParam mp){
		if (mp == null) return;
		formalParams.add(mp);
		name += (formalParamsNum!=0 ? "," : "") + (mp.getType() == null ? "_unknown" : mp.getType().toString());
		mp.setOrdinal(formalParamsNum++);
	}
	
	public void complFormalParamAdding(){
		name += ")";
	}
	
	public List<MethParam> getMethParamList(){
		return this.formalParams;
	}

	@Override
	public SymbolDataStructure getLocals() {
		if (this.myScope != null) return this.myScope.getLocals();
		else return null;
	}
	
	@Override
	public void setScope(Scope scope){
		this.myScope = scope;
	}
	
	@Override
	public Scope getScope(){
		return this.myScope;
	}
	
	public String getMethodDefFilePath() {
		return methodDefFilePath;
	}

	public void setMethodDefFilePath(String methodDefFilePath) {
		this.methodDefFilePath = methodDefFilePath;
	}
	
	public boolean isConstructor() {
		return constructor;
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

	public boolean isStatic() {
		if (modifiers == null) return false;
		return modifiers.haveModifier(Modifier.STATIC);
	}
	
	public Class getParentClass() {
		return (Class)myScope.getOuter().getParrentObj();
	}
	
	public boolean isDefined() {
		return isDefined;
	}

	public void setDefined(boolean isDefined) {
		this.isDefined = isDefined;
	}

	public List<SpecialArg> getSpecialArguments() {
		return specialArguments;
	}

	public void setSpecialArguments(List<SpecialArg> specialArguments) {
		this.specialArguments = specialArguments;
	}

	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append(isConstructor() ? "constr: " : "method: ").append(modifiers != null ? modifiers + " " : "");
		if (!isConstructor()) sb.append(retType==null?"void":retType).append(" ");
		sb.append(name);
		if (getLocals() != null && getLocals().numSymbols() > 0) sb.append("\r\n");
		if(getLocals() != null) getLocals().print(sb, tabNum+1);
	}
	
	public StatementsBlock getBody() {
		return body;
	}

	public StatementsBlock getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(StatementsBlock currentBlock) {
		this.currentBlock = currentBlock;
	}

	public void addStatement(Statement stmt) {
		this.currentBlock.addStatement(stmt);
	}

	public void addStatement(Statement stmt, boolean isLocalVariableDeclaration) {
		this.currentBlock.addStatement(stmt, isLocalVariableDeclaration);
	}
	
	public void executableBlockEnd() {
		this.currentBlock = this.currentBlock.getParentStatementsBlock();
	}
	
//	public Type getReturnVariableType() {
//		VariableExec v = this.body.getReturnVar();
//		if (v != null && v.object != null) {
//			if (v.object instanceof Class) return (Class)v.object;
//			if (v.object instanceof symbol.object.Variable) {
//				symbol.object.Variable var = (symbol.object.Variable)v.object;
//				if (var.getType() != null) return var.getType().type;
//			}
//			if (v.object instanceof Field) {
//				Field var = (Field)v.object;
//				if (var.getType() != null) return var.getType().type;
//			}
//			if (v.object instanceof MethParam) {
//				MethParam var = (MethParam)v.object;
//				if (var.getType() != null) return var.getType().type;
//			}
//		}
//		return null;
//	}
	
//	public ObjValue executeMethod(MethodValuesHolder values) throws Exception {
//		if (Main.infoPS != null) Main.infoPS.println("Exec "+ this.getName() +" method!");
//		if (isDefined) {
//			if (!parsed) {
//				if (this.constructor) {
//					throw new Exception("Constructors need to be parsed in ConstructorCallStatement!");
//				}
//				this.parseMethod();
//			}
//			return null; //body.execute(values);
//		} else {
//			ObjValue returnValue = null;
//			
//			for (SpecialArg specArg : specialArguments) {
//				if (specArg.type == SpecialArg.TYPE_CRITICAL_OUTPUT) {
//					String argumentName;
//					if (specArg.index == SpecialArg.INDEX_RETURN_OBJ) continue;
//					else if (specArg.index == -1) { argumentName = "this"; }
//					else { argumentName = this.getMethParamList().get(specArg.index).getName(); }
//					ObjValue obj = values.get(argumentName);
//					if (obj != null && !obj.isSafe()) {
//						StringBuilder sb = new StringBuilder();
//						sb.append("SQL injection detected!\r\n\r\nCritical method call stack:\r\n");
//						printMethodCallStack(sb);
//						throw new main.exception.SQLInjection(sb.toString());
//					}
//				} else {
//					String argumentName;
//					if (specArg.index == -2) {
//						returnValue = new StringVal(specArg.type == SpecialArg.TYPE_SAFE_ARG);
//						continue;
//					}
//					else if (specArg.index == -1) { argumentName = "this"; }
//					else { argumentName = this.getMethParamList().get(specArg.index).getName(); }
//					ObjValue obj = values.get(argumentName);
//					obj.setSafe(specArg.type == SpecialArg.TYPE_SAFE_ARG);
//				}
//			}
//			
//			return returnValue;
//		}
//	}
	
	public void parseMethod() throws Exception {
		assert this.isDefined;
		if (!this.parsed)
		{
			this.parsed = true;			
			
			if (Main.infoPS != null) Main.infoPS.println("Parsing "+this.name+" method!");
			
			Reader fr = new BufferedReader(new FileReader(this.getMethodDefFilePath()));
			lex.Lexer l = new lex.Lexer(fr, 5);
					
			MethodParser g = new MethodParser(l);
			//g.setErrorPS(System.err);
			//g.setInfoPS(System.out);
			g.setParsingTopMethod(this);
			
			Scope currentScope = Table.currentScope();
			g.parse();
			Table.setScope(currentScope);
			
			fr.close();
			
			if (!this.constructor)
			{
				this.body.addStatement(new EndExecBlockStatement(true /* reduce */));
			}
		}
	}
	
	public void executeSpecialMethod(VariableExec thisObj, List<VariableExec> actualArgs, VariableExec returnDest, List<Task> tasks) throws SQLInjection
	{
		assert !this.isDefined;
		assert specialArguments.size() >= actualArgs.size();
		for (Task t : tasks)
		{
			ValuesHolder values = t.values;
			
			for (SpecialArg specArg : specialArguments) {
				if (specArg.type == SpecialArg.TYPE_CRITICAL_OUTPUT) {
					VariableExec actualArg = null;
					assert (specArg.index != SpecialArg.INDEX_RETURN_OBJ);
					if (specArg.index == -1) { actualArg = thisObj; }
					else { actualArg = actualArgs.get(specArg.index); }
					ObjValue obj;
					if (actualArg.value != null)
					{
						obj = actualArg.value;
					}
					else
					{
						obj = values.get(actualArg.name);
					}
					if (obj != null && !obj.isSafe()) {
						StringBuilder sb = new StringBuilder();
						sb.append("SQL injection detected!\r\n\r\nCritical method call stack:\r\n");
						printMethodCallStack(sb);
						throw new main.exception.SQLInjection(sb.toString());
					}
				} else {
					VariableExec actualArg = null;
					if (specArg.index == -2 && returnDest != null) {
						assert (returnDest.name != null);
						values.put(returnDest.name, new StringVal(specArg.type == SpecialArg.TYPE_SAFE_ARG));
						continue;
					}
					else if (specArg.index == -1) { actualArg = thisObj; }
					else { actualArg = actualArgs.get(specArg.index); }
					ObjValue obj;
					if (actualArg.value != null)
					{
						obj = actualArg.value;
					}
					else
					{
						obj = values.get(actualArg.name);
					}
					obj.setSafe(specArg.type == SpecialArg.TYPE_SAFE_ARG);
				}
			}
		}
	}
	
	public boolean isMethodAlreadyOnStack() {
		Enumeration<Method> e = methCallStack.elements();
		while (e.hasMoreElements()) {
			Method i = e.nextElement();
			if (this.equals(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static void printMethodCallStack(StringBuilder sb) {
		for (int i = methCallStack.size()-1; i>=0; --i) {
			Method meth = methCallStack.get(i);
			sb.append("- ");
			meth.printCallSignOnly(sb);
		}
	}

	public void printCallSignOnly(StringBuilder sb) {
		sb.append(getName()+"\r\n");
	}

}