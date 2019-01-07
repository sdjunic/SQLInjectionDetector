package libraryMethod;

import java.util.List;
import java.util.Stack;

import execution.Task;
import main.LibraryMethodDecl;
import main.exception.SQLInjection;
import object.VariableExec;
import object.values.ObjValue;
import object.values.StringVal;
import symbol.object.Method;

public class SpecialAction {
	
	public static final String THIS = "__this_";
	public static final String RETURN = "__return_";
	public static final String SAFE = "__safe_";
	public static final String UNSAFE = "__unsafe_";
	public static final String CRITICAL_OUTPUT = "__CO_";
	
	private String left;
	
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
				assert methodDecl.retType != null;
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
			// TODO: assert that we have "default class object" for left type
			this.isCriticalOutput = false;
			this.actionTree = parseExpression(expression.substring(1), methodDecl);
			this.right = null;
		}
		else if (expression.startsWith(":=")) // assign EXISTING object
		{
			this.isCriticalOutput = false;
			this.actionTree = null;
			this.right = expression.substring(2);
			
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
				fullLeftName = returnDest.name;
			}
			else
			{
				return;
			}
		}
		else
		{
			fullLeftName = GetDataNode.getFullName(this.left, thisObj, actualArgs);
			assert (!fullLeftName.isEmpty() && !Character.isDigit(fullLeftName.get(0).charAt(0)));
		}
		
		if (right != null)
		{	
			ObjValue rightObj = GetDataNode.getObject(this.right, thisObj, actualArgs, task);
			
			task.values.put(fullLeftName, rightObj);
			return;
		}
		
		assert this.actionTree != null;
		boolean rightSafetyValue = actionTree.execute(thisObj, actualArgs, task);		
		task.values.put(fullLeftName, StringVal.getString(rightSafetyValue));
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
