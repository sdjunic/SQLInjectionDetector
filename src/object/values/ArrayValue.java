package object.values;

import symbol.object.Class;

public class ArrayValue extends ObjValue {

	ObjValue element;
	
	public ArrayValue(boolean isSafe, ObjValue element) {
		super(isSafe);
		this.element = element;
	}

	@Override
	public Class getObjectType() {
		return element.getObjectType();
	}

	@Override
	public ObjValue shallowCopy() {
		return new ArrayValue(this.isSafe(), this.element.shallowCopy());
	}
	
	@Override
	public void setSafe(boolean isSafe) {
		super.setSafe(isSafe);
		element.setSafe(isSafe);
	}

}
