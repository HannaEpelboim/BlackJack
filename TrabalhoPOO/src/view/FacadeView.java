package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.Controller;
import model.FacadeModel;

public class FacadeView implements Observer {
	/* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */

	private static FacadeView instance;

	private FacadeView() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.addObserver(this);
	}

	public static synchronized FacadeView getInstance() {
		if (instance == null) {
			instance = new FacadeView();
		}

		return instance;
	}

	/* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */
	public void init() {
		Popup popup = Popup.getInstance();
		popup.init();
	}

	public void gameInit() {
		Controller controller = Controller.getInstance();
		controller.modelInit();
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		janelaPrincipal.init();
		FacadeView facadeView = FacadeView.getInstance();
		facadeView.atualizaView();
	}

	public void loadGame() {
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		janelaPrincipal.load();
		
		FacadeView facadeView = FacadeView.getInstance();
        facadeView.setPopupVisibility(false);
		facadeView.atualizaView();
	}

	public void setPopupVisibility(boolean flag) {
		Popup popup = Popup.getInstance();
		if (flag) {
			popup.setVisible(true);
		} else {
			popup.setVisible(false);
		}
		popup.revalidate();
		popup.repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Atualiza");
		FacadeView facadeView = FacadeView.getInstance();
		facadeView.atualizaView();

	}

	public void atualizaView() {
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		FacadeModel facadeModel = FacadeModel.getInstance();
		PlayerFrame playerFrame = PlayerFrame.getInstance();
		DealerFrame dealerFrame = DealerFrame.getInstance();
		SplitFrame splitFrame = SplitFrame.getInstance();

		janelaPrincipal.updateLabels();
		playerFrame.updateCartas("Player");
		playerFrame.updatePontos();
		SwingUtilities.updateComponentTreeUI(playerFrame);

		dealerFrame.updateCartas("Dealer");
		dealerFrame.updatePontos();
		SwingUtilities.updateComponentTreeUI(dealerFrame);

		splitFrame.updateCartas("Split");
		splitFrame.updatePontos();
		if (facadeModel.getSomaCartas("Split") == 0) {
			splitFrame.setVisible(false);
		} else {
			SwingUtilities.updateComponentTreeUI(splitFrame);
		}

		janelaPrincipal.revalidate();
		janelaPrincipal.repaint();

		if (facadeModel.getEndGame()) {
			janelaPrincipal.endGame();
		}
		facadeModel.unSetEndGame();
	}

	public int getAposta(String tipo) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.getAposta(tipo);
	}

	public int getBalance() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.getBalance();
	}

	public void incAposta(int valor, boolean split) {
		Controller controller = Controller.getInstance();
		controller.incAposta(valor, split);
	}

	public PlayerFrame getPlayerFrame() {
		PlayerFrame playerFrame = PlayerFrame.getInstance();
		playerFrame.initComponents();
		return playerFrame;
	}

	public SplitFrame getSplitFrame() {
		SplitFrame splitFrame = SplitFrame.getInstance();
		splitFrame.initComponents();
		return splitFrame;
	}

	public DealerFrame getDealerFrame() {
		DealerFrame dealerFrame = DealerFrame.getInstance();
		dealerFrame.initComponents();
		return dealerFrame;
	}

	public void disposePlayerFrame() {
		PlayerFrame playerFrame = PlayerFrame.getInstance();
		playerFrame.deleteFrame();
	}

	public void disposeDealerFrame() {
		DealerFrame dealerFrame = DealerFrame.getInstance();
		dealerFrame.deleteFrame();
	}

	public void disposeSplitFrame() {
		SplitFrame splitFrame = SplitFrame.getInstance();
		splitFrame.deleteFrame();
	}

	public Rectangle getJanelaPrincipalBounds() {
		return JanelaPrincipal.getInstance().getBounds();
	}

	public void distribuiCartas() {
		Controller controller = Controller.getInstance();
		controller.distribuiCartas();
	}

	public ArrayList<String> importaCarta(String tipo) {
		System.out.println("Importando cartas");
		FacadeModel facadeModel = FacadeModel.getInstance();
		if (tipo == "Player" || tipo == "Dealer" || tipo == "Split") {
			return facadeModel.exportaCartas(tipo);
		}

		return null;
	}

	public void hitPlayer() {
		Controller controller = Controller.getInstance();
		controller.hitPlayer();
	}

	public void hitDealer() {
		Controller controller = Controller.getInstance();
		controller.hitDealer();
	}

	public void hitSplit() {
		Controller controller = Controller.getInstance();
		controller.hitSplit();
	}

	public void finalizaPartida() {
		Controller controller = Controller.getInstance();
		controller.finalizaPartida();
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		janelaPrincipal.canDeal();
	}

	public int getSomaCarta(String tipo) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		int somaCartas = facadeModel.getSomaCartas(tipo);
		if (somaCartas == -1) {
			if (tipo == "Player") {
				System.out.println("Player estoura");
			} else {
				System.out.println("Dealer estoura");
			}
		} else if (somaCartas == -2) {
			if (tipo == "Player") {
				System.out.println("player blackJack");
			} else {
				System.out.println("dealer blackJack");
			}
		}
		return somaCartas;
	}

	public void surrender() {
		Controller controller = Controller.getInstance();
		controller.surrender();
	}

	public boolean isDealed() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.isDealed();
	}

	public void setDealed(boolean dealed) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setDealed(dealed);
	}

	public boolean isSplited() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.isSplited();
	}

	public void setSplited(boolean splited) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setSplited(splited);
	}

	public boolean isAlreadySplited() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.isAlreadySplited();
	}

	public void setAlreadySplited(boolean alreadySplited) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setAlreadyHited(alreadySplited);
	}

	public boolean isAlreadyHited() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.isAlreadyHited();
	}

	public void setAlreadyHited(boolean alreadyHited) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setAlreadyHited(alreadyHited);
	}
}
