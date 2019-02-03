package execution;

import java.util.LinkedList;
import java.util.List;

import object.Statement;
import object.StatementsBlock;
import object.VariableExec;
import object.values.MethodValuesHolder;

public class ExecutionBlock {

	// TODO(delete) currently used only for assertion
	public boolean isLoopExecBlock = false;
	
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
	
	public Task createNewTask(MethodValuesHolder values)
	{
		Task t = new Task(values);
		taskTable.add(t);
		return t;
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
		if (activeTaskGroup.isEmpty())
		{
			updateMinPC();
			for (Task task : taskTable)
			{
				if (task.PC == this.minPC)
				{
					++task.PC;
					activeTaskGroup.add(task);
				}
			}
		}
		return activeTaskGroup;
	}
	
	public void complete()
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
		if (taskTable.isEmpty())
		{
			this.complete();
			return;
		}
		
		int endPC = statements.getStmtCount();
		assert (minPC < endPC);
		
		List<Task> activeTaskGroup = getNextActiveTaskGroup();
		assert !activeTaskGroup.isEmpty();
		Statement stmt = statements.getStatement(minPC++);
		stmt.execute(activeTaskGroup);
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

}
