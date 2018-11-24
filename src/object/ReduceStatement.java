package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import object.values.MethodValuesHolder;

public class ReduceStatement extends Statement {

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		//TODO: actual reduce of taskGroup
		
		ExecutionBlock eb = TaskExecutor.activeExecutionBlock;
		assert eb.isMethodBody;
		
		for (Task t : taskGroup)
		{
			if (t.returned)
			{
				t.returned = false;
			}
			MethodValuesHolder parentValues = t.values.getParentValuesHolder();
			t.values = parentValues;
			t.PC = eb.parentExecBlock.getNextPC();
		}
		
		eb.taskTable.clear();
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention).append("--reduce--");
	}

}
