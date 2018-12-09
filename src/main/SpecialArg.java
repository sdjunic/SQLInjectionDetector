package main;

public class SpecialArg {
	public static final int INDEX_RETURN_OBJ = -2;
	public static final int INDEX_THIS_OBJ = -1;
	//arguments indexes start from 0 
	
	public static final int TYPE_CRITICAL_OUTPUT = 0;
	public static final int TYPE_SAFE_ARG = 1;
	public static final int TYPE_UNSAFE_ARG = 2;
	
	public int index;
	public int type;
	
	public SpecialArg(int index, int type) {	
		assert ((type == TYPE_SAFE_ARG || type == TYPE_UNSAFE_ARG) ? (index == INDEX_RETURN_OBJ) : true);
		this.index = index;
		this.type = type;
	}
	
};
