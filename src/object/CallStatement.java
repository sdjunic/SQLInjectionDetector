package object;

import java.util.List;

public abstract class CallStatement extends Statement {
	
	VariableExec left = null;
	List<VariableExec> arguments = null;
	
	public List<VariableExec> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableExec> arguments) {
		this.arguments = arguments;
	}

	public VariableExec getLeft() {
		return left;
	}

	public void setLeft(VariableExec left) {
		this.left = left;
	}

}
