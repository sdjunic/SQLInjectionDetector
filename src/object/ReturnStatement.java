package object;

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
		while(eb != null)
		{
			if (eb.isMethodBody)
			{
				for (Task t : taskGroup)
				{
					if (returnVariable != null && eb.returnDestination != null)
					{
						MethodValuesHolder parentValues = t.values.getParentValuesHolder();
						ObjValue returnVal;
						if (returnVariable.value == null)
						{
							returnVal = t.values.get(returnVariable.name);
						}
						else
						{
							returnVal = returnVariable.value;
						}
						parentValues.put(eb.returnDestination.name, returnVal);
					}
					t.PC = eb.statements.getStmtCount() - 1; // jump to ReduceStatement
				}
				if (eb != TaskExecutor.activeExecutionBlock)
				{
					assert !firstIteration;
					eb.taskTable.addAll(taskGroup);
				}
				break;
			}
			else
			{
				// Tasks should be already deleted in all EB, except in active one
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
