package object;

import java.util.List;
import execution.Task;

public abstract class Statement {
	
	public abstract void execute(List<Task> taskGroup) throws Exception;

	public abstract void print(StringBuilder sb, String indention);
}
