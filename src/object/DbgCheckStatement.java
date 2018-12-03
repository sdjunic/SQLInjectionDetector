package object;

import java.util.List;

import execution.Task;

public class DbgCheckStatement extends Statement {

	private int taskNum;
	
	public DbgCheckStatement(int taskNum) {
		assert main.Main.useCheck;
		this.taskNum = taskNum;
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		assert main.Main.useCheck;
		assert taskGroup.size() == taskNum;
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		assert main.Main.useCheck;
		sb.append(indention + "__check_" + taskNum + "\r\n");
	}

}
