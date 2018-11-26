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
		assert eb.isMethodBody;
		
		for (Task t : taskGroup)
		{
			MethodValuesHolder parentValues = t.values.getParentValuesHolder();
			t.values = parentValues;
			t.PC = eb.getParentEB_PC();
		}
		
		eb.taskTable.clear();
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention).append("--end exec block--\r\n");
	}

}
