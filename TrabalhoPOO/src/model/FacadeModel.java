package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import controller.Controller;

public class FacadeModel extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean endGameFlag = false;
	private Player player;
	private Player dealer;
	private Player split;
	private ArrayList<Carta> baralho;
	private boolean dealed;
	private boolean splited;
	private boolean alreadySplited;
	private boolean alreadyHited;
	private boolean burstBeforeSplit;

	/* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */
	public static void setInstance(FacadeModel instance) {
		FacadeModel.instance = instance;
	}

	private static FacadeModel instance;

	private FacadeModel() {
	}

	public static synchronized FacadeModel getInstance() {
		if (instance == null) {
			instance = new FacadeModel();
		}

		return instance;
	}

	/* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */

	public void initComponents() {
		this.player = new Player();
		this.dealer = new Player();
		this.split = new Player();
		this.baralho = new Carta().criaBaralho();
		this.splited = false;
		this.alreadySplited = false;
		this.alreadyHited = false;
	}

	public void distribuiCartas() {
		player.distribuiCartas(player.getMao(), baralho);
		dealer.distribuiCartas(dealer.getMao(), baralho);
	}

	public int getAposta(String tipo) {
		return player.getAposta(tipo);
	}

	public int getBalance() {
		return player.getBalanco();
	}

	public void hitPlayer() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hit(player.getMao());
		facadeModel.notifica();
	}

	public void hitDealer() {
		while (dealer.somaCartas() < 17) {
			FacadeModel facadeModel = FacadeModel.getInstance();
			facadeModel.hit(dealer.getMao());
			facadeModel.notifica();
		}
	}

	public void hitSplit() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hit(split.getMao());
		facadeModel.notifica();
	}

	public void atualizaBaralho() {
		if (baralho.size() <= 374) {
			Collections.shuffle(baralho);
		}
	}

	public void hit(ArrayList<Carta> mao) {
		FacadeModel facadeModel = FacadeModel.getInstance();

		if (this.baralho.size() == 0) {
			this.baralho = new Carta().criaBaralho();
		}

		mao.add(this.baralho.get(0));
		this.baralho.remove(0);

		facadeModel.atualizaBaralho();
	}

	public void hit(ArrayList<Carta> mao, ArrayList<Carta> baralho) {
		FacadeModel facadeModel = FacadeModel.getInstance();

		mao.add(baralho.get(0));
		baralho.remove(0);

		facadeModel.atualizaBaralho();
	}

	public void incAposta(int valor, boolean split) {
		player.atualizaAposta(valor, split);
	}

	public void atualizaBalanco(String resultado, int valor, boolean split) {
		player.atualizaBalanco(resultado, valor, split);
	}

	// carta.naipe e carta.valor
	// { carta, carta, cart ....} => { naipe, valor, naipe, valor ... }
	public ArrayList<String> exportaCartas(String tipo) {
		ArrayList<String> mao = new ArrayList<String>();

		if (tipo == "Player") {
			for (Carta carta : player.getMao()) {
				mao.add(carta.getNaipe());
				mao.add(carta.getValor());
			}
		} else if (tipo == "Dealer") {
			for (Carta carta : dealer.getMao()) {
				mao.add(carta.getNaipe());
				mao.add(carta.getValor());
			}
		} else if (tipo == "Split") {
			for (Carta carta : split.getMao()) {
				mao.add(carta.getNaipe());
				mao.add(carta.getValor());
			}
		}

		return mao;
	}

	public int getSomaCartas(String tipo) {
		int playerSoma = player.somaCartas();
		int dealerSoma = dealer.somaCartas();
		int splitSoma = split.somaCartas();
		if (tipo == "Player") {
			return playerSoma;
		} else if (tipo == "Split") {
			return splitSoma;
		}

		return dealerSoma;
	}

	public void finalizaPartida() {
		Controller controller = Controller.getInstance();
		String estado = "";
		if (!split.getMao().isEmpty()) {
			if (split.somaCartas() == dealer.somaCartas()) { // Empate v
				controller.popupJOption("SUA DUPLA EMPATOU!!");
				estado = "Empate";
			} else if (split.somaCartas() > 21) { // Player estourou v
				controller.popupJOption("SUA DUPLA E PESSIMA... ELA ESTOUROU!!");
				estado = "Burst";
			} else if (dealer.somaCartas() == 21) { // Dealer blackjack
				controller.popupJOption("SUA DUPLA FOI HUMILHADA!!");
				estado = "Loss";
			} else if (dealer.somaCartas() > 21) { // Dealer estourou v
				controller.popupJOption("O INIMIGO DE SUA DUPLA ESTOUROU");
				estado = "Win";
			} else if (split.somaCartas() == 21) { // Player blackjack v
				controller.popupJOption("SUA DUPLA HUMILHOU SEU INIMIGO");
				estado = "Blackjack";
			} else if (split.somaCartas() < dealer.somaCartas()) { // Player perdao v
				controller.popupJOption("SUA DUPLA PERDEU!");
				estado = "Loss";
			} else if (split.somaCartas() > dealer.somaCartas()) { // Player ganhou v
				controller.popupJOption("SUA DUPLA GANHOU!");
				estado = "Win";
			}
			split.limpaMao();
			player.atualizaBalanco(estado, 0, true);

			if (estado != "Burst" && estado != "Blackjack" && !this.burstBeforeSplit) {
				FacadeModel facadeModel = FacadeModel.getInstance();
				facadeModel.finalizaPartida();
			}
		} else {
			if (player.somaCartas() == dealer.somaCartas()) { // Empate v
				controller.popupJOption("EMPATE!");
				estado = "Empate";
			} else if (player.somaCartas() > 21) { // Player estourou v
				controller.popupJOption("DERROTA! VOCE ESTOUROU!!");
				estado = "Burst";
			} else if (player.somaCartas() == 21) { // Player blackjack v
				controller.popupJOption("VITORIA! VOCE HUMILHOU SEU INIMIGO");
				estado = "Blackjack";
			} else if (dealer.somaCartas() == 21) { // Dealer blackjack
				controller.popupJOption("DERROTA! VOCE FOI HUMILHADO!!");
				estado = "Loss";
			} else if (dealer.somaCartas() > 21) { // Dealer estourou v
				controller.popupJOption("VITORIA! SEU INIMIGO ESTOUROU");
				estado = "Win";
			} else if (player.somaCartas() < dealer.somaCartas()) { // Player perdao v
				controller.popupJOption("DERROTA!");
				estado = "Loss";
			} else if (player.somaCartas() > dealer.somaCartas()) { // Player ganhou v
				controller.popupJOption("VITORIA!");
				estado = "Win";
			}

			player.limpaMao();
			dealer.limpaMao();
			player.atualizaBalanco(estado, 0, false);
		}
	}

	public void surrender() {
		player.limpaMao();
		dealer.limpaMao();
		split.limpaMao();
		player.atualizaBalanco("Surrender", 0, false);
	}

	public boolean checkPoints(String tipo) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		if (tipo == "Player") {
			if (player.somaCartas() >= 21) {
				return true;
			}
		} else if (tipo == "Dealer") {
			if (dealer.somaCartas() >= 21) {
				return true;
			}
		} else if (tipo == "Split") {
			if (split.somaCartas() >= 21) {
				return true;
			}
		}

		return false;
	}

	public boolean podeSplit() {
		if (player.getMao().size() == 2) {
			return ((player.getMao().get(0).getValor() == player.getMao().get(1).getValor())
					&& player.podeApostar(player.getAposta("Player")) && (!this.alreadySplited));
		}
		return false;
	}

	public void splitPlayer() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.divideMao();
	}

	public void setEndGame() {
		this.endGameFlag = true;
	}

	public void unSetEndGame() {
		this.endGameFlag = false;
	}

	public boolean getEndGame() {
		return endGameFlag;
	}

	public boolean isDealed() {
		return dealed;
	}

	public void setDealed(boolean bool) {
		this.dealed = bool;
	}

	public boolean isSplited() {
		return splited;
	}

	public void setSplited(boolean bool) {
		this.splited = bool;
	}

	public boolean isAlreadySplited() {
		return alreadySplited;
	}

	public void setAlreadySplited(boolean bool) {
		this.alreadySplited = bool;
	}

	public boolean isAlreadyHited() {
		return alreadyHited;
	}

	public void setAlreadyHited(boolean bool) {
		this.alreadyHited = bool;
	}

	public void divideMao() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hit(split.getMao(), player.getMao());
		facadeModel.notifica();
	}

	@SuppressWarnings("deprecation")
	void notifica() {
		setChanged();
		notifyObservers();
	}

	public boolean isBurstBeforeSplit() {
		return burstBeforeSplit;
	}

	public void setBurstBeforeSplit(boolean burstBeforeSplit) {
		this.burstBeforeSplit = burstBeforeSplit;
	}

}
