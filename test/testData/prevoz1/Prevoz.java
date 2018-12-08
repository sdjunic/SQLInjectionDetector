package prevoz;

import java.io.PrintStream;

public abstract class Prevoz {
	
	protected String vrstaPrevoza = null;
	protected String imeVozila = null;
	protected double potrosnja;
	
	public abstract void pomeri();
	
	public double potrtosnjaNaKm() {
		return potrosnja;
	}
	
	public abstract void ispisi(PrintStream ps);
	
	public String getImeVozila() {
		return imeVozila;
	};
}
