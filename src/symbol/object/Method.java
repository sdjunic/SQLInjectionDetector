package symbol.object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import Parse.MethodParser;
import execution.Task;
import javaLibrary.SpecialAction;
import main.Main;
import main.exception.SQLInjection;
import object.*;
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
	private List<SpecialAction> specialActions = null;
	
	private StatementsBlock body = new StatementsBlock();
	private StatementsBlock currentBlock = null;
	
	private boolean parsed = false;	
	
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

	public List<SpecialAction> getSpecialActions() {
		return specialActions;
	}

	public void setSpecialActions(List<SpecialAction> specialActions) {
		for (SpecialAction specAction : specialActions)
		{
			specAction.bind();
		}
		this.specialActions = specialActions;
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

	public Statement getLastStatement()
	{
		return this.currentBlock.getLastStatement();
	}
	
	public void addStatement(Statement stmt) {
		this.currentBlock.addStatement(stmt);
	}
	
	public void addAllStatements(StatementsBlock stmtBlock) {
		this.currentBlock.addAllStatementsFromBlock(stmtBlock);
	}

	public void addStatement(Statement stmt, boolean isLocalVariableDeclaration) {
		this.currentBlock.addStatement(stmt, isLocalVariableDeclaration);
	}
	
	public void currentBlockEnd() {
		this.currentBlock = this.currentBlock.getParentStatementsBlock();
	}
	
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
		assert !specialActions.isEmpty();
		for (Task t : tasks)
		{			
			for (SpecialAction specAct : specialActions) {
				specAct.execute(thisObj, actualArgs, returnDest, t);
			}
		}
	}

	public void printCallSignOnly(StringBuilder sb) {
		sb.append(getName()+"\r\n");
	}

}