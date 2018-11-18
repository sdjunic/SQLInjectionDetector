package object;

import object.values.ObjValue;
import object.values.ValuesHolder;

public class MethodBody extends ExecutableBlock {
	
	private VariableExec returnVar;
	
	public MethodBody() {
		super();
	}
	
	public VariableExec getReturnVar() {
		return returnVar;
	}

	public void setReturnVar(VariableExec returnVar) {
		this.returnVar = returnVar;
	}
	
	@Override
	public ObjValue execute(ValuesHolder values) throws Exception { 
		for (Statement s : statements) {
			s.execute(values);
		}
		if (returnVar == null) return null;
		if (returnVar.value != null) return returnVar.value;
		else return values.get(returnVar.name);
	}
	
}
