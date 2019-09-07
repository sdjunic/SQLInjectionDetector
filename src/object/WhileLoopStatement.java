package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

// DO WHILE LOOP:
//
//>	loopBody
//	condition
//	repeat
//	end_exec_block


// WHILE LOOP:
//
//	loopBody
//>	condition
//	repeat
//	end_exec_block

public class WhileLoopStatement extends Statement {

	protected StatementsBlock condition = null;
	protected StatementsBlock loopBody = null;	
	
	protected StatementsBlock execLoopBody = null;	// = (body + condition) in case of both while and do-while loops
	
	private boolean isDoWhile = false;
	
	private int startPC = 0;	// Used for the first loop iteration:
								// for while loop start from condition (PC = body_statements_count),
								// for do-while loop start from body (PC = 0).
	
	public WhileLoopStatement(StatementsBlock parentBlock, boolean isDoWhile) {
		condition = new StatementsBlock(parentBlock);
		loopBody = new StatementsBlock(parentBlock);
		this.isDoWhile = isDoWhile;
	}
	
	public StatementsBlock getCondition() {
		return condition;
	}
	
	public StatementsBlock getLoopBody() {
		return loopBody;
	}

	public void parsingDone()
	{
		execLoopBody = new StatementsBlock(loopBody.getParentStatementsBlock());
		execLoopBody.appendStmtBlock(loopBody);
		execLoopBody.appendStmtBlock(condition);
		
		if (isDoWhile)
		{
			startPC = 0;
		}
		else
		{
			startPC = loopBody.getStmtCount();
		}
		
		execLoopBody.addStatement(new LoopRepeatStatement() /*this statement will do reduce*/);
		execLoopBody.addStatement(new EndExecBlockStatement(false /*reduce*/));
	}
	
	@Override
	public void print(StringBuilder sb, String indention) {
		if (isDoWhile)
		{
			sb.append(indention + "DO\r\n");
			loopBody.print(sb, indention.concat("    "));
			sb.append(indention + "WHILE\r\n");
			condition.print(sb, indention.concat("    "));
			sb.append(indention + "END_WHILE\r\n");
		}
		else
		{
			sb.append(indention + "WHILE\r\n");
			condition.print(sb, indention.concat("    "));
			sb.append(indention + "DO\r\n");
			loopBody.print(sb, indention.concat("    "));
			sb.append(indention + "END_WHILE\r\n");
		}
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// initialize a new execution block.
		ExecutionBlock loopExecBlock = new ExecutionBlock(this.execLoopBody);
		loopExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		loopExecBlock.brotherExecBlock = null;
		loopExecBlock.isMethodBody = false;
		loopExecBlock.isLoopExecBlock = true;
		
		// Move tasks to the new ExecutionBlock.
		for (Task task : taskGroup)
		{
			task.PC = this.startPC;
			
			loopExecBlock.taskTable.add(task);
		}
		
		TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);
		TaskExecutor.activeExecutionBlock = loopExecBlock;
	}

}
