package object;

import java.util.LinkedList;
import java.util.List;

import object.values.MethodValuesHolder;
import object.values.ObjValue;

public class StatementsBlock {

	protected List<Statement> statements = null;
	protected StatementsBlock parentStatementsBlock = null;
	
	public StatementsBlock() {
		statements = new LinkedList<Statement>();
	}

	public StatementsBlock(StatementsBlock parentExecutableBlock) {
		statements = new LinkedList<Statement>();
		this.parentStatementsBlock = parentExecutableBlock;
	}
	
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}
	
	public void addStatement(Statement stmt, int index) {
		statements.add(index, stmt);
	}
	
	public void setParentStatementsBlock(StatementsBlock parentStatementsBlock)
	{
		this.parentStatementsBlock = parentStatementsBlock;
	}
	
	public StatementsBlock getParentStatementsBlock()
	{
		return parentStatementsBlock;
	}
	
	public Statement getStatement(int n)
	{
		if (statements == null || statements.size() < n) return null;
		return statements.get(n);
	}
	
	public int getStmtCount()
	{
		return statements.size();
	}
	
	public void print(StringBuilder sb, String indention) {
		for (Statement stmt : statements) {
			if (stmt != null) stmt.print(sb, indention);
		}
	}

}
