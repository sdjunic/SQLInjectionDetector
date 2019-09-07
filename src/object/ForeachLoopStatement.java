package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

public class ForeachLoopStatement extends Statement {

	protected StatementsBlock loopBody = null;	
	
	public ForeachLoopStatement(StatementsBlock parentBlock) {
		loopBody = new StatementsBlock(parentBlock);
	}
	
	public StatementsBlock getStmtBlock()
	{
		return this.loopBody;
	}
	
	public void parsingDone()
	{
		loopBody.addStatement(new LoopRepeatStatement() /*this stmt will do reduce*/);
		loopBody.addStatement(new EndExecBlockStatement(false /*reduce*/));
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// Initialize a new execution block.
		ExecutionBlock loopExecBlock = new ExecutionBlock(this.loopBody);
		loopExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		loopExecBlock.brotherExecBlock = null;
		loopExecBlock.isMethodBody = false;
		loopExecBlock.isLoopExecBlock = true;
		
		// Move tasks to the new ExecutionBlock.
		for (Task task : taskGroup)
		{
			// Reset PC.
			task.PC = 0;

			// Add tasks to the new execution block.
			loopExecBlock.taskTable.add(task);
		}
		
		TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);
		TaskExecutor.activeExecutionBlock = loopExecBlock;
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "FOREACH\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "END_FOREACH\r\n");
	}

}
