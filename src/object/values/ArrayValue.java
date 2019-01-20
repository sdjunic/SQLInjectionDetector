package object.values;

import symbol.object.ArrayType;
import symbol.object.Class;
import symbol.object.Type;

public class ArrayValue extends ObjValue {

	ObjValue element;
	ArrayType type;
	
	public ArrayValue(boolean isSafe, ObjValue element, ArrayType type) {
		super(isSafe);
		this.element = element;
		this.type = type;
		
		assert element.getObjectType().equals(type.getType().type);
	}

	@Override
	public Type getObjectType() {
		return element.getObjectType();
	}

	@Override
	public ObjValue shallowCopy() {
		return new ArrayValue(this.isSafe(), this.element.shallowCopy(), type);
	}
	
	@Override
	public void setSafe(boolean isSafe) {
		super.setSafe(isSafe);
		element.setSafe(isSafe);
	}

}