package object;

import java.util.LinkedList;
import java.util.List;

import object.values.MethodValuesHolder;
import object.values.ObjValue;

public class StatementsBlock {

	protected List<Statement> statements = null;
	protected StatementsBlock parentExecutableBlock = null;
	
	public StatementsBlock() {
		statements = new LinkedList<Statement>();
	}

	public StatementsBlock(StatementsBlock parentExecutableBlock) {
		statements = new LinkedList<Statement>();
		this.parentExecutableBlock = parentExecutableBlock;
	}
	
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}
	
	public void addStatement(Statement stmt, int index) {
		statements.add(index, stmt);
	}
	
	public void setParentExecutableBlock(StatementsBlock parentExecutableBlock)
	{
		this.parentExecutableBlock = parentExecutableBlock;
	}
	
	public StatementsBlock getParentExecutableBlock()
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
