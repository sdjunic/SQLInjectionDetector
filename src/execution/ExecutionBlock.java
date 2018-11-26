package execution;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.html.MinimalHTMLWriter;

import object.Statement;
import object.StatementsBlock;
import object.VariableExec;
import object.values.MethodValuesHolder;

public class ExecutionBlock {

	public ExecutionBlock parentExecBlock;
	public ExecutionBlock brotherExecBlock;
	public StatementsBlock statements;
	public List<Task> taskTable;
	
	private int minPC;
	
	public boolean isMethodBody;
	public VariableExec returnDestination = null; // valid only if isMethodBody == true
	
	private static final int maxPC = 100000;
	
	public ExecutionBlock(StatementsBlock statements) {
		this.parentExecBlock = null;
		this.brotherExecBlock = null;
		this.statements = statements;
		this.taskTable = new LinkedList<>();
		this.minPC = 0;
	}
	
	public void createNewTask(MethodValuesHolder values)
	{
		taskTable.add(new Task(values));
	}
	
	private List<Task> getNextActiveTaskGroup()
	{		
		List<Task> activeTaskGroup = new LinkedList<>();
		for (Task task : taskTable)
		{
			if (task.PC == this.minPC)
			{
				++task.PC;
				activeTaskGroup.add(task);
			}
		}
		return activeTaskGroup;
	}
	
	private void completeEB()
	{
		if (brotherExecBlock != null)
		{
			TaskExecutor.activeExecutionBlock = brotherExecBlock;
		}
		else
		{
			TaskExecutor.activeExecutionBlock = parentExecBlock;
		}
	}
	
	public void executeNextStmt() throws Exception
	{
		int endPC = statements.getStmtCount();
		if (minPC >= endPC)
		{
			completeEB();
		}
		else
		{
			List<Task> activeTaskGroup = getNextActiveTaskGroup();
			assert !activeTaskGroup.isEmpty();
			Statement stmt = statements.getStatement(minPC++);
			stmt.execute(activeTaskGroup);
		}
	}
	
	public void updateMinPC()
	{
		this.minPC = maxPC;
		for (Task task : taskTable)
		{
			if (task.PC < this.minPC)
			{
				this.minPC = task.PC;
			}
		}
	}
	
	public int getParentEB_PC()
	{
		if (parentExecBlock != null) return parentExecBlock.minPC;
		else return -1;
	}
	
	public void removeTasks(List<Task> tasksToRemove)
	{
		for (Task t : tasksToRemove) taskTable.remove(t);
		updateMinPC();
	}

}
