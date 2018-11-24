package object;

import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import object.values.MethodValuesHolder;

public class LoopStatement extends Statement {

	protected StatementsBlock loopBody = null;
	
	public LoopStatement(StatementsBlock parentBlock) {
		loopBody = new StatementsBlock(parentBlock);
	}
	
	public StatementsBlock getLoopBody() {
		return loopBody;
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "LOOP\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "END_LOOP\r\n");
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
