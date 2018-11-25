package execution;

import object.values.MethodValuesHolder;

public class Task {
	
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
	
}
