package prevoz.kopno;

import prevoz.*;

import java.io.PrintStream;

public class Bicikl extends Prevoz {
	
	private String marka = null;
	public static final int BR_TOCKOVA = 2;
	
	public Bicikl(String marka){
		super.vrstaPrevoza = "Biciklo";
		super.potrosnja = 0;
		this.marka = marka;
	}
	
	public Bicikl(String marka, String ime){
		super.vrstaPrevoza = "Biciklo";
		super.potrosnja = 0;
		super.imeVozila = ime;
		this.marka = marka;
	}
	
	public static int getBrTockova(){
		return BR_TOCKOVA;
	}
	
	public boolean setMarka(String marka){
		if (marka == null) return false;
		else this.marka = marka;
		return true;
	}
	
	public String getMarka(){
		return this.marka;
	}
	
	
	@Override
	public String toString(){
		return "Biciklo - marka - " + this.marka.toString();
	}

	@Override
	public void pomeri() {
		// TODO Auto-generated method stub
	}

	@Override
	public void ispisi(PrintStream ps) {
		ps.println(this.toString());
	}
	
}

