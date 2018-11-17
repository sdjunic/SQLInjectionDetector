package object;

import object.values.*;

public class StringConcat extends Statement {
	
	private VariableExec left = null, right1 = null, right2 = null;
	
	public StringConcat(VariableExec left, VariableExec right1, VariableExec right2) {
		this.left = left;
		this.right1 = right1;
		this.right2 = right2;
	}

	@Override
	public void execute(ValuesHolder values) {
		ObjValue rightVal1 = null, rightVal2 = null;
		if (right1 != null) {
			if (right1.value != null) {
				rightVal1 = right1.value;
			} else {
				rightVal1 = values.get(right1.name);
			}
		}
		if (rightVal1 == null) rightVal1 = new StringVal(true);
		
		if (right2 != null) {
			if (right2.value != null) {
				rightVal2= right2.value;
			} else {
				rightVal2 = values.get(right2.name);
			}
		}
		if (rightVal2== null) rightVal2 = new StringVal(true);
	
		values.put(left.name, new StringVal(rightVal1.isSafe() && rightVal2.isSafe()));
	}

	@Override
	public void print(StringBuilder sb) {
		sb.append(left + " = " + right1 + " + " + right2 + "\r\n");
	}

}
