package object;

import java.util.*;
import java.util.Map.Entry;

import Parse.ParseData;
import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import main.SpecialArg;
import object.values.MethodValuesHolder;
import object.values.ObjValue;
import object.values.StringVal;
import symbol.object.*;
import symbol.object.Class;

public class MethCallStatement extends CallStatement {

	private String methodToCall = null;
	private VariableExec thisObj = null;
	
	private Method staticMethodToCall = null; // only for static methods
	
	public MethCallStatement(VariableExec left, String methodToCall, VariableExec thisObj, List<VariableExec> arguments) {
		this.left = left;
		this.methodToCall = methodToCall;
		this.thisObj = thisObj;
		this.arguments = arguments;
	}
	
	public MethCallStatement(VariableExec left, Method staticMethodToCall, List<VariableExec> arguments) {
		this.left = left;
		this.staticMethodToCall = staticMethodToCall;
		this.arguments = arguments;
	}

	public List<VariableExec> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableExec> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		HashMap<Method, List<Task>> hashMethodsToCall = getMethodToCall(taskGroup);
		
		ExecutionBlock prevMethodExecBlock = null;
		for (Entry<Method, List<Task>> methodToCall : hashMethodsToCall.entrySet())
		{
			Method m = methodToCall.getKey();
			List<Task> methTasks = methodToCall.getValue();
			
			// Execute special method inline
			if (!m.isDefined())
			{
				m.executeSpecialMethod(thisObj, arguments, left, methTasks);
				continue;
			}
			
			m.parseMethod();
			
			// Set new execution block
			ExecutionBlock methodExecBlock = new ExecutionBlock(m.getBody());
			methodExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
			methodExecBlock.brotherExecBlock = prevMethodExecBlock;
			methodExecBlock.isMethodBody = true;
			methodExecBlock.returnDestination = left;
			
			// Set tasks for new ExecutionBlock
			for (Task task : methTasks)
			{
				MethodValuesHolder callingMethValues = new MethodValuesHolder(task.values);
				
				// Populate ValuesHolder for method body
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
				
				task.values = callingMethValues;
				task.PC = 0;
			}
			// Add all tasks to new execution block
			methodExecBlock.taskTable.addAll(methTasks);
			// and remove them from current execution block
			TaskExecutor.activeExecutionBlock.taskTable.removeAll(methTasks);
			
			prevMethodExecBlock = methodExecBlock;
		}
		
		// If all methods were special we didn't add new EB, just continue execution in the same EB.
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
		
		for (Task task : taskGroup)
		{
			ObjValue thisObjValue = task.values.get(thisObj.name);
			Class thisObjClass = thisObjValue.getObjectType();
			if (thisObjClass != null) {
				Method methodToCall = thisObjClass.findMethod(this.methodToCall, this.arguments);
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
	
//	public void execute(MethodValuesHolder values) throws Exception {
//		Method methodToCall = staticMethodToCall;
//		
//		if (isInitFieldsMethod)
//		{
//			Method.methCallStack.push(methodToCall);
//			methodToCall.executeMethod(values);
//			Method.methCallStack.pop();
//			return;
//		}
//		
//		MethodValuesHolder callingMethValues = new MethodValuesHolder(values);
//		
//		if (methodToCall == null) {
//			ObjValue thisObjValue = values.get(thisObj.name);
//			Class thisObjClass = thisObjValue.getObjectType();
//			if (thisObjClass != null) {
//				
//				methodToCall = thisObjClass.findMethod(this.methodToCall, this.arguments);
//
//				callingMethValues.put("this", thisObjValue);
//				
//			} else {
//				throw new Exception("Metoda nije pronadjena!");
//			}
//		}
//		
//		if (methodToCall.isMethodAlreadyOnStack()) { return; }
//		Method.methCallStack.push(methodToCall);
//		
//		for (int i=0; i<arguments.size(); ++i) {
//			ObjValue argumentVal = null;
//			if (arguments.get(i) == null) continue;
//			if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
//			else argumentVal = values.get(arguments.get(i).name);
//			if (argumentVal == null) continue;
//			callingMethValues.put(methodToCall.getMethParamList().get(i).getName(), argumentVal);
//		}
//		
//		ObjValue returnVal = methodToCall.executeMethod(callingMethValues);
//		if (left != null) {
//			values.put(left.name, returnVal);
//		}
//		
//		Method.methCallStack.pop();
//	}
	
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
