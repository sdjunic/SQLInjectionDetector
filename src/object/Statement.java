package object;

import object.values.MethodValuesHolder;

public abstract class Statement {
	
	public abstract void execute(MethodValuesHolder values) throws Exception;
	
	public abstract void print(StringBuilder sb, String indention);
}
