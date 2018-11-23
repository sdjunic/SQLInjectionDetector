package object;

import object.values.MethodValuesHolder;

public class ForLoopStatement extends Statement {

	protected ExecutableBlock condition = null;
	protected ExecutableBlock update = null;
	protected ExecutableBlock loopBody = null;
	
	public ForLoopStatement(ExecutableBlock parentBlock) {
		this.condition = new ExecutableBlock(parentBlock);
	}

	public ExecutableBlock getCondition() {
		return condition;
	}

	public ExecutableBlock getUpdate() {
		return update;
	}

	public ExecutableBlock getLoopBody() {
		return loopBody;
	}
	
	public void addUpdate() {
		this.update = new ExecutableBlock(condition.getParentExecutableBlock());
	}

	public void addLoopBody() {
		this.loopBody = new ExecutableBlock(condition.getParentExecutableBlock());
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
