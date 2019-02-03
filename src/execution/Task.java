package execution;

import java.security.NoSuchAlgorithmException;
import java.util.Stack;

import object.values.MethodValuesHolder;
import symbol.object.Method;

public class Task {
	
	public Stack<Method> methCallStack = new Stack<Method>();
	public MethodValuesHolder values;
	public int PC;
	
	public Task(MethodValuesHolder values) {
		this(values, 0);
	}
	
	public Task(MethodValuesHolder values, int PC) {
		super();
		this.values = values;
		this.PC = PC;
	}
	
	public Task clone()
	{
		Task clone = new Task(this.values.deepCopy(), this.PC);
		for (int i = 0; i < this.methCallStack.size(); ++i)
		{
			clone.methCallStack.push(this.methCallStack.get(i));
		}
		return clone;
	}
	
	public byte[] hash() throws NoSuchAlgorithmException
	{
		return this.values.hash();
	}
	
	public void popMethodFromStack()
	{
		assert !methCallStack.isEmpty();
		methCallStack.pop();
	}
	
	public void pushMethodOnStack(Method m)
	{
		methCallStack.push(m);
	}
	
	public void printMethodCallStack(StringBuilder sb) {
		for (int i = methCallStack.size()-1; i>=0; --i) {
			Method meth = methCallStack.get(i);
			sb.append("- ");
			meth.printCallSignOnly(sb);
		}
	}
}
