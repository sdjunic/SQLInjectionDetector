package object;

import java.util.*;

import Parse.ParseData;
import object.values.ObjValue;
import object.values.ValuesHolder;
import symbol.object.*;
import symbol.object.Class;

public class MethCallStatement extends CallStatement {

	private String methodToCall = null;
	private VariableExec thisObj = null;
	private boolean isInitFieldsMethod = false;
	
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
	
	public void setInitFieldsMethod()
	{
		isInitFieldsMethod = true;
	}
	
	public List<VariableExec> getArguments() {
		return arguments;
	}

	public void setArguments(List<VariableExec> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public void execute(ValuesHolder values) throws Exception {
		Method methodToCall = staticMethodToCall;
		
		if (isInitFieldsMethod)
		{
			Method.methCallStack.push(methodToCall);
			methodToCall.executeMethod(values);
			Method.methCallStack.pop();
			return;
		}
		
		ValuesHolder callingMethValues = new ValuesHolder(null);
		
		if (methodToCall == null) {
			ObjValue thisObjValue = values.get(thisObj.name);
			Class thisObjClass = thisObjValue.getObjectType();
			if (thisObjClass != null) {
				
				methodToCall = thisObjClass.findMethod(this.methodToCall, this.arguments);

				callingMethValues.put("this", thisObjValue);
				
			} else {
				throw new Exception("Metoda nije pronadjena!");
			}
		}
		
		if (methodToCall.isMethodAlreadyOnStack()) { return; }
		Method.methCallStack.push(methodToCall);
		
		for (int i=0; i<arguments.size(); ++i) {
			ObjValue argumentVal = null;
			if (arguments.get(i) == null) continue;
			if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
			else argumentVal = values.get(arguments.get(i).name);
			if (argumentVal == null) continue;
			callingMethValues.put(methodToCall.getMethParamList().get(i).getName(), argumentVal);
		}
		
		ObjValue returnVal = methodToCall.executeMethod(callingMethValues);
		if (left != null) {
			values.put(left.name, returnVal);
		}
		
		Method.methCallStack.pop();
	}
	
	@Override
	public void print(StringBuilder sb, String indention) {
		if (left != null) sb.append(indention + left + " = ");
		if (thisObj != null) {
			sb.append(ParseData.makeFullNameWithDots(thisObj.name)).append(".");
		}

		if (staticMethodToCall == null) sb.append(methodToCall).append(" (");
		else sb.append(staticMethodToCall.getName()).append(" (");
		
		for (int i=0; i<arguments.size(); ++i) {
			sb.append(arguments.get(i));
			if (i < arguments.size()-1) sb.append(", ");
		}
		sb.append(")\r\n");
	}
	
}
