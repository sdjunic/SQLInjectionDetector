package object;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

public class LoopRepeatStatement extends Statement {

	public LoopRepeatStatement() {}

	@Override
	public void execute(List<Task> currentTasks) throws Exception {
		ExecutionBlock loopExecBlock = TaskExecutor.activeExecutionBlock;
		assert loopExecBlock.isLoopExecBlock;
		
		// Delete local variables for current tasks
		List<String> localVarToRemove = TaskExecutor.activeExecutionBlock.statements.getBlockLocalVariables();
		for (String var : localVarToRemove)
		{
			for(Task task : currentTasks)
			{
				task.values.remove(var);
			}
		}
		
		// Hash current tasks values
		List<byte[]> currentValuesHash = new LinkedList<>();
		for(Task task : currentTasks)
		{
			currentValuesHash.add(task.hash());
		}
		
		// Reduce current tasks	in-between	
		for (int i = 0; i < currentValuesHash.size(); ++i)
		{
			byte i_Hash[] = currentValuesHash.get(i);
			for (int j = i + 1; j < currentValuesHash.size(); ++j)
			{
				byte j_Hash[] = currentValuesHash.get(j);
				if (Arrays.equals(i_Hash, j_Hash))
				{
					TaskExecutor.activeExecutionBlock.taskTable.remove(currentTasks.get(j));
					currentTasks.remove(j);
					currentValuesHash.remove(j);
					--j;
				}
			}
		}

		// Get all tasks from previous iterations
		List<Task> prevItersTasks = new LinkedList<>();
		prevItersTasks.addAll(loopExecBlock.taskTable);
		prevItersTasks.removeAll(currentTasks);
		
		// Hash all previous iterations tasks values
		List<byte[]> prevIterationsValuesHash = new LinkedList<>();
		for(Task task : prevItersTasks)
		{
			prevIterationsValuesHash.add(task.hash());
		}
	
		// Reduce current tasks	against tasks from previous iterations	
		for (int i = 0; i < prevIterationsValuesHash.size(); ++i)
		{
			byte i_Hash[] = prevIterationsValuesHash.get(i);
			for (int j = 0; j < currentValuesHash.size(); ++j)
			{
				byte j_Hash[] = currentValuesHash.get(j);
				if (Arrays.equals(i_Hash, j_Hash))
				{
					TaskExecutor.activeExecutionBlock.taskTable.remove(currentTasks.get(j));
					currentTasks.remove(j);
					currentValuesHash.remove(j);
					--j;
				}
			}
		}
		
		// For all tasks with new values, add another iteration.
		// Loop is finished when we skip this if.
		if (!currentTasks.isEmpty())
		{
			for(Task task : currentTasks)
			{
				Task nextIterationTask = task.clone();
				nextIterationTask.PC = 0;
				TaskExecutor.activeExecutionBlock.taskTable.add(nextIterationTask);
			}
			
			TaskExecutor.activeExecutionBlock.updateMinPC();
		}
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "--loop repeat--\r\n");
	}

}
