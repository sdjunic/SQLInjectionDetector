package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

public class IfElseStatement extends Statement {

	private StatementsBlock ifStmtBody = null; // for ExecBlock which will do the task reduction
	
	private StatementsBlock ifThenBody = null; // for actual statements in true branch
	private StatementsBlock elseBody = null; // for actual statements in false branch
	
	public IfElseStatement(StatementsBlock parentBlock) {
		ifStmtBody = new StatementsBlock(parentBlock);
		ifThenBody = new StatementsBlock(parentBlock);
		elseBody = null;
	}

	public StatementsBlock getParentStatementsBlock()
	{
		assert (ifStmtBody != null);
		return ifStmtBody.getParentStatementsBlock();
	}
	
	public StatementsBlock getIfThenBody() {
		return ifThenBody;
	}

	public void setIfThenBody(StatementsBlock ifThenBody) {
		this.ifThenBody = ifThenBody;
	}

	public StatementsBlock getElseBody() {
		return elseBody;
	}

	public void addElseBody() {
		this.elseBody = new StatementsBlock(getParentStatementsBlock());
	}
	
	public void parsingDone()
	{
		assert (ifThenBody != null);
		assert (ifStmtBody != null);
		
		ifThenBody.addStatement(new EndExecBlockStatement(false /*reduce*/));
		if(elseBody != null)
		{
			elseBody.addStatement(new EndExecBlockStatement(false /*reduce*/));
		}
		
		ifStmtBody.addStatement(new EndExecBlockStatement(true /*reduce*/));
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "IF\r\n");
		ifThenBody.print(sb, indention.concat("    "));
		if (elseBody != null)
		{
			sb.append(indention + "ELSE\r\n");
			elseBody.print(sb, indention.concat("    "));
		}
		sb.append(indention + "END_IF\r\n");
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// Set new execution blocks
		ExecutionBlock ifStmtExecBlock = new ExecutionBlock(ifStmtBody);
		ifStmtExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		ifStmtExecBlock.brotherExecBlock = null;
		ifStmtExecBlock.isMethodBody = false;
		
		ExecutionBlock elseExecBlock = null;
		if (elseBody != null)
		{
			elseExecBlock = new ExecutionBlock(elseBody);	
			elseExecBlock.parentExecBlock = ifStmtExecBlock;
			elseExecBlock.brotherExecBlock = null;
			elseExecBlock.isMethodBody = false;
		}
		
		ExecutionBlock ifThenExecBlock = new ExecutionBlock(ifThenBody);
		ifThenExecBlock.parentExecBlock = ifStmtExecBlock;
		ifThenExecBlock.brotherExecBlock = elseExecBlock;
		ifThenExecBlock.isMethodBody = false;
		
		// Set tasks for new ExecutionBlock
		for (Task task : taskGroup)
		{
			task.PC = 0;
			
			Task elseBranchTask = task.clone();
			
			// Add tasks to the new execution blocks
			//
			ifThenExecBlock.taskTable.add(task);
			if (elseExecBlock != null)
			{
				assert (elseBody != null);
				elseExecBlock.taskTable.add(elseBranchTask);
			}
			else
			{
				assert (elseBody == null);
				ifStmtExecBlock.taskTable.add(elseBranchTask);
			}
		}
		
		TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);
		
		// Brother of ifThenExecBlock is elseExecBlock, if it exists
		TaskExecutor.activeExecutionBlock = ifThenExecBlock;
	}


}
