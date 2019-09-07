package execution;

import java.util.List;

import object.values.MethodValuesHolder;
import object.values.ValuesHolder;
import symbol.object.Class;
import symbol.object.MethParam;
import symbol.object.Method;
import symbol.object.Obj;

public class TaskExecutor {

	public static ExecutionBlock activeExecutionBlock;
	public static int finalTaskCount;
	
	public TaskExecutor() {}

	public static void execute(Method m, boolean initialArgumentsSafe) throws Exception
	{
		finalTaskCount = 0;
		
		m.parseMethod();
		
		MethodValuesHolder values = new MethodValuesHolder(null, m);
		
		if (!m.isStatic()) {
			Obj methodClass = m.getScope().getOuter().getParrentObj();
			if (!(methodClass instanceof Class)) throw new Exception();
			values.put("this", ValuesHolder.makeDefaultObjValue((Class)methodClass, initialArgumentsSafe));
		}
		List<MethParam> lmp = m.getMethParamList();
		for (int i=0; i<lmp.size(); ++i) {
			values.put(lmp.get(i).getName(), ValuesHolder.makeDefaultObjValue(lmp.get(i).getType().type, initialArgumentsSafe));
		}
		
		// Save hash method input values (for recursion detection).
		values.saveInputMVH_hash();
		
		activeExecutionBlock = new ExecutionBlock(m.getBody());
		activeExecutionBlock.isMethodBody = true;
		Task t = activeExecutionBlock.createNewTask(values);
		t.pushMethodOnStack(m);
		
		while (activeExecutionBlock != null)
		{
			activeExecutionBlock.executeNextStmt();
		}
	}
}
