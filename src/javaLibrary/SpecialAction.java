package javaLibrary;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Parse.ParseData;
import execution.Task;
import main.LibraryMethodDecl;
import main.exception.SQLInjection;
import object.VariableExec;
import object.values.ArrayValue;
import object.values.ObjValue;
import symbol.Table;
import symbol.object.ArrayType;
import symbol.object.Class;
import symbol.object.Obj;
import symbol.object.PrimitiveType;
import symbol.object.TypeReference;
import symbol.object.UnknownType;

public class SpecialAction {
	
	public static enum AssignOperator
	{
		OP_SET_SAFE,
		OP_ASSIGN_NEW_OBJECT,
		OP_ASSIGN_EXISTING_OBJECT,
		OP_CRITICAL_OUTPUT
	}
	
	public static String ASSIGN_NEW_OBJECT(String type)
	{
		return ASSIGN_NEW_OBJECT_PREFIX + type.trim() + ASSIGN_NEW_OBJECT_SUFIX;
	}
	
	public static final String SET_SAFE = ":s=";
	private static final String ASSIGN_NEW_OBJECT_PREFIX = ":n(";
	private static final String ASSIGN_NEW_OBJECT_SUFIX = ")=";
	public static final String ASSIGN_EXISTING_OBJECT = ":=";
	
	public static final String THIS = "_this";
	public static final String RETURN = "_return";
	public static final String SAFE = "_safe";
	public static final String UNSAFE = "_unsafe";
	public static final String CRITICAL_OUTPUT = "__CO_";
	
	private String left = null;
	private String assign = null;
	private String expression = null;
	private LibraryMethodDecl methodDecl = null;
	
	private AssignOperator assignOp = null;
	
	private String objToCopyTypeFrom = null;
	private TypeReference newObjType = null;	
	private ActionTreeNode actionTree = null;
	private String right = null;
	
	public SpecialAction(String left, String assign, String expression, LibraryMethodDecl methodDecl) {
		this.left = left.trim();
		this.assign = assign.trim();
		this.expression = expression == null ? null : expression.trim();
		this.methodDecl = methodDecl;
	}
	
	public void bind()
	{
		bind_priv(this.left, this.assign, this.expression, this.methodDecl);
	}
	
	private void bind_priv(String left, String assign, String expression, LibraryMethodDecl methodDecl)
	{
		// Assert that special argument is applicable to its method.
		{
			if (left.startsWith(RETURN))
			{
				assert methodDecl.retTypeName != null && !methodDecl.retTypeName.equals("void");
			}
			
			if (left.startsWith(THIS))
			{
				assert !methodDecl.isStatic;
			}
			
			if (Character.isDigit(left.charAt(0)))
			{
				String fullLeft[] = left.split("\\.");
				int argIndex = Integer.parseInt(fullLeft[0]);
				assert argIndex < methodDecl.methodArgs.size();
			}
		}
		
		if (assign.equals(SET_SAFE)) // Calculate boolean expression and set safety value for left variable.
		{
			this.assignOp = AssignOperator.OP_SET_SAFE;
			this.actionTree = parseExpression(expression, methodDecl);
		}
		else if (assign.startsWith(ASSIGN_NEW_OBJECT_PREFIX)) // Calculate boolean expression to get safety value for NEW object
		{										              // and assign that object to left variable.
			assign = assign.substring(ASSIGN_NEW_OBJECT_PREFIX.length()).trim();
			assign = assign.substring(0, assign.length() - ASSIGN_NEW_OBJECT_SUFIX.length()).trim();
			if (assign.contains("|"))
			{
				this.objToCopyTypeFrom = assign.substring(0, assign.indexOf("|"));
				assign = assign.substring(assign.indexOf("|") + 1);
			}
			
			this.assignOp = AssignOperator.OP_ASSIGN_NEW_OBJECT;
			this.newObjType = getTypeForClassName(assign);
			this.actionTree = parseExpression(expression, methodDecl);
		}
		else if (assign.equals(ASSIGN_EXISTING_OBJECT)) // Assign existing object to left variable.
		{
			this.assignOp = AssignOperator.OP_ASSIGN_EXISTING_OBJECT;
			this.right = expression;
			
			if (this.right.contains(THIS))
			{
				assert !methodDecl.isStatic;
			}
			else if (Character.isDigit(this.right.charAt(0)))
			{
				String fullLeft[] = this.right.split("\\.");
				int argIndex = Integer.parseInt(fullLeft[0]);
				assert argIndex < methodDecl.methodArgs.size();
			}
			else
			{
				assert false;
			}
		}
		else if(assign.equals(CRITICAL_OUTPUT)) // Check the safety of the CRITICAL OUTPUT.
		{
			this.assignOp = AssignOperator.OP_CRITICAL_OUTPUT;
			assert !left.startsWith(RETURN);
		}
		else
		{
			assert false;
		}
	}
	
