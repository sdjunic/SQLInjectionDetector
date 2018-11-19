package object;

import object.values.ValuesHolder;

public class LoopStatement extends Statement {

	protected ExecutableBlock loopBody = null;
	
	public LoopStatement(ExecutableBlock parentBlock) {
		loopBody = new ExecutableBlock(parentBlock);
	}
	
	public ExecutableBlock getLoopBody() {
		return loopBody;
	}

	@Override
	public void execute(ValuesHolder values) throws Exception {
		loopBody.execute(values);
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "LOOP\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "END_LOOP\r\n");
	}

}
