package object;

import java.util.Arrays;
import java.util.LinkedList;
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
		List<String> localVarToRemove = TaskExecutor.activeExecutionBlock.statements.getBlockLocalVariables();
		for (String var : localVarToRemove)
		{
			for(Task task : taskGroup)
			{
				task.values.remove(var);
			}
		}
		
		if (reduce)
		{
			List<byte[]> taskValuesHash = new LinkedList<>();
			for(Task task : taskGroup)
			{
				taskValuesHash.add(task.hash());
			}
			
			for (int i = 0; i < taskValuesHash.size(); ++i)
			{
				byte i_Hash[] = taskValuesHash.get(i);
				for (int j = i + 1; j < taskValuesHash.size(); ++j)
				{
					byte j_Hash[] = taskValuesHash.get(j);
					if (Arrays.equals(i_Hash, j_Hash))
					{
						TaskExecutor.activeExecutionBlock.taskTable.remove(taskGroup.get(j));
						taskGroup.remove(j);
						taskValuesHash.remove(j);
					}
				}
			}
		}
		
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
