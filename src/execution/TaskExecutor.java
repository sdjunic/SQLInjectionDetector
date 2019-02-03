package execution;

import java.util.List;

import object.values.MethodValuesHolder;
import symbol.object.Class;
import symbol.object.MethParam;
import symbol.object.Method;
import symbol.object.Obj;

public class TaskExecutor {

	public static ExecutionBlock activeExecutionBlock;
	public static int finalTaskCount;
	
	public TaskExecutor() {
		// TODO Auto-generated constructor stub
	}

	public static void execute(Method m, boolean initialArgumentsSafe) throws Exception
	{
		finalTaskCount = 0;
		
		m.parseMethod();
		
		MethodValuesHolder values = new MethodValuesHolder(null, m);
		
		if (!m.isStatic()) {
			Obj methodClass = m.getScope().getOuter().getParrentObj();
			if (!(methodClass instanceof Class)) throw new Exception();
			//values.addObject((Class)methodClass, "this", initialArgumentsSafe);
			values.put("this", values.makeDefaultObjValue((Class)methodClass, initialArgumentsSafe));
		}
		List<MethParam> lmp = m.getMethParamList();
		for (int i=0; i<lmp.size(); ++i) {
			values.addObject(lmp.get(i).getType().type, lmp.get(i).getName(), initialArgumentsSafe);
			values.put(lmp.get(i).getName(), values.makeDefaultObjValue(lmp.get(i).getType().type, initialArgumentsSafe));
		}
		
		// Hash method input values, in case of recursion.
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
