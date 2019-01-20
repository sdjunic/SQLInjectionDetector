package libraryMethod;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import execution.Task;
import main.LibraryMethodDecl;
import main.exception.SQLInjection;
import object.VariableExec;
import object.values.ArrayValue;
import object.values.ObjValue;
import symbol.object.ArrayType;
import symbol.object.Method;
import symbol.object.TypeReference;
import symbol.object.UnknownType;

public class SpecialAction {
	
	public static final String THIS = "__this_";
	public static final String RETURN = "__return_";
	public static final String SAFE = "__safe_";
	public static final String UNSAFE = "__unsafe_";
	public static final String CRITICAL_OUTPUT = "__CO_";
	
	private String left;
	private TypeReference returnType;
	
	private boolean isCriticalOutput;
	private ActionTreeNode actionTree;
	private String right;
	
	public SpecialAction(String left, String expression, LibraryMethodDecl methodDecl) {
		left = left.trim();
		expression = expression.trim();
		
		this.left = left;
		
		// Assert that special argument is applicable to appropriate method
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
		
		if (expression.startsWith("=")) // calculate boolean expression to get safety value for NEW object
		{
			this.isCriticalOutput = false;
			this.actionTree = parseExpression(expression.substring(1), methodDecl);
			this.right = null;
		}
		else if (expression.startsWith(":=")) // assign EXISTING object
		{
			this.isCriticalOutput = false;
			this.actionTree = null;
			this.right = expression.substring(2).trim();
			
			if (this.right.contains(THIS))
			{
				assert !methodDecl.isStatic;
			}
			
			if (Character.isDigit(this.right.charAt(0)))
			{
				String fullLeft[] = this.right.split("\\.");
				int argIndex = Integer.parseInt(fullLeft[0]);
				assert argIndex < methodDecl.methodArgs.size();
			}
		}
		else if(expression.startsWith(CRITICAL_OUTPUT)) // check CRITICAL OUTPUT safety
		{
			assert !left.startsWith(RETURN);
			this.isCriticalOutput = true;
			this.actionTree = null;
			this.right = null;
		}
		else
		{
			assert false;
		}
	}
	
	public String getLeft()
	{
		return this.left;
	}
	
	public boolean isCriticalOutput()
	{
		return this.isCriticalOutput;
	}
	
	public void setReturnType(TypeReference returnType) {
		this.returnType = returnType;
	}

	public void execute(VariableExec thisObj, List<VariableExec> actualArgs, VariableExec returnDest, Task task) throws SQLInjection
	{
		if (isCriticalOutput)
		{
			if(!GetDataNode.getSafetyValue(this.left, thisObj, actualArgs, task))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("SQL injection detected!\r\n\r\nCritical method call stack:\r\n");
				Method.printMethodCallStack(sb);
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
		
		if (right != null)
		{	
			ObjValue rightObj = GetDataNode.getObject(this.right, thisObj, actualArgs, task);
			
			task.values.put(fullLeftName, rightObj);
			return;
		}
		
		assert this.actionTree != null;
		assert returnType != null && returnType.type != null && returnType.type.isRefType() && !(returnType.type instanceof UnknownType);
		
		boolean rightSafetyValue = actionTree.execute(thisObj, actualArgs, task);
		if (this.returnType.type instanceof ArrayType)
		{
			ObjValue defaultElem = ((symbol.object.Class)((symbol.object.ArrayType)this.returnType.type).getType().type).getDefaultObject(rightSafetyValue);
			ArrayValue defaultArrayValue = new ArrayValue(rightSafetyValue, defaultElem);
			task.values.put(fullLeftName, defaultArrayValue);
		}
		else
		{
			task.values.put(fullLeftName, ((symbol.object.Class)this.returnType.type).getDefaultObject(rightSafetyValue));
		}
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
				// Assert that special argument is applicable to appropriate method
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
	
};
