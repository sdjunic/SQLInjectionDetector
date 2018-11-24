package object;

import object.values.MethodValuesHolder;

public class IfElseStatement extends Statement {

	private StatementsBlock ifThenBody = null;
	private StatementsBlock elseBody = null;
	
	public IfElseStatement(StatementsBlock parentBlock) {
		ifThenBody = new StatementsBlock(parentBlock);
		elseBody = null;
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
		this.elseBody = new StatementsBlock(ifThenBody.getParentExecutableBlock());
	}

	@Override
	public void execute(MethodValuesHolder values) throws Exception {
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
