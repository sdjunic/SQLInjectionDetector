package prevoz.kopno;

import prevoz.*;

import java.io.PrintStream;

public class Automobil extends Prevoz {
	
	private int brSedista;
	private String marka = null;
	public static final int BR_TOCKOVA = 4;
	
	public Automobil(int brSedista, String marka, double potrosnja){
		super.vrstaPrevoza = "Automobil";
		super.potrosnja = potrosnja;
		this.brSedista = brSedista;
		this.marka = marka;
	}
	
	public Automobil(String marka){
		this(5, marka, 7);
	}
	
	public int getBrSedista(){
		return brSedista;
	}
	
	public boolean setBrSedista(int brSedista){
		if (brSedista > 6 || brSedista < 1) return false;
		this.brSedista = brSedista;
		return true;
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
		return "Automobol - marka - " + this.marka.toString();
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

