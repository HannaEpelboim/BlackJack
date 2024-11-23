package controller;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.*;

import model.FacadeModel;
import view.FacadeView;

public class Controller {
	/* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */
	
	private static Controller instance;
	
	private Controller() {
	}
	
	public static synchronized Controller getInstance() {
		if (instance == null) {
			instance  = new Controller();
		}
		
		return instance;
	}
	
	/* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */

	public void init() {
		FacadeView facadeView = FacadeView.getInstance();
		facadeView.init();
	}
	
	public void modelInit() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.initComponents();
	}
	
	public void incAposta(int valor, boolean split) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.incAposta(valor, split);
	}
	
	public void distribuiCartas() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.distribuiCartas();
	}
	
	public void hitPlayer() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hitPlayer();
	}
	
	public void hitDealer() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hitDealer();
	}
	
	public void hitSplit() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.hitSplit();
	}
	
	public void finalizaPartida() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.finalizaPartida();
	}
	
	public void surrender() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.surrender();
	}
	
	public boolean checkPoints(String tipo) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		return facadeModel.checkPoints(tipo);
	}
	
	public void splitPlayer() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.splitPlayer();
	}
	
	public boolean podeSplit() {
		FacadeModel facadeModel = FacadeModel.getInstance();
		if (facadeModel.podeSplit()) {
			facadeModel.splitPlayer();
			return true;
		}
		return false;
	}
	
	public void setAlreadyHited(boolean alreadyHited) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setAlreadyHited(alreadyHited);
	}
	
	public void setDealed(boolean bool) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setDealed(bool);
	}
	
	public void setSplited(boolean bool) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setSplited(bool);
	}
	
	public void setAlreadySplited(boolean bool) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setAlreadySplited(bool);
	}
	
	public void setBurstBeforeSplit(boolean bool) {
		FacadeModel facadeModel = FacadeModel.getInstance();
		facadeModel.setBurstBeforeSplit(bool);
	}
	
	public void popupJOption(String frase) {
		JOptionPane.showMessageDialog(null, frase);
	}
	
	public void saveGame() {
	    FacadeModel facadeModel = FacadeModel.getInstance();
	    JFileChooser fileChooser = new JFileChooser();
	    int returnValue = fileChooser.showSaveDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
	            oos.writeObject(facadeModel);
	            popupJOption("Jogo salvo com sucesso!");
	        } catch (IOException e) {
	            popupJOption("Erro ao salvar o jogo: " + e.getMessage());
	        }
	    }
	}

	public void loadGame() {
        FacadeView facadeView = FacadeView.getInstance();
	    JFileChooser fileChooser = new JFileChooser();
	    int returnValue = fileChooser.showOpenDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	            FacadeModel loadedModel = (FacadeModel) ois.readObject();
	            FacadeModel.setInstance(loadedModel);
	            loadedModel.addObserver(facadeView);
	            facadeView.loadGame();
	            popupJOption("Jogo carregado com sucesso!");
	        } catch (IOException | ClassNotFoundException e) {
	            popupJOption("Erro ao carregar o jogo: " + e.getMessage());
	        }
	    }
	}
}
