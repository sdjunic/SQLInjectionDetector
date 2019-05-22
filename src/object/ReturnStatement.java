package object;

import java.util.LinkedList;
import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import object.values.MethodValuesHolder;
import object.values.ObjValue;

public class ReturnStatement extends Statement {

	VariableExec returnVariable;
	
	public ReturnStatement(VariableExec returnVariable) {
		this.returnVariable = returnVariable;
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		ExecutionBlock eb = TaskExecutor.activeExecutionBlock;
		boolean firstIteration = true;
		
		// Get values to return.
		List<ObjValue> returnValues = new LinkedList<ObjValue>();
		if (returnVariable != null)
		{
			for (Task t : taskGroup)
			{
				if (returnVariable.value == null)
				{
					returnValues.add(t.values.get(returnVariable.name));
				}
				else
				{
					returnValues.add(returnVariable.value);
				}
			}
		}
		
		while(eb != null)
		{
			if (eb.isMethodBody)
			{
				// Return values to parent VH map.
				int i = 0;
				for (Task t : taskGroup)
				{
					if (returnVariable != null && eb.returnDestination != null)
					{
						MethodValuesHolder parentValues = t.values.getParentValuesHolder();
						ObjValue returnVal = returnValues.get(i++);
						parentValues.put(eb.returnDestination.name, returnVal);
					}
					t.PC = eb.statements.getStmtCount() - 1; // jump to ReduceStatement
				}
				
				// Add tasks to appropriate EB which represents method body.
				if (eb != TaskExecutor.activeExecutionBlock)
				{
					assert !firstIteration;
					eb.taskTable.addAll(taskGroup);
				}
				break;
			}
			else
			{
				// Delete local variables.
				List<String> localVarToRemove = eb.statements.getBlockLocalVariables();
				for(Task task : taskGroup)
				{
					for (String var : localVarToRemove)
					{
						task.values.remove(var);
					}
				}
				
				// Tasks should be already deleted in all EB, except in active one.
				if (firstIteration)
				{
					assert eb.taskTable.containsAll(taskGroup);
					eb.taskTable.removeAll(taskGroup);
				}
				else
				{
					for (Task t : taskGroup)
					{
						assert !eb.taskTable.contains(t);
					}
				}
			}
			eb = eb.parentExecBlock;
			firstIteration = false;
		}
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention).append("return");
		if (returnVariable != null)
		{
			sb.append(" ").append(returnVariable);
		}
		sb.append("\r\n");
	}

}
