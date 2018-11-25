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
				eb.updateMinPC();
				break;
			}
			else
			{
				eb.removeTasks(taskGroup);
			}
			eb = eb.parentExecBlock;
		}
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention).append("return");
		if (returnVariable != null)
		{
			sb.append(" ").append(returnVariable);
		}
	}

}
