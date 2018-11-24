package object;

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
	public void execute(MethodValuesHolder values) throws Exception {
		loopBody.execute(values);
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + "LOOP\r\n");
		loopBody.print(sb, indention.concat("    "));
		sb.append(indention + "END_LOOP\r\n");
	}

}
