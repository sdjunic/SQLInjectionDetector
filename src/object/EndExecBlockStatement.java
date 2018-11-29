package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import object.values.MethodValuesHolder;

public class EndExecBlockStatement extends Statement {

	private boolean reduce;
	
	public EndExecBlockStatement(boolean reduce) {
		this.reduce = reduce;
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		//TODO: actual reduce of taskGroup
		
		ExecutionBlock eb = TaskExecutor.activeExecutionBlock;
		assert (taskGroup.size() == eb.taskTable.size());
		
		ExecutionBlock parentEb = TaskExecutor.activeExecutionBlock.parentExecBlock;
		
		if (parentEb != null)
		{
			parentEb.taskTable.addAll(taskGroup);
		}
		
		for (Task t : taskGroup)
		{
			if (eb.isMethodBody)
			{
				t.values = t.values.getParentValuesHolder();
			}
			t.PC = eb.getParentEB_PC();
		}
		
		eb.taskTable.clear();
		eb.complete();
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention).append("--end exec block--\r\n");
	}

}
