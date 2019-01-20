package object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import execution.ExecutionBlock;
import execution.Task;
import execution.TaskExecutor;
import main.Main;
import object.values.MethodValuesHolder;
import object.values.ObjValue;
import symbol.object.Method;
import symbol.object.Class;
import symbol.object.Field;

public class ConstructorCallStatement extends CallStatement {

	private Method constructor = null;
	
	public ConstructorCallStatement(VariableExec left, Method constructor, List<VariableExec> arguments) {
		this.left = left;
		this.constructor = constructor;
		this.arguments = arguments;
	}
	
	public Method getConstructor() {
		return constructor;
	}
	
	private void parse() throws Exception
	{
		if (!constructor.isParsed())
		{
			constructor.parseMethod();
			if (!constructor.isContainsExplThisConstructorCall()) {				
				Method fieldInitMethod = constructor.getParentClass().getFieldsInitializerMethod();
				if (fieldInitMethod != null)
				{
					List<VariableExec> args = new LinkedList<VariableExec>();
					MethCallStatement fieldInitMethCall = new MethCallStatement(
							null, 
							fieldInitMethod,
							new VariableExec("this", constructor.getParentClass()), 
							args);
					constructor.getBody().addStatement(fieldInitMethCall, 0);
				}
				
				if (!constructor.isContainsExplSuperConstructorCall()) {
					if (constructor.getParentClass().getSuperClass() != null && constructor.getParentClass().getSuperClass().type != null) {
						Class superClass = (Class)constructor.getParentClass().getSuperClass().type;
						List<VariableExec> args = new LinkedList<VariableExec>();
						Field superField = constructor.getParentClass().findField("super");
						List<String> name = new LinkedList<String>();
						name.add("this"); name.add("super");
						ConstructorCallStatement superConstrCall = new ConstructorCallStatement(new VariableExec(name, superField), superClass.findConstructor(args), args);
						constructor.getBody().addStatement(superConstrCall, 0);
					}
				}
			}
			else
			{
				throw new Exception("Calling this() in constructor isn't supported!");
			}
			
			constructor.getBody().addStatement(new ReturnStatement(new VariableExec("this", constructor.getParentClass())));
			constructor.getBody().addStatement(new EndExecBlockStatement(false /*reduce*/));
		}
	}
	
	@Override
	public void execute(List<Task> taskGroup) throws Exception {
		if (Main.infoPS != null) 
		{
			Main.infoPS.println("Calling constructor " + constructor.getName() + " (" + taskGroup.size() + ")");
		}
		
		// Execute special constructor inline
		if (!constructor.isDefined())
		{
			constructor.executeSpecialMethod(null, arguments, left, taskGroup);
			return;
		}
		
		parse();
		
		// Set new execution block
		ExecutionBlock constrExecBlock = new ExecutionBlock(constructor.getBody());
		constrExecBlock.parentExecBlock = TaskExecutor.activeExecutionBlock;
		constrExecBlock.brotherExecBlock = null;
		constrExecBlock.isMethodBody = true;
		constrExecBlock.returnDestination = left;
		
		// Set tasks for new ExecutionBlock
		Iterator<Task> taskIter = taskGroup.iterator();
		while(taskIter.hasNext())
		{
			Task task = taskIter.next();
			MethodValuesHolder callingConstrValues = new MethodValuesHolder(task.values, constructor);
			callingConstrValues.addObject(constructor.getParentClass(), "this", true);
			
			// Populate ValuesHolder for method body
			for (int i=0; i<arguments.size(); ++i) {
				ObjValue argumentVal = null;
				if (arguments.get(i) == null) continue;
				if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
				else argumentVal = task.values.get(arguments.get(i).name);
				if (argumentVal == null) continue;
				callingConstrValues.put(constructor.getMethParamList().get(i).getName(), argumentVal);
			}
			
			// Hash method input values, in case of recursion.
			callingConstrValues.saveInputMVH_hash();
			if(callingConstrValues.checkForRecursionCycle())
			{
				// Remove this task due to completed recursion cycle.
				//
				if (Main.infoPS != null) Main.infoPS.println("Killing the task due to completed recursion cycle.");
				TaskExecutor.activeExecutionBlock.taskTable.remove(task);
				taskIter.remove();
				continue;
			}
			
			task.values = callingConstrValues;
			task.PC = 0;
		}
		
		if (!taskGroup.isEmpty())
		{
			// Add all tasks to new execution block
			constrExecBlock.taskTable.addAll(taskGroup);
		
			// and remove them from current execution block
			TaskExecutor.activeExecutionBlock.taskTable.removeAll(taskGroup);

			TaskExecutor.activeExecutionBlock = constrExecBlock;
		}
	}
	
//	@Override
//	public void execute(MethodValuesHolder values) throws Exception {
//		if (constructor.isMethodAlreadyOnStack()) return;
//		Method.methCallStack.push(constructor);
//		
//		MethodValuesHolder callingConstrValues = new MethodValuesHolder(values);
//		callingConstrValues.addObject(constructor.getParentClass(), "this", true);
//		
//		if (!constructor.isParsed())
//		{
//			constructor.parseMethod();
//			if (!constructor.isContainsExplThisConstructorCall()) {				
//				Method fieldInitMethod = constructor.getParentClass().getFieldsInitializerMethod();
//				if (fieldInitMethod != null)
//				{
//					List<VariableExec> args = new LinkedList<VariableExec>();
//					MethCallStatement fieldInitMethCall = new MethCallStatement(null, fieldInitMethod, args);
//					fieldInitMethCall.setInitFieldsMethod();
//					constructor.getBody().addStatement(fieldInitMethCall, 0);
//				}
//				
//				if (!constructor.isContainsExplSuperConstructorCall()) {
//					if (constructor.getParentClass().getSuperClass() != null && constructor.getParentClass().getSuperClass().type != null) {
//						Class superClass = (Class)constructor.getParentClass().getSuperClass().type;
//						List<VariableExec> args = new LinkedList<VariableExec>();
//						Field superField = constructor.getParentClass().findField("super");
//						List<String> name = new LinkedList<String>();
//						name.add("this"); name.add("super");
//						ConstructorCallStatement superConstrCall = new ConstructorCallStatement(new VariableExec(name, superField), superClass.findConstructor(args), args);
//						constructor.getBody().addStatement(superConstrCall, 0);
//					}
//				}
//			}
//			else
//			{
//				throw new Exception("Calling this() in constructor isn't supported!");
//			}
//		}
//		
//		//constructor call
//		for (int i=0; i<arguments.size(); ++i) {
//			ObjValue argumentVal = null;
//			if (arguments.get(i) == null) continue;
//			if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
//			else argumentVal = values.get(arguments.get(i).name);
//			if (argumentVal == null) continue;
//			callingConstrValues.put(constructor.getMethParamList().get(i).getName(), argumentVal);
//		}
//			
//		constructor.executeMethod(callingConstrValues);
//		if (left != null) {
//			values.put(left.name, callingConstrValues.get("this"));
//		}
//		
//		Method.methCallStack.pop();
//	}

	@Override
	public void print(StringBuilder sb, String indention) {
		if (left != null) sb.append(indention + left + " = ");


		sb.append(constructor.getName()).append(" (");
		
		for (int i=0; i<arguments.size(); ++i) {
			sb.append(arguments.get(i));
			if (i < arguments.size()-1) sb.append(", ");
		}
		sb.append(")\r\n");
	}
	
}
