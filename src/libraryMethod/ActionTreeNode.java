package libraryMethod;

import java.util.List;

import execution.Task;
import object.VariableExec;

public interface ActionTreeNode {
	
	public boolean execute(VariableExec thisObj, List<VariableExec> actualArgs,Task task);
	
}
