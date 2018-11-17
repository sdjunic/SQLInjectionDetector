package symbol.object;

public class MethParam extends Variable {
	
	private int ordinal;
	
	public MethParam(TypeReference type, String name){
		super(name, type);
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	
}
