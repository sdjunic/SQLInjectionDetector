package symbol.object;

import java.io.*;
import java.util.*;

import Parse.ExpressionParser;
import object.AssignmentStatement;
import object.ConstructorCallStatement;
import object.MethCallStatement;
import object.EndExecBlockStatement;
import object.ReturnStatement;
import object.VariableExec;
import symbol.Scope;
import symbol.SymbolDataStructure;
import symbol.Table;
import symbol.object.Class;
import object.values.ClassValue;
import object.values.NullValue;
import object.values.ObjValue;
import object.values.StringVal;

public class Class implements Type {

	private static int anonymousClassCount = 0;
	private String name;
	private Scope myScope = null;
	private Modifiers modifiers = null;
	
	private List<List<String>> importedObjects, importedScopes;
	
	private Method fieldsInitializerMethod = null;
	private boolean fieldInitializersParsed = false;
	
	private TypeReference superClass = null;
	
	private static List<Class> allClasses = new LinkedList<Class>();
	
	public Class(String name, TypeReference superClass){
		this.name = name;
		this.superClass = superClass;
		allClasses.add(this);
	}
	
	@Override
	public String getName() {
		return name;
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
	
	public TypeReference getSuperClass() {
		return superClass;
	}

	public void setSuperClass(TypeReference superClass) {
		this.superClass = superClass;
	}

	public Modifiers getModifiers() {
		return modifiers;
	}

	public void setModifiers(Modifiers modifiers) {
		this.modifiers = modifiers;
	}
	
	@Override
	public boolean isValueType() {
		return false;
	}

	@Override
	public boolean isRefType() {
		return true;
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

	public boolean containsField(String fieldName) { // Don't count inherited fields
		if (getLocals() == null || getLocals().symbols() == null) return false;
		for (Obj o : getLocals().symbols()) {
			if (o instanceof Field) {
				if (o.getName().equals(fieldName)) return true;
			}
		}
		return false;
	}
	
	public void extendClass() {
		if (superClass != null && superClass.type != null && superClass.type instanceof Class) {
			Field field = new Field("super", superClass, new Modifiers());
			field.setScope(this.myScope);
			this.getLocals().insertKey(field);
		}
	}
	
	public static void extendAllClasses() {
		Iterator<Class> it = allClasses.iterator();
		while (it.hasNext()) {
			it.next().extendClass();
		}
	}
	
	public String getAnonymousClassName() {
		return ++anonymousClassCount + "_" + name;
	}
	
	public String getFieldInitializerMethodName()
	{
		return "_field_init_" + name;
	}
	
	// Used in special methods to make new object of this Class.
	// That object will have all fields set to null, except
	// String fields, which will have given safety value.
	//
	public ObjValue getDefaultObject(boolean isSafe)
	{
		ClassValue result = new ClassValue(this, isSafe);
		
		for (Obj o : getLocals().symbols()) { 
			if (o instanceof Field) { 
				Field field = (Field)o;
				if(field.getType().isRefType())
				{
					// TODO: check are all string fields stored like this
					if (field.getType().type.getName() == "String")
					{
						result.setField(field.getName(), StringVal.getString(isSafe));
					}
					else
					{
						result.setField(field.getName(), NullValue.getNullValue());
					}
				}
			}
		}
		
		return result;
	}
	
	public Method getFieldsInitializerMethod() throws Exception {
		if (this.fieldInitializersParsed == false) {
			fieldInitializersParsed = true;
			if (getLocals() != null && getLocals().symbols() != null) {
				LinkedList<Field> fields = new LinkedList<Field>();
				for (Obj o : getLocals().symbols()) { 
					if (o instanceof Field) { 
						Field field = (Field)o;
						if(field.getType().isRefType() && !field.getName().equals("super"))
						{
							fields.add(field);
						}
					}
				}
				if (!fields.isEmpty()) {
					Method m = new Method(getFieldInitializerMethodName(), true);
					m.setParsed(true);
					this.fieldsInitializerMethod = m;
					m.complFormalParamAdding();
					
					Scope currentScope = Table.currentScope();
					
					Table.setCurrentScope(this.myScope);
					m.setImportedObjects(importedObjects);
					m.setImportedScopes(importedScopes);
					Table.openScope(m);
					m.setScope(Table.currentScope());
					
					this.myScope.addToLocals(m);
					Table.closeScope();
					
					for (Field field : fields) {
						if (field.getInitExpression() == null)
						{
							List<String> varName = new LinkedList<String>();
							varName.add("this"); varName.add(field.getName());
							m.addStatement(new AssignmentStatement(new VariableExec(varName, field), new VariableExec(null)));
							continue;
						}
						
						Reader fr = new StringReader(field.getInitExpression());
						lex.Lexer l = new lex.Lexer(fr, 5);
	
						ExpressionParser g = new ExpressionParser(l);
						g.setErrorPS(System.err);
						//g.setInfoPS(null);
						
						g.setParsingTopMethod(m);
						
						g.parse();
						if (g.expressionResult != null) {
							List<String> varName = new LinkedList<String>();
							varName.add("this"); varName.add(field.getName());
							m.addStatement(new AssignmentStatement(new VariableExec(varName, field), g.expressionResult));
						}
						fr.close();
					}
					m.addStatement(new EndExecBlockStatement(true /* reduce */));
					
					Table.setCurrentScope(currentScope);
				}
			}
		}
		return this.fieldsInitializerMethod;
	}
	
	
	public Method findMethod(String name, List<VariableExec> args) {
		Class cl = this;
		while (cl != null) {
			Collection<Obj> classContent = cl.getLocals().symbols();
			Iterator<Obj> it = classContent.iterator();
			List<Candidate> candidates = new LinkedList<>();
			while (it.hasNext()) {
				Obj o = it.next();
				if (o instanceof Method && o.getName().startsWith(name+"(")) {
					Method meth = (Method)o;
					List<MethParam> params = meth.getMethParamList();
					if (params.size() == args.size()) {
						candidates.add(new Candidate(meth));
					}
				}
			}
			
			if (!candidates.isEmpty())
			{
				return this.findBestMatchingCandidate(candidates, args);
			}
			
			if (cl.getSuperClass() != null && cl.getSuperClass().type != null && cl.getSuperClass().type instanceof Class) {
				cl = (Class)cl.getSuperClass().type;
			} else { 
				return null;
			}
		}
		return null;
	}
	
	public Method findConstructor(List<VariableExec> args) throws Exception {		
		Collection<Obj> classContent = this.getLocals().symbols();
		Iterator<Obj> it = classContent.iterator();
		List<Candidate> candidates = new LinkedList<>();
		while (it.hasNext()) {
			Obj o = it.next();
			if (o instanceof Method && o.getName().startsWith(name+"(")) {
				Method meth = (Method)o;
				List<MethParam> params = meth.getMethParamList();
				if (params.size() == args.size()) {
					candidates.add(new Candidate(meth));
				}
			}
		}
		
		if (!candidates.isEmpty())
		{
			return this.findBestMatchingCandidate(candidates, args);
		}
		
		if (args.size() == 0) { // add default constructor
			Method defCon = new Method(name, true);
			defCon.complFormalParamAdding();
			defCon.setParsed(true);
			
			Scope currentScope = Table.currentScope();
			
			Table.setCurrentScope(this.myScope);
			defCon.setImportedObjects(importedObjects);
			defCon.setImportedScopes(importedScopes);
			Table.openScope(defCon);
			defCon.setScope(Table.currentScope());
			
			this.myScope.addToLocals(defCon);
			Table.setCurrentScope(currentScope);
			
			if (!defCon.isContainsExplThisConstructorCall()) {				
				Method fieldInitMethod = defCon.getParentClass().getFieldsInitializerMethod();
				if (fieldInitMethod != null)
				{
					List<VariableExec> args1 = new LinkedList<VariableExec>();
					MethCallStatement fieldInitMethCall = new MethCallStatement(
							null, 
							//defCon.getParentClass().getFieldInitializerMethodName(), 
							fieldInitMethod,
							new VariableExec("this", defCon.getParentClass()), 
							args);
					defCon.getBody().addStatement(fieldInitMethCall, 0);
				}
				
				if (!defCon.isContainsExplSuperConstructorCall()) {
					if (defCon.getParentClass().getSuperClass() != null && defCon.getParentClass().getSuperClass().type != null) {
						Class superClass = (Class)defCon.getParentClass().getSuperClass().type;
						List<VariableExec> args1 = new LinkedList<VariableExec>();
						Field superField = defCon.getParentClass().findField("super");
						List<String> name = new LinkedList<String>();
						name.add("this"); name.add("super");
						ConstructorCallStatement superConstrCall = new ConstructorCallStatement(new VariableExec(name, superField), superClass.findConstructor(args), args);
						defCon.getBody().addStatement(superConstrCall, 0);
					}
				}
			}
			else
			{
				throw new Exception("Calling this() in constructor isn't supported!");
			}
			
			defCon.getBody().addStatement(new ReturnStatement(new VariableExec("this", defCon.getParentClass())));
			defCon.getBody().addStatement(new EndExecBlockStatement(false /*reduce*/));
			return defCon;
			
//			if (fieldInitMethod != null)
//			{
//				List<VariableExec> emptyArgs = new LinkedList<VariableExec>();
//				MethCallStatement fieldInitMethCall = new MethCallStatement(null, fieldInitMethod, emptyArgs);
//				defCon.addStatement(fieldInitMethCall);
//			}
//			
//			if (this.superClass != null && this.superClass.type != null) {
//				Class superClass = (Class)this.superClass.type;
//				VariableExec superField = new VariableExec("super", this.findField("super"));
//				defCon.addStatement(new ConstructorCallStatement(superField, superClass.findConstructor(args), args));
//			}
//			
//			defCon.getBody().addStatement(new ReturnStatement(new VariableExec("this", this)));
//			defCon.getBody().addStatement(new EndExecBlockStatement(true /* reduce */));
//			return defCon;
		}
		
		return null;
	}
	
	class Candidate
	{
		public Method method;
		public int rate;
		
		public Candidate(Method m)
		{
			this.method = m;
			this.rate = 0;
		}
	}
	
	public Method findBestMatchingCandidate(List<Candidate> candidates, List<VariableExec> args)
	{
		assert candidates != null && !candidates.isEmpty();
		
		for (Candidate candidate : candidates)
		{
			List<MethParam> params = candidate.method.getMethParamList();
			
			for (int i = 0; i < params.size(); ++i)
			{
				MethParam param = params.get(i);
				VariableExec arg = args.get(i);
				
				if (arg == null)
				{
					continue;
				}
				
				Type argType = null;
				if (arg.object != null)
				{
					if (arg.object instanceof Type)
					{
						argType = ((Type)arg.object);
					}
					else if (arg.object instanceof Field)
					{
						argType = ((Field)arg.object).getType().type;
					}
					else if (arg.object instanceof Variable)
					{
						argType = ((Variable)arg.object).getType().type;
					}
				} else if (arg.value != null)
				{
					argType = arg.value.getObjectType();
				}
				
				if (param.getType().getName().equals(argType.getName()))
				{
					candidate.rate += 5;
				}
				else
				{
					// TODO: add logic derived type match
					Type paramSourceType = param.getType().type;
					int paramArrayLevel = 0;
					if (paramSourceType instanceof ArrayType)
					{
						paramSourceType = ((ArrayType)paramSourceType).getType().type;
						paramArrayLevel = ((ArrayType)paramSourceType).getArrayLevel();
					}
					
					Type argSourceType = argType;
					int argArrayLevel = 0;
					if (argSourceType instanceof ArrayType)
					{
						argSourceType = ((ArrayType)argSourceType).getType().type;
						argArrayLevel = ((ArrayType)argSourceType).getArrayLevel();
					}
					
					if (paramArrayLevel != argArrayLevel) continue;
					
					Type argSuperClass = argSourceType;
					while(true)
					{
						if (argSuperClass instanceof Class && ((Class)argSuperClass).superClass != null)
						{
							argSuperClass = ((Class)argSourceType).superClass.type;
						}
						else
						{
							break;
						}
						
						if (argSuperClass.getName().equals(paramSourceType.getName()))
						{
							candidate.rate += 3;
							break;
						}
					}
				}
			}
		}
		
		Method bestCandidate = null;
		int bestRate = -1;
		for (Candidate candidate : candidates)
		{
			if (candidate.rate > bestRate)
			{
				bestRate = candidate.rate;
				bestCandidate = candidate.method;
			}
		}		
		return bestCandidate;
	}
	
	public Field findField(String name) {
		Class cl = this;
		while (cl != null) {
			Collection<Obj> classContent = cl.getLocals().symbols();
			Iterator<Obj> it = classContent.iterator();
			while (it.hasNext()) {
				Obj o = it.next();
				if (o instanceof Field && o.getName().equals(name)) {
					return (Field)o;
				}
			}
			if (cl.getSuperClass() != null && cl.getSuperClass().type != null && cl.getSuperClass().type instanceof Class) {
				cl = (Class)cl.getSuperClass().type;
			} else { 
				return null;
			}
		}
		return null;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public void print(StringBuilder sb, int tabNum) {
		String tab = "";
		for(int i=0; i<tabNum; ++i) tab+="\t";
		sb.append(tab).append("class: ").append(modifiers!=null ? modifiers.toString() + " " : "").append(name)
		.append(superClass!=null ? " extends "+superClass.type : "").append("\r\n");
		if(getLocals() != null) getLocals().print(sb, tabNum+1);
		sb.append("\r\n");
	}

}
