package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Controller;

class Player implements Serializable{
    private static final long serialVersionUID = 1L;
	private int aposta;
	private int apostaSplit;
	private ArrayList<Carta> mao = new ArrayList<Carta>();
	private int balance;

	public Player() {
		this.balance = 2400;
		this.aposta = 0;
		this.apostaSplit = 0;
	}

	public int getAposta(String tipo) {
		if(tipo == "Player") {
			return this.aposta;
		}else if(tipo == "Split") {
			return this.apostaSplit;
		}
		
		return this.aposta + this.apostaSplit;
	}

	public void atualizaAposta(int valor, boolean split) {
		if (podeApostar(valor)) {
			if (split) {
				this.apostaSplit = this.apostaSplit + valor;
			} else {
				this.aposta = this.aposta + valor;
			}
			FacadeModel facadeModel = FacadeModel.getInstance();
			facadeModel.atualizaBalanco("Bet", valor, split);
			facadeModel.notifica();
		}
	}

	public int getBalanco() {
		return this.balance;
	}

	public void atualizaBalanco(String gameState, int bet, boolean split) {
		Controller controller = Controller.getInstance();
		FacadeModel facadeModel = FacadeModel.getInstance();
		if (split) {
			switch (gameState) {
			case "Win":
				this.balance += this.apostaSplit * 2;
				this.apostaSplit = 0;
				break;
			case "Surrender":
				this.balance += this.apostaSplit / 2;
				this.apostaSplit = 0;
				break;
			case "Bet":
				this.balance -= bet;
				break;
			case "Blackjack":
				this.balance += this.apostaSplit * 2.5;
				this.apostaSplit = 0;
				break;
			case "Empate":
				this.balance += this.apostaSplit;
				this.apostaSplit = 0;
				break;
			default: // Loss
				this.apostaSplit = 0;
				break;
			}
			
		} else {
			switch (gameState) {
			case "Win":
				this.balance += this.aposta * 2;
				this.aposta = 0;
				break;
			case "Surrender":
				this.balance += this.aposta / 2;
				this.aposta = 0;
				break;
			case "Bet":
				this.balance -= bet;
				break;
			case "Blackjack":
				this.balance += this.aposta * 2.5;
				this.aposta = 0;
				break;
			case "Empate":
				this.balance += this.aposta;
				this.aposta = 0;
				break;
			default: // Loss
				this.aposta = 0;
				if (this.balance < 50) {
					System.out.println("Oi");
					controller.popupJOption("Ah, entÃ£o Ã© verdade que suas economias "
							+ "finalmente se candidataram ao Guinness como o maior fiasco "
							+ "financeiro da histÃ³ria? Que audÃ¡cia fabulosa!");
					facadeModel.setEndGame();
				}

				break;
			}
		}

		facadeModel.notifica();
	}

	public void distribuiCartas(ArrayList<Carta> mao, ArrayList<Carta> baralho) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hit(mao, baralho);
		facadeModel.hit(mao, baralho);
		facadeModel.notifica();
	}

	public int somaCartas() {
		int total = 0;
		int numAses = 0;

		for (Carta carta : this.mao) {
			String valor = carta.getValor();

			switch (valor) {
			case "t":
			case "j":
			case "q":
			case "k":
				total += 10;
				break;
			case "a":
				total += 11;
				numAses++;
				break;
			default:
				total += Integer.parseInt(valor);
			}
		}

		// Ajusta o valor do Ã�s de 11 para 1 se o total ultrapassar 21
		while (total > 21 && numAses > 0) {
			total -= 10; // Subtrai 10 por Ã�s para ajustÃ¡-lo de 11 para 1
			numAses--;
		}

		return total;
	}

	public void exibeMao() {
		for (Carta carta : this.mao) {
			carta.imprimeCarta();
		}
	}

	public void limpaMao() {
		this.mao.removeAll(mao);
	}

	public boolean podeApostar(int valor) {
		if (valor < 0) {
			if (aposta > 0) {
				return true;
			} else {
				return false;
			}
		}

		return 0 <= (this.balance - valor);
	}

	public void debitaAposta(int aposta) {
		this.balance -= aposta;
	}

	public void creditaGanho(int ganho) {
		this.balance += ganho;
	}

	public ArrayList<Carta> getMao() {
		return this.mao;
	}


	public void setAposta(int aposta) {
		this.aposta = aposta;
	}
	
	public void setApostaSplit(int apostaSplit) {
		this.apostaSplit = apostaSplit;
	}

	public void setMao(ArrayList<Carta> novaMao) {
		this.mao = novaMao;
	}

}