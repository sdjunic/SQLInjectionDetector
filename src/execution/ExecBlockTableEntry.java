package execution;

public class ExecBlockTableEntry {

	public Task task;
	public boolean isExecuted;
	
	public ExecBlockTableEntry(Task task, boolean isExecuted) {
		super();
		this.task = task;
		this.isExecuted = isExecuted;
	}
}
