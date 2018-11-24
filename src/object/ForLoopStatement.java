package object;

import object.values.MethodValuesHolder;

public class ForLoopStatement extends Statement {

	protected StatementsBlock condition = null;
	protected StatementsBlock update = null;
	protected StatementsBlock loopBody = null;
	
	public ForLoopStatement(StatementsBlock parentBlock) {
		this.condition = new StatementsBlock(parentBlock);
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
	
	public void addUpdate() {
		this.update = new StatementsBlock(condition.getParentExecutableBlock());
	}

	public void addLoopBody() {
		this.loopBody = new StatementsBlock(condition.getParentExecutableBlock());
	}

	@Override
	public void execute(MethodValuesHolder values) throws Exception {
		condition.execute(values);
		update.execute(values);
		loopBody.execute(values);
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

}
