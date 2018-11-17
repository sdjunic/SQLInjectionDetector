package object;

import object.values.ValuesHolder;

public abstract class Statement {
	
	public abstract void execute(ValuesHolder values) throws Exception;
	
	public abstract void print(StringBuilder sb);
}
