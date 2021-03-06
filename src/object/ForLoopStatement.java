package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;

//FOR LOOP:
//
//	eb1:
//> i
//>	loopBody
//	condition
//	repeat
//	end_exec_block

public class ForLoopStatement extends Statement {

	protected StatementsBlock init = null;
	protected StatementsBlock condition = null;
	protected StatementsBlock update = null;
	protected StatementsBlock loopBody = null;
	
	protected StatementsBlock execLoopBody = null;	// = (body + update + condition)
	
	private int startPC = 0; // Used to start the loop iteration from the condition.

	
	public ForLoopStatement(StatementsBlock parentBlock) {
		this.init = new StatementsBlock(parentBlock);
		this.condition = new StatementsBlock(parentBlock);
		this.update = new StatementsBlock(parentBlock);
		this.loopBody = new StatementsBlock(parentBlock);
	}
	
	public StatementsBlock getInit() {
		return init;
	}

	public StatementsBlock getCondition() {
		return condition;
	}

	public StatementsBlock getUpdate() {
		return update;
	}

	public StatementsBlock getLoopBody() {
		return loopBody;
	}
	
	public void parsingDone()
	{					
		execLoopBody = new StatementsBlock(loopBody.getParentStatementsBlock());
		execLoopBody.appendStmtBlock(loopBody);
		execLoopBody.appendStmtBlock(update);
		execLoopBody.appendStmtBlock(condition);
		execLoopBody.getBlockLocalVariables().addAll(init.getBlockLocalVariables());
		
		startPC = loopBody.getStmtCount() + update.getStmtCount();
		
		execLoopBody.addStatement(new LoopRepeatStatement() /* this statement will reduce tasks */);
		execLoopBody.addStatement(new EndExecBlockStatement(false /*reduce*/));
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "FOR\r\n");
		sb.append(indention + "cond\r\n");
		condition.print(sb, indention.concat("    "));
		sb.append(indention + "body\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "update\r\n");
		update.print(sb, indention.concat("    "));
		sb.append(indention + "END_FOR\r\n");
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// Initialize a new execution block.
		ExecutionBlock loopExecBlock = new ExecutionBlock(this.execLoopBody);
		loopExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		loopExecBlock.brotherExecBlock = null;
		loopExecBlock.isMethodBody = false;
		loopExecBlock.isLoopExecBlock = true;
		
		// Move tasks to the new ExecutionBlock.
		for (Task task : taskGroup)
		{
			// Set PC to the first condition instruction.
			task.PC = this.startPC;

			// Add tasks to the new execution block.
			loopExecBlock.taskTable.add(task);
		}
		
		TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);
		TaskExecutor.activeExecutionBlock = loopExecBlock;
	}

}
