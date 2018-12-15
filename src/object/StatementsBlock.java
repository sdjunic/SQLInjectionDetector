package object;

import java.util.LinkedList;
import java.util.List;

import object.values.MethodValuesHolder;
import object.values.ObjValue;

public class StatementsBlock {

	private List<Statement> statements = null;
	private StatementsBlock parentStatementsBlock = null;
	
	// Local variables declared in this block.
	// Used to delete variables from VH map when stmt block is executed.
	private List<String> blockLocalVariables = null;
	
	public StatementsBlock() {
		statements = new LinkedList<Statement>();
		blockLocalVariables = new LinkedList<>();
	}

	public StatementsBlock(StatementsBlock parentExecutableBlock) {
		this();
		this.parentStatementsBlock = parentExecutableBlock;
	}
	
	public void addStatement(Statement stmt) {
		statements.add(stmt);
	}

	public void addStatement(Statement stmt, boolean isLocalVariableDeclaration) {
		statements.add(stmt);
		if (isLocalVariableDeclaration)
		{
			assert (stmt instanceof AssignmentStatement);
			AssignmentStatement assignmentStmt = (AssignmentStatement)stmt;
			VariableExec var = assignmentStmt.getLeft();
			assert var.name != null;
			assert var.name.size() == 1;
			blockLocalVariables.add(var.name.get(0));
		}
	}
	
	public void addStatement(Statement stmt, int index) {
		statements.add(index, stmt);
	}
	
	public void appendStmtBlock(StatementsBlock stmtBlock)
	{
		assert (this.parentStatementsBlock == stmtBlock.parentStatementsBlock);
		
		this.statements.addAll(stmtBlock.statements);
		this.blockLocalVariables.addAll(stmtBlock.blockLocalVariables);
	}
	
	public void addAllStatementsFromBlock(StatementsBlock stmtBlock)
	{
		this.statements.addAll(stmtBlock.statements);
	}
	
	public void prependStmtBlock(StatementsBlock stmtBlock)
	{
		assert (this.parentStatementsBlock == stmtBlock.parentStatementsBlock);
		
		this.statements.addAll(0, stmtBlock.statements);
		this.blockLocalVariables.addAll(stmtBlock.blockLocalVariables);
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
		assert (statements != null);
		if (statements == null || statements.size() < n) return null;
		return statements.get(n);
	}
	
	public Statement getLastStatement()
	{
		assert (statements != null);
		if (statements == null) return null;
		return statements.get(statements.size() - 1);
	}
	
	public int getStmtCount()
	{
		return statements.size();
	}
	
	public List<String> getBlockLocalVariables() {
		return blockLocalVariables;
	}

	public void print(StringBuilder sb, String indention) {
		for (Statement stmt : statements) {
			if (stmt != null) stmt.print(sb, indention);
		}
	}

}
