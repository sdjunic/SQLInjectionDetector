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
		if (main.Main.infoPS != null)
		{
			int taskId = 1;
			main.Main.infoPS.println("Task num: " + taskGroup.size() + "(" + taskNum + ")");
			for (Task task : taskGroup)
			{
				StringBuilder sb = new StringBuilder("Taks("+ taskId++ +") values:\n");
				task.values.print(sb, "   ",  true);
				main.Main.infoPS.println(sb);
			}
		}
		
		assert main.Main.useCheck;
		assert taskGroup.size() == taskNum;
	}

	@Override
	public void print(StringBuilder sb, String indention) {
		assert main.Main.useCheck;
		sb.append(indention + "__check_" + taskNum + "\r\n");
	}

}
