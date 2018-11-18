package object;

import java.util.LinkedList;
import java.util.List;

import object.values.ObjValue;
import object.values.ValuesHolder;
import symbol.object.Class;
import symbol.object.Field;
import symbol.object.Method;

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
	
	@Override
	public void execute(ValuesHolder values) throws Exception {
		if (constructor.isMethodAlreadyOnStack()) return;
		Method.methCallStack.push(constructor);
		
		ValuesHolder callingMethValues = new ValuesHolder(null);
		callingMethValues.addObject(constructor.getParentClass(), "this", true);
		
		if (!constructor.isParsed())
		{
			constructor.parseMethod();
			if (!constructor.isContainsExplThisConstructorCall()) {				
				Method fieldInitMethod = constructor.getParentClass().getFieldsInitializerMethod();
				if (fieldInitMethod != null)
				{
					List<VariableExec> args = new LinkedList<VariableExec>();
					MethCallStatement fieldInitMethCall = new MethCallStatement(null, fieldInitMethod, args);
					fieldInitMethCall.setInitFieldsMethod();
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
			
		constructor.executeMethod(callingMethValues);
		if (left != null) {
			values.put(left.name, callingMethValues.get("this"));
		}
		
		Method.methCallStack.pop();
	}

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
