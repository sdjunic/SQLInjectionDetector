package libraryMethod;

import java.util.List;

import execution.Task;
import object.VariableExec;

public class OperationNode implements ActionTreeNode {

	public static final String OR = "|";
	public static final String AND = "&";
	
	private String operation;
	private ActionTreeNode left, right;
	
	public OperationNode(String operation, ActionTreeNode left, ActionTreeNode right) {
		assert (operation.equals(OR) || operation.equals(AND));
		this.operation = operation;
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean execute(VariableExec thisObj, List<VariableExec> actualArgs, Task task) {
		if (operation.equals(OR))
		{
			return left.execute(thisObj, actualArgs, task) || right.execute(thisObj, actualArgs, task);
		}
		else
		{
			return left.execute(thisObj, actualArgs, task) && right.execute(thisObj, actualArgs, task);
		}
	}

}
