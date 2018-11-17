package object;

import java.util.LinkedList;
import java.util.List;

import object.values.ObjValue;
import object.values.ValuesHolder;

public class MethodBody {
	
	private List<Statement> statements;
	private VariableExec returnVar;
	
	public MethodBody() {
		statements = new LinkedList<Statement>();
	}
	
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}
	
	public void addStatement(Statement stmt, int index) {
		statements.add(index, stmt);
	}
	
	public VariableExec getReturnVar() {
		return returnVar;
	}

	public void setReturnVar(VariableExec returnVar) {
		this.returnVar = returnVar;
	}

	public ObjValue execute(ValuesHolder values) throws Exception { 
		for (Statement s : statements) {
			s.execute(values);
		}
		if (returnVar == null) return null;
		if (returnVar.value != null) return returnVar.value;
		else return values.get(returnVar.name);
	}
	
	public void print(StringBuilder sb) {
		for (Statement stmt : statements) {
			if (stmt != null) stmt.print(sb);
		}
	}
	
}
