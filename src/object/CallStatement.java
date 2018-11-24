package object;

import java.util.List;

import execution.ExecutionBlock;

public abstract class CallStatement extends Statement {
	
	VariableExec left = null;
	List<VariableExec> arguments = null;
	
	public List<VariableExec> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableExec> arguments) {
		this.arguments = arguments;
	}

}
