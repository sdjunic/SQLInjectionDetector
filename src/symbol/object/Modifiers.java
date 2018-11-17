package symbol.object;

public class Modifiers {
	
	public static final int OTHER_MOD_NUM = 8;
	
	public enum Modifier {
		STATIC, ABSTRACT, FINAL, NATIVE, SYNCHRONIZED, TRANSIENT, VOLATILE, STRICTFP, PUBLIC, PROTECTED, PRIVATE, PACKAGE;
		
		@Override
		public String toString(){
			if (name().equals("PACKAGE")) return "";
			else return name();
		}
	}
	
	private Modifier accessLevelModifier;
	private boolean otherModifiers[]; 
	
	
	public Modifiers(){
		accessLevelModifier = Modifier.PACKAGE;
		otherModifiers = new boolean[OTHER_MOD_NUM];
		for (int i=0; i<OTHER_MOD_NUM; ++i){
			otherModifiers[i] = false;
		}
	}
	
	public Modifiers(Modifier m){
		this();
		addModifier(m);
	}
	
	public void addModifier(Modifier m){
		if(m.ordinal() >= OTHER_MOD_NUM)
		{
			accessLevelModifier = m;
		} else {
			otherModifiers[m.ordinal()] = true;
		}
	}
	
	public boolean haveModifier(Modifier m){
		if(m.ordinal() >= OTHER_MOD_NUM)
		{
			return accessLevelModifier == m;
		} else {
			return otherModifiers[m.ordinal()];
		}
	}
	
	@Override
	public String toString(){
		String modifiers = accessLevelModifier.toString().toLowerCase();
		for (int i=0; i<8; ++i){
			if (otherModifiers[i]) {
				modifiers += (modifiers.length() == 0 ? "" : " ") + Modifier.values()[i].toString().toLowerCase();
			}
		}
		return modifiers;
	}

}