	public static TypeReference getTypeForClassName(String newObjTypeString)
	{
		String newObjPackageName = "";
		String newObjTypeName;
		
		String fullTypeName[] = newObjTypeString.split("\\.");
		newObjTypeName = fullTypeName[fullTypeName.length - 1];
		
		for (int i = 0; i < fullTypeName.length - 1; ++i)
		{
			if (newObjPackageName.isEmpty()) newObjPackageName = fullTypeName[i];
			else newObjPackageName += "." + fullTypeName[i];
		}
		
		if (newObjPackageName == null || newObjPackageName.isEmpty())
		{
			String typeName = ParseData.getTypeIfArray(newObjTypeName);
			Obj obj = Table.find(typeName);
			
			symbol.object.Type retType = null;
			if (obj != null || obj instanceof PrimitiveType || obj instanceof Class)
				retType = (symbol.object.Type)obj;
			else {
				retType = new UnknownType(typeName, null, null, Table.currentScope());
			}
			
			TypeReference typeRef = new TypeReference(retType);
			int arrayLevel = ParseData.getTypeArrayLevel(newObjTypeName);
			typeRef.addArrayLevel(arrayLevel);
			
			return typeRef;
		}
		else
		{
			symbol.object.Type retType = null;
			String typeName = ParseData.getTypeIfArray(newObjTypeName);
			
			List<String> returnTypeAbsolutePath = new LinkedList<>();
			returnTypeAbsolutePath.add(newObjPackageName);
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
			int arrayLevel = ParseData.getTypeArrayLevel(newObjTypeName);
			typeRef.addArrayLevel(arrayLevel);
			
			return typeRef;
		}
	}
	
	public String getLeft()
	{
		return this.left;
	}
	
	public boolean isCriticalOutput()
	{
		return assign.equals(CRITICAL_OUTPUT);
	}
	
	public void execute(VariableExec thisObj, List<VariableExec> actualArgs, VariableExec returnDest, Task task) throws SQLInjection
	{
		if (assignOp.equals(AssignOperator.OP_CRITICAL_OUTPUT))
		{
			if(!GetDataNode.getSafetyValue(this.left, thisObj, actualArgs, task))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("SQL injection detected!\r\n\r\n");
				sb.append("Critical method call stack:\r\n");
				task.printMethodCallStack(sb);
				List<String> fullArgName = GetDataNode.getFullName(this.left, thisObj, actualArgs);
				if (fullArgName.size() == 1 && GetDataNode.isNumeric(fullArgName.get(0)))
				{
					sb.append("\r\nCritical argument index: " + fullArgName.get(0));
				}
				else
				{
					sb.append("\r\nCritical argument name: " + Parse.ParseData.makeFullNameWithDots(fullArgName));
				}
				throw new main.exception.SQLInjection(sb.toString());
			}
			
			return;
		}
		
		List<String> fullLeftName = null;
		if (this.left.startsWith(RETURN))
		{
			if (returnDest.name != null)
			{
				fullLeftName = new LinkedList<String>();
				fullLeftName.addAll(returnDest.name);
				List<String> fields = Arrays.asList(this.left.replaceFirst(RETURN, "").split("\\."));
				for (String field : fields)
				{
					if (field.trim().isEmpty()) continue;
					else fullLeftName.add(field.trim());
				}
			}
			else
			{
				return;
			}
		}
		else
		{
			fullLeftName = GetDataNode.getFullName(this.left, thisObj, actualArgs);
			assert (!fullLeftName.isEmpty() && !GetDataNode.isNumeric(fullLeftName.get(0)));
		}
		
		if (assignOp.equals(AssignOperator.OP_ASSIGN_EXISTING_OBJECT))
		{	
			assert right != null;
			ObjValue rightObj = GetDataNode.getObject(this.right, thisObj, actualArgs, task);
			
			task.values.put(fullLeftName, rightObj);
			return;
		}
		
