package object;

import java.util.*;

import execution.Task;
import object.values.MethodValuesHolder;
import symbol.object.Obj;

public class AssignmentStatement extends Statement {
	
	private static int genericNameCount = 0; 
	
	private VariableExec left = null, right = null;
	
	public AssignmentStatement(VariableExec left, VariableExec right) {
		this.left = left;
		this.right = right;
	}
	
	public static VariableExec getNewTempVariable(Obj type) {
		List<String> name = new LinkedList<String>();
		name.add(++genericNameCount + "_temp");
		return new VariableExec(name, type);
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		for (Task task : taskGroup)
		{
			execute(task.values);
		}
	}
	
	private void execute(MethodValuesHolder values)
	{
		if (right.value != null) {
			values.put(left.name, right.value);
		} else {
			values.put(left.name, values.get(right.name));
		}
	}
	
	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + left + " = " + right + "\r\n");
	}

}
