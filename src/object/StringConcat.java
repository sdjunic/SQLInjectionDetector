package object;

import java.util.List;
import execution.Task;
import object.values.MethodValuesHolder;
import object.values.ObjValue;
import object.values.StringVal;

public class StringConcat extends Statement {
	
	private VariableExec left = null, right1 = null, right2 = null;
	
	public StringConcat(VariableExec left, VariableExec right1, VariableExec right2) {
		this.left = left;
		this.right1 = right1;
		this.right2 = right2;
	}

	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		for (Task task : taskGroup)
		{
			execute(task.values);
		}
	}
	
	private void execute(MethodValuesHolder values) {
		ObjValue rightVal1 = null, rightVal2 = null;
		if (right1 != null) {
			if (right1.value != null) {
				rightVal1 = right1.value;
			} else {
				rightVal1 = values.get(right1.name);
			}
		}
		// TODO: consider assertion below
		//assert rightVal1 != null;
		if (rightVal1 == null) rightVal1 = StringVal.getString(true);
		
		if (right2 != null) {
			if (right2.value != null) {
				rightVal2= right2.value;
			} else {
				rightVal2 = values.get(right2.name);
			}
		}
		// TODO: consider assertion below
		//assert rightVal2 != null;
		if (rightVal2== null) rightVal2 = StringVal.getString(true);
	
		values.put(left.name, StringVal.getString(rightVal1.isSafe() && rightVal2.isSafe()));
	}
	
	@Override
	public void print(StringBuilder sb, String indention) {
		sb.append(indention + left + " = " + right1 + " + " + right2 + "\r\n");
	}
}