		boolean rightSafetyValue = actionTree.execute(thisObj, actualArgs, task);
		
		if (assignOp.equals(AssignOperator.OP_ASSIGN_NEW_OBJECT))
		{
			assert this.actionTree != null;
			
			if (this.objToCopyTypeFrom != null)
			{
				try
				{
					ObjValue obj = GetDataNode.getObject(objToCopyTypeFrom, thisObj, actualArgs, task);
					if (obj != null)
					{
						newObjType = new TypeReference(obj.getObjectType());
					}
				}
				catch (Exception e) {}
			}
			
			assert newObjType != null && newObjType.type != null && newObjType.type.isRefType() && !(newObjType.type instanceof UnknownType);

			if (this.newObjType.type instanceof ArrayType)
			{
				ObjValue defaultElem = ((symbol.object.Class)((symbol.object.ArrayType)this.newObjType.type).getType().type).getDefaultObject(rightSafetyValue);
				ArrayValue defaultArrayValue = new ArrayValue(rightSafetyValue, defaultElem, (symbol.object.ArrayType)this.newObjType.type);
				task.values.put(fullLeftName, defaultArrayValue);
			}
			else
			{
				task.values.put(fullLeftName, ((symbol.object.Class)this.newObjType.type).getDefaultObject(rightSafetyValue));
			}
			return;
		}
		
		assert assignOp.equals(AssignOperator.OP_SET_SAFE);
		ObjValue left = task.values.get(fullLeftName);
		assert left != null;
		left.setSafe(rightSafetyValue);
	}
	
	private ActionTreeNode parseExpression(String expression, LibraryMethodDecl methodDecl)
	{
		class ExprStackEntry
		{
			ActionTreeNode left = null;
			String operation = null;
		}
		
		Stack<ExprStackEntry> exprStack = new Stack<>();
		exprStack.push(new ExprStackEntry());
		String tokenizedExpr[] = expression.replaceAll("&", "~&~").replaceAll("\\|", "~|~").replaceAll("\\(", "~(~").replaceAll("\\)", "~)~").split("~");
		
		for(String token : tokenizedExpr)
		{
			token = token.trim();
			if (token.isEmpty()) continue;
			
			ExprStackEntry stackTop = exprStack.peek();
			if (token.equals(OperationNode.OR) || token.equals(OperationNode.AND))
			{
				assert stackTop.operation == null;
				stackTop.operation = token;
			}
			else if (token.equals("("))
			{
				exprStack.push(new ExprStackEntry());
			}
			else if (token.equals(")"))
			{
				ExprStackEntry res = exprStack.pop();
				assert res.operation == null;
				assert res.left != null;
				stackTop = exprStack.peek();
				
				if (stackTop.left == null)
				{
					assert stackTop.operation == null;
					stackTop.left = res.left;
				}
				else
				{
					assert stackTop.operation != null;
					ActionTreeNode right = res.left;
					OperationNode operation = new OperationNode(stackTop.operation, stackTop.left, right);
					stackTop.left = operation;
					stackTop.operation = null;
				}
			}
			else
			{
				// Assert that special argument is applicable to its method.
				{
					assert (!token.startsWith(RETURN));
					
					if (token.startsWith(THIS))
					{
						assert !methodDecl.isStatic;
					}
					
					if (Character.isDigit(token.charAt(0)))
					{
						String fullLeft[] = token.split("\\.");
						int argIndex = Integer.parseInt(fullLeft[0]);
						assert argIndex < methodDecl.methodArgs.size();
					}
				}
				
				
				if (stackTop.left == null)
				{
					assert stackTop.operation == null;
					stackTop.left = new GetDataNode(token);
				}
				else
				{
					assert stackTop.operation != null;
					GetDataNode right = new GetDataNode(token);
					OperationNode operation = new OperationNode(stackTop.operation, stackTop.left, right);
					stackTop.left = operation;
					stackTop.operation = null;
				}
			}	
		}
		
		assert exprStack.size() == 1;
		assert exprStack.peek().left != null;
		assert exprStack.peek().operation == null;
		
		return exprStack.peek().left;
	}
	
	@Override
	public String toString()
	{
		String res = left.trim() + " ";
		if (assign.equals(SpecialAction.CRITICAL_OUTPUT))
		{
			res += "is critical output";
		}
		else
		{
			res += assign + " " + expression;
		}
		return res;
	}
	
};
