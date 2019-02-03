package javaLibrary;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import execution.Task;
import object.VariableExec;
import object.values.ObjValue;

public class GetDataNode implements ActionTreeNode {

	public static final String INVERT = "!";
	
	private boolean invertValue = false;
	
	private String argument = null;
	private boolean value;
	
	public GetDataNode(String argument) {	
		argument = argument.trim();
		
		if (argument.startsWith(INVERT))
		{
			invertValue = true;
			argument = argument.substring(1);
		}
		
		if (argument.equals(SpecialAction.SAFE))
		{
			argument = null;
			value = true;
			return;
		}
		
		if (argument.equals(SpecialAction.UNSAFE))
		{
			argument = null;
			value = false;
			return;
		}
		
		assert (argument.startsWith(SpecialAction.THIS) || (!argument.isEmpty() && isNumeric(argument)));
		
		this.argument = argument;
	}

	@Override
	public boolean execute(VariableExec thisObj, List<VariableExec> actualArgs, Task task) {
		
		if (argument == null)
		{
			return invertValue ? !value : value;
		}
		
		boolean result = getSafetyValue(this.argument, thisObj, actualArgs, task);
		
		return invertValue ? !result : result;
	}
	
	public static boolean getSafetyValue(String specArgument, VariableExec thisObj, List<VariableExec> actualArgs, Task task)
	{
		ObjValue object = getObject(specArgument, thisObj, actualArgs, task);
		assert object != null;
		return object.isSafe();
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Integer.parseInt(str);
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static ObjValue getObject(String specArgument, VariableExec thisObj, List<VariableExec> actualArgs, Task task)
	{
		List<String> fullArgName = getFullName(specArgument, thisObj, actualArgs);
		
		
		if (fullArgName.size() == 1 && isNumeric(fullArgName.get(0)))
		{
			int argIndex = Integer.parseInt(fullArgName.get(0));
			VariableExec actualArg = actualArgs.get(argIndex);
			
			assert actualArg.name == null;
			return actualArg.value;
		}
		else
		{
			return task.values.get(fullArgName);
		}
	}
	
	public static List<String> getFullName(String specArgument, VariableExec thisObj, List<VariableExec> actualArgs)
	{
		List<String> fullArgName = new LinkedList<>();
		fullArgName.addAll(Arrays.asList(specArgument.split("\\.")));

		if (specArgument.startsWith(SpecialAction.THIS))
		{
			assert thisObj.value == null;
			
			fullArgName.remove(0);
			fullArgName.addAll(0, thisObj.name);
		}
		else if (isNumeric(fullArgName.get(0)))
		{
			int argIndex = Integer.parseInt(fullArgName.get(0));
			VariableExec actualArg = actualArgs.get(argIndex);
			assert actualArg != null;
			
			if (actualArg.name != null)
			{
				fullArgName.remove(0);
				fullArgName.addAll(0, actualArg.name);	
			}
			else
			{
				assert fullArgName.size() == 1;
			}
		}
		else
		{
			assert false;
		}
		
		return fullArgName;
	}

}
