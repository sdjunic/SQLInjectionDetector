package object;

import java.util.List;

import object.values.ObjValue;
import object.values.ValuesHolder;
import symbol.object.Method;

public class ConstructorCallStatement extends CallStatement {

	private Method constructor = null;
	Method fieldInitMethod = null;
	
	public ConstructorCallStatement(VariableExec left, Method constructor, List<VariableExec> arguments) {
		this.left = left;
		this.constructor = constructor;
		this.arguments = arguments;
	}
	
	public Method getConstructor() {
		return constructor;
	}
	
	@Override
	public void execute(ValuesHolder values) throws Exception {
		if (constructor.isMethodAlreadyOnStack()) return;
		Method.methCallStack.push(constructor);
		
		if (fieldInitMethod == null) {
			fieldInitMethod = constructor.getParentClass().getFieldsInitializerMethod();
		}
		ValuesHolder callingMethValues = new ValuesHolder(null);
		
		callingMethValues.addObject(constructor.getParentClass(), "this", true);
		
		//field initialize
		if (fieldInitMethod != null) {
			fieldInitMethod.executeMethod(callingMethValues);
		}
		
		//constructor call
		for (int i=0; i<arguments.size(); ++i) {
			ObjValue argumentVal = null;
			if (arguments.get(i) == null) continue;
			if (arguments.get(i).value != null) argumentVal = arguments.get(i).value;
			else argumentVal = values.get(arguments.get(i).name);
			if (argumentVal == null) continue;
			callingMethValues.put(constructor.getMethParamList().get(i).getName(), argumentVal);
		}
			
		ObjValue returnVal = constructor.executeMethod(callingMethValues);
		if (left != null) {
			values.put(left.name, returnVal);
		}
		
		Method.methCallStack.pop();
	}

	@Override
	public void print(StringBuilder sb) {
		if (left != null) sb.append(left + " = ");


		sb.append(constructor.getName()).append(" (");
		
		for (int i=0; i<arguments.size(); ++i) {
			sb.append(arguments.get(i));
			if (i < arguments.size()-1) sb.append(", ");
		}
		sb.append(")");
		
		if (fieldInitMethod != null) {
			sb.append(" { field_init: ");
			sb.append(fieldInitMethod.getName());
			sb.append(" }");
		}
		sb.append("\r\n");
	}
	
}
