package execution;

import java.util.List;

import object.StatementsBlock;

public class ExecutionBlock {

	private ExecutionBlock parentExecBlock;
	private StatementsBlock statements;
	private List<ExecBlockTableEntry> ebTable;
	
	public ExecutionBlock() {
		// TODO Auto-generated constructor stub
	}

}
