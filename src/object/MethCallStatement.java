package object;

import java.util.*;
import java.util.Map.Entry;

import Parse.ParseData;
import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import main.Main;
import object.values.MethodValuesHolder;
import object.values.NullValue;
import object.values.ObjValue;
import symbol.object.*;
import symbol.object.Class;

public class MethCallStatement extends CallStatement {

	private String methodToCall = null;
	private VariableExec thisObj = null;
	
	private Method staticMethodToCall = null; // For static methods.
	
	private Method libraryMethodToCall = null; // For non-static library methods when this object is null.
	private boolean isLibraryMethod = false;
	
	public MethCallStatement(VariableExec left, Method method, VariableExec thisObj, List<VariableExec> arguments) {
		this.isLibraryMethod = !method.isDefined();
		this.left = left;
		if (thisObj != null)
		{
			String methodNameWithArguments = method.getName();
			this.methodToCall = methodNameWithArguments.substring(0, methodNameWithArguments.indexOf("("));
			
			if (this.isLibraryMethod)
			{
				libraryMethodToCall = method;
			}
		}
		else
		{
			this.staticMethodToCall = method;
		}
		this.thisObj = thisObj;
		this.arguments = arguments;
	}
	
	public MethCallStatement(VariableExec left, Method staticMethodToCall, List<VariableExec> arguments) {
		this.left = left;
		this.staticMethodToCall = staticMethodToCall;
		this.arguments = arguments;
		this.isLibraryMethod = !staticMethodToCall.isDefined();
	}

	public List<VariableExec> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableExec> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		if (Main.infoPS != null)
		{
			Main.infoPS.println("Calling " 
								+ (methodToCall != null ? methodToCall : staticMethodToCall.getName())
								+ " (" + taskGroup.size() + ")");
		}
		
		HashMap<Method, List<Task>> hashMethodsToCall = getMethodToCall(taskGroup);
		
		ExecutionBlock prevMethodExecBlock = null;
		for (Entry<Method, List<Task>> methodToCall : hashMethodsToCall.entrySet())
		{
			Method m = methodToCall.getKey();
			List<Task> methTasks = methodToCall.getValue();
			
			// Execute special method inline.
			if (!m.isDefined())
			{
				m.executeSpecialMethod(thisObj, arguments, left, methTasks);
				continue;
			}
			
			m.parseMethod();
			
			// initialize a new execution block.
			ExecutionBlock methodExecBlock = new ExecutionBlock(m.getBody());
			methodExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
			methodExecBlock.brotherExecBlock = prevMethodExecBlock;
			methodExecBlock.isMethodBody = true;
			methodExecBlock.returnDestination = left;
			
			// Move tasks to the new ExecutionBlock.
			Iterator<Task> taskIter = methTasks.iterator();
			while (taskIter.hasNext())
			{
				Task task = taskIter.next();
				
				MethodValuesHolder callingMethValues = new MethodValuesHolder(task.values, m);
				
				// Populate ValuesHolder for the new method.
				if (staticMethodToCall == null)
				{
					ObjValue thisObjValue = task.values.get(thisObj.name);
					callingMethValues.put("this", thisObjValue);
				}
				for (int i=0; i<arguments.size(); ++i) {
					ObjValue argumentVal = null;
					if (arguments.get(i) == null) continue;
					if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
					else argumentVal = task.values.get(arguments.get(i).name);
					if (argumentVal == null) continue;
					callingMethValues.put(m.getMethParamList().get(i).getName(), argumentVal);
				}
				
				// Hash method input values (for recursion cycle detection).
				callingMethValues.saveInputMVH_hash();
				if(callingMethValues.checkForRecursionCycle())
				{
					// Remove this task due to completed recursion cycle.
					//
					if (Main.infoPS != null) Main.infoPS.println("Killing the task due to completed recursion cycle.");
					TaskExecutor.activeExecutionBlock.taskTable.remove(task);
					taskIter.remove();
					continue;
				}
				
				task.values = callingMethValues;
				task.PC = 0;
				task.pushMethodOnStack(m);
			}
			
			if (!methTasks.isEmpty())
			{
				// Add all tasks to the new execution block...
				methodExecBlock.taskTable.addAll(methTasks);
			
				// and remove them from the current execution block.
				TaskExecutor.activeExecutionBlock.taskTable.removeAll(methTasks);

				prevMethodExecBlock = methodExecBlock;
			}
		}
		
		// If all the methods were special, or all tasks completes recursion cycle, we didn't make a new EB.
		// In such cases just continue execution in the same EB.
		if (prevMethodExecBlock != null)
		{
			TaskExecutor.activeExecutionBlock = prevMethodExecBlock;
		}
	}
	
	public HashMap<Method, List<Task>> getMethodToCall(List<Task> taskGroup) throws Exception
	{
		HashMap<Method, List<Task>> res = new HashMap<>();
		if (staticMethodToCall != null)
		{
			res.put(staticMethodToCall, taskGroup);
			return res;
		}
		
		Iterator<Task> taskIter = taskGroup.iterator();
		while (taskIter.hasNext())
		{
			Task task = taskIter.next();
			ObjValue thisObjValue = task.values.get(thisObj.name);
			
			// Library method is often call for non-defined class which is obtained by another
			// library method. Since we can't define all library classes/methods, try to execute
			// method as it is static. If method don't use THIS argument the execution will be successful.
			//
			if (thisObjValue == null)
			{
				assert this.isLibraryMethod;
				
				if (!res.containsKey(libraryMethodToCall))
				{
					res.put(libraryMethodToCall, new LinkedList<>());
				}
				res.get(libraryMethodToCall).add(task);
				continue;
			}
			
			if(thisObjValue instanceof NullValue)
			{
				// Remove this task due to null pointer exception.
				//
				if (Main.infoPS != null) Main.infoPS.println("Killing the task due to null pointer exception.");
				TaskExecutor.activeExecutionBlock.taskTable.remove(task);
				taskIter.remove();
				continue;
			}
			
			assert thisObjValue.getObjectType() instanceof Class;
			Class thisObjClass = (Class)thisObjValue.getObjectType();
			if (thisObjClass != null) {
				Method methodToCall = thisObjClass.findMethod(this.methodToCall, this.arguments);
				if (methodToCall == null)
				{
					throw new Exception("Metoda nije pronadjena!");
				}
				
				if (!res.containsKey(methodToCall))
				{
					res.put(methodToCall, new LinkedList<>());
				}
				res.get(methodToCall).add(task);
				
			} else {
				throw new Exception("Metoda nije pronadjena!");
			}
		}
		
		return res;
	}
	
	@Override
	public void print(StringBuilder sb, String indention) {
		if (left != null) sb.append(indention + left + " = ");
		if (thisObj != null) {
			sb.append(ParseData.makeFullNameWithDots(thisObj.name)).append(".");
		}

		if (staticMethodToCall == null) sb.append(methodToCall).append("() (");
		else sb.append(staticMethodToCall.getName()).append(" (");
		
		for (int i=0; i<arguments.size(); ++i) {
			sb.append(arguments.get(i));
			if (i < arguments.size()-1) sb.append(", ");
		}
		sb.append(")\r\n");
	}
	
}
