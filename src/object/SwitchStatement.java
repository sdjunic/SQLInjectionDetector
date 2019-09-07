package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

public class SwitchStatement extends Statement {

	private StatementsBlock switchStmtBody = null; // For the ExecBlock which will do the task reduction.
	
	private List<StatementsBlock> caseBlocks = null; 
	
	public SwitchStatement(StatementsBlock parentBlock) {
		switchStmtBody = new StatementsBlock(parentBlock);
	}

	public StatementsBlock getParentStatementsBlock()
	{
		assert (switchStmtBody != null);
		return switchStmtBody.getParentStatementsBlock();
	}
	
	public void parsingDone(List<StatementsBlock> caseBlocks)
	{
		this.caseBlocks = caseBlocks;
		for (StatementsBlock sb : caseBlocks)
		{
			sb.setParentStatementsBlock(this.switchStmtBody);
			sb.addStatement(new EndExecBlockStatement(false /*reduce*/));
		}
		switchStmtBody.addStatement(new EndExecBlockStatement(true /*reduce*/));
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "SWITCH\r\n");
		for (StatementsBlock stmtBlock: caseBlocks)
		{
			sb.append(indention.concat("    ") + "CASE\r\n");
			stmtBlock.print(sb, indention.concat("        "));
		}
		sb.append(indention + "END_SWITCH\r\n");
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// Initialize the new execution blocks.
		ExecutionBlock switchStmtExecBlock = new ExecutionBlock(switchStmtBody);
		switchStmtExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		switchStmtExecBlock.brotherExecBlock = null;
		switchStmtExecBlock.isMethodBody = false;
		
		ExecutionBlock prevExecBlock = null;
		for (StatementsBlock sb : this.caseBlocks)
		{
			ExecutionBlock currentExecBlock = new ExecutionBlock(sb);
			currentExecBlock.parentExecBlock = switchStmtExecBlock;
			currentExecBlock.brotherExecBlock = prevExecBlock;
			currentExecBlock.isMethodBody = false;
			prevExecBlock = currentExecBlock;
		}
		ExecutionBlock activeExecBlock = prevExecBlock;
		
		for (Task task : taskGroup)
		{
			task.PC = 0;
		}
		
		boolean isFirst = true;
		while (prevExecBlock != null)
		{
			if (isFirst)
			{
				isFirst = false;
				prevExecBlock.taskTable.addAll(taskGroup);
			}
			else
			{
				// Clone and move tasks to the new ExecutionBlock.
				for (Task task : taskGroup)
				{
					Task cloneTask = task.clone();
					
					// Move tasks to the new execution block.
					//
					prevExecBlock.taskTable.add(cloneTask);
				}
			}
			prevExecBlock = prevExecBlock.brotherExecBlock;
		}
		
		TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);		
		TaskExecutor.activeExecutionBlock = activeExecBlock;
	}
}
