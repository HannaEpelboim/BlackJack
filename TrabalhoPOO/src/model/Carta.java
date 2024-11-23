package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

class Carta implements Serializable{
    private static final long serialVersionUID = 1L;
	private String naipe;
	private String valor;
	private String[] naipes = { "h", "d", "s", "c" };
	private String[] valores = { "a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k" };
	
	public Carta() {}

	public Carta(String naipe, String valor) {
		this.naipe = naipe;
		this.valor = valor;
	}

	public ArrayList<Carta> criaBaralho() {
		ArrayList<Carta> baralho = new ArrayList<Carta>();

		for (int i = 0; i < 8; i++) {
			for (String naipe : this.naipes) {
				for (String valor : this.valores) {
					baralho.add(new Carta(naipe, valor));
				}
			}
		}
		
		
		Collections.shuffle(baralho);
		
		return baralho;
	}


	public String getValor() {
		return this.valor;
	}

	public String getNaipe() {
		return naipe;
	}

	public void setNaipe(String naipe) {
		this.naipe = naipe;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
}