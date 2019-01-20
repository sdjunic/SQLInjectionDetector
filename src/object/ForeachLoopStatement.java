package object;

import java.util.List;

import execution.Task;

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

	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "FOREACH\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "END_FOREACH\r\n");
	}

}
