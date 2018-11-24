package object;

import object.values.ObjValue;
import object.values.MethodValuesHolder;

public class MethodBody extends StatementsBlock {
	
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
	public ObjValue execute(MethodValuesHolder values) throws Exception { 
		for (Statement s : statements) {
			s.execute(values);
		}
		if (returnVar == null) return null;
		if (returnVar.value != null) return returnVar.value;
		else return values.get(returnVar.name);
	}
	
}
