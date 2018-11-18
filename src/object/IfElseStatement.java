package object;

import object.values.ValuesHolder;

public class IfElseStatement extends Statement {

	private ExecutableBlock ifThenBody = null;
	private ExecutableBlock elseBody = null;
	
	public IfElseStatement(ExecutableBlock parentBlock) {
		ifThenBody = new ExecutableBlock(parentBlock);
		elseBody = null;
	}

	public ExecutableBlock getIfThenBody() {
		return ifThenBody;
	}

	public void setIfThenBody(ExecutableBlock ifThenBody) {
		this.ifThenBody = ifThenBody;
	}

	public ExecutableBlock getElseBody() {
		return elseBody;
	}

	public void addElseBody() {
		this.elseBody = new ExecutableBlock(ifThenBody.getParentExecutableBlock());
	}

	@Override
	public void execute(ValuesHolder values) throws Exception {
		ifThenBody.execute(values);
		if (elseBody != null)
		{
			elseBody.execute(values);
		}
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

}
