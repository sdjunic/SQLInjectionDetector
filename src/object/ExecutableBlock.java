package object;

import java.util.LinkedList;
import java.util.List;

import object.values.MethodValuesHolder;
import object.values.ObjValue;

public class ExecutableBlock {

	protected List<Statement> statements = null;
	protected ExecutableBlock parentExecutableBlock = null;
	
	public ExecutableBlock() {
		statements = new LinkedList<Statement>();
	}

	public ExecutableBlock(ExecutableBlock parentExecutableBlock) {
		statements = new LinkedList<Statement>();
		this.parentExecutableBlock = parentExecutableBlock;
	}
	
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}
	
	public void addStatement(Statement stmt, int index) {
		statements.add(index, stmt);
	}
	
	public void setParentExecutableBlock(ExecutableBlock parentExecutableBlock)
	{
		this.parentExecutableBlock = parentExecutableBlock;
	}
	
	public ExecutableBlock getParentExecutableBlock()
	{
		return parentExecutableBlock;
	}
	
	public ObjValue execute(MethodValuesHolder values) throws Exception { 
		for (Statement s : statements) {
			s.execute(values);
		}
		return null;
	}
	
	public void print(StringBuilder sb, String indention) {
		for (Statement stmt : statements) {
			if (stmt != null) stmt.print(sb, indention);
		}
	}

}
