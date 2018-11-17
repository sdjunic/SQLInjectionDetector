package prevoz.voda;

import prevoz.*;

import java.io.PrintStream;

public class Camac extends Prevoz {
	
	private int brSedista;
	private int mestaZaStapove;
	private String marka = null;
	public static final int BR_TOCKOVA = 4;
	
	public Camac(int brSedista, int mestaZaStapove, String marka, double potrosnja){
		super.vrstaPrevoza = "Camac";
		super.potrosnja = potrosnja;
		this.brSedista = brSedista;
		this.mestaZaStapove = mestaZaStapove;
		this.marka = marka;
	}
	
	public Camac(String marka){
		this(5, 0, marka, 16);
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
	
	public int getMestaZaStapove() {
		return mestaZaStapove;
	}

	public void setMestaZaStapove(int mestaZaStapove) {
		this.mestaZaStapove = mestaZaStapove;
	}

	@Override
	public String toString(){
		return "Camac - marka - " + this.marka.toString();
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
