package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.FacadeModel;

class JanelaPrincipal extends JFrame implements Serializable {
	private static final long serialVersionUID = 1L;
	private BufferedImage backgroundImg;
	private JLabel balanceLabel;
	private JLabel betLabel;
	private JButton saveGameButton;

	/* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */
	private static JanelaPrincipal instance;

	private JanelaPrincipal() {
	}

	public static synchronized JanelaPrincipal getInstance() {
		if (instance == null) {
			instance = new JanelaPrincipal();
		}

		return instance;
	}
	/* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */

	public void init() {
		FacadeView facadeView = FacadeView.getInstance();
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		janelaPrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		janelaPrincipal.setResizable(false);

		janelaPrincipal.addImages();
		janelaPrincipal.setLayout(null);

		janelaPrincipal.addLabels();
		janelaPrincipal.addListeners();
		janelaPrincipal.addSaveGameButton();

		facadeView.getPlayerFrame();
		facadeView.getDealerFrame();
		facadeView.getSplitFrame();

		janelaPrincipal.pack();
		janelaPrincipal.setVisible(true);
	}

	public void load() {
		FacadeView facadeView = FacadeView.getInstance();
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		janelaPrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		janelaPrincipal.setResizable(false);

		janelaPrincipal.addImages();
		janelaPrincipal.setLayout(null);

		janelaPrincipal.addLabels();
		janelaPrincipal.addListeners();
		janelaPrincipal.addSaveGameButton();

		facadeView.getPlayerFrame();
		facadeView.getDealerFrame();
		facadeView.getSplitFrame();

		janelaPrincipal.pack();
		janelaPrincipal.setVisible(true);
	}

	public void addSaveGameButton() {
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		saveGameButton = new JButton("Save Game");
		saveGameButton.setBounds(906, 0, 100, 50);
		saveGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = Controller.getInstance();
				controller.saveGame();

			}
		});
		janelaPrincipal.add(saveGameButton);
	}

	public void addImages() {
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		try {
			backgroundImg = ImageIO.read(new File("src/backgroundImg/blackjack.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		janelaPrincipal.setContentPane(new CustomPanel());

	}

	private class CustomPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (backgroundImg != null) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);

				Fichas fichasInstance = Fichas.getInstance();
				for (Fichas.Ficha ficha : fichasInstance.getFichas()) {
					g2d.drawImage(ficha.getImage(), ficha.getX(), ficha.getY(), ficha.getWidth(), ficha.getHeight(),
							null);
				}
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(1006, 761); // Tamanho desejado
		}
	}

	public void endGame() {
		final FacadeView facadeView = FacadeView.getInstance();
		final JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		facadeView.setPopupVisibility(true);
		janelaPrincipal.removeMouseListener(janelaPrincipal.getMouseListeners()[0]);
		facadeView.setDealed(false);
		facadeView.disposePlayerFrame();
		facadeView.disposeDealerFrame();
		facadeView.disposeSplitFrame();
		janelaPrincipal.dispatchEvent(new WindowEvent(janelaPrincipal, WindowEvent.WINDOW_CLOSING));
		janelaPrincipal.dispose();

	}

	public void addListeners() {
		final PlayerFrame playerFrame = PlayerFrame.getInstance();
		final FacadeView facadeView = FacadeView.getInstance();
		final Controller controller = Controller.getInstance();
		final JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		final SplitFrame splitFrame = SplitFrame.getInstance();
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (x >= 50 && x <= 150 && y >= 600 && y <= 650) {
					System.out.println("Exit");
					facadeView.setPopupVisibility(true);
					janelaPrincipal.removeMouseListener(janelaPrincipal.getMouseListeners()[0]);
					facadeView.setDealed(false);
					facadeView.disposePlayerFrame();
					facadeView.disposeDealerFrame();
					facadeView.disposeSplitFrame();
					janelaPrincipal.dispatchEvent(new WindowEvent(janelaPrincipal, WindowEvent.WINDOW_CLOSING));
					janelaPrincipal.dispose();
				}
				if (x >= 870 && x <= 970 && y >= 600 && y <= 650) {
					if (facadeView.isDealed()) {
						System.out.println("Hit");
						if (!facadeView.isSplited()) {
							facadeView.hitPlayer();
							facadeView.setAlreadyHited(true);
							if (controller.checkPoints("Player")) {
								facadeView.finalizaPartida();
								janelaPrincipal.canDeal();
							}
						} else {
							facadeView.hitSplit();
							if (controller.checkPoints("Split")) {
								if(facadeView.isBurstBeforeSplit()) {
									janelaPrincipal.canDeal();
								}
								facadeView.setSplited(false);
								facadeView.finalizaPartida();
							}
						}
					}
				}
				if (x >= 870 && x <= 970 && y >= 670 && y <= 700) {
					if (facadeView.isDealed()) {
						System.out.println("Stand");
						if (facadeView.isSplited()) {
							if(facadeView.isBurstBeforeSplit()) {
								facadeView.hitDealer();
								facadeView.finalizaPartida();
								janelaPrincipal.canDeal();
							}
							facadeView.setSplited(false);
						} else {
							facadeView.hitDealer();
							facadeView.finalizaPartida();
							janelaPrincipal.canDeal();
						}
					}
				}
				if (x >= 870 && x <= 970 && y >= 740 && y <= 790) {
					if (facadeView.isDealed() && !facadeView.isAlreadySplited() && !facadeView.isAlreadyHited()) {
						System.out.println("Sunder");
						facadeView.surrender();
						janelaPrincipal.canDeal();
					}
				}
				if (x >= 680 && x <= 800 && y >= 730 && y <= 790) {
					if (!facadeView.isDealed() && facadeView.getAposta("Player") >= 50) {
						System.out.println("Deal");
						facadeView.distribuiCartas();
						facadeView.setDealed(true);
						if (controller.checkPoints("Player")) {
							facadeView.finalizaPartida();
							janelaPrincipal.canDeal();
						} else {
							if (controller.checkPoints("Dealer")) {
								facadeView.finalizaPartida();
								janelaPrincipal.canDeal();
							}
						}
					}
				}
				if (x >= 530 && x <= 650 && y >= 730 && y <= 790) {
					System.out.println("Clear");
					janelaPrincipal.removeMouseListener(janelaPrincipal.getMouseListeners()[0]);
					facadeView.setDealed(false);
					facadeView.disposePlayerFrame();
					facadeView.disposeDealerFrame();
					facadeView.disposeSplitFrame();
					janelaPrincipal.dispatchEvent(new WindowEvent(janelaPrincipal, WindowEvent.WINDOW_CLOSING));
					janelaPrincipal.dispose();
					facadeView.gameInit();

				}
				if (x >= 380 && x <= 500 && y >= 730 && y <= 790) {
					if (!facadeView.isAlreadySplited()) {
						System.out.println("Split");
						if (controller.podeSplit()) {
							facadeView.incAposta(facadeView.getAposta("Player"), true);
							splitFrame.setVisible(true);
							facadeView.hitSplit();
							facadeView.hitPlayer();
							facadeView.setAlreadySplited(true);
							facadeView.setSplited(true);
							if (controller.checkPoints("Split")) {
								if (controller.checkPoints("Player")) {
									janelaPrincipal.canDeal();
								}
								facadeView.finalizaPartida();
								facadeView.setSplited(false);
								splitFrame.deleteFrame();
								facadeView.getSplitFrame();
								splitFrame.setVisible(false);
								System.out.println(facadeView.getSomaCarta("Split"));
								System.out.println(facadeView.getSomaCarta("Player"));
								
							} else {
								if (controller.checkPoints("Player")) {
									System.out.println("Player algo");
									facadeView.setBurstBeforeSplit(true);
								}
							}
						}
					}
				}
				if (x >= 230 && x <= 350 && y >= 730 && y <= 790) {
					if (facadeView.isDealed()) {
						System.out.println("Double");
						if (!facadeView.isSplited()) {
							facadeView.incAposta(facadeView.getAposta("Player"), false);
							facadeView.hitPlayer();
							if (controller.checkPoints("Player")) {
								facadeView.finalizaPartida();
								janelaPrincipal.canDeal();
							} else {
								facadeView.hitDealer();
								if (controller.checkPoints("Dealer")) {
									janelaPrincipal.canDeal();
								}
								facadeView.finalizaPartida();
							}
						}

					} else {
						facadeView.incAposta(facadeView.getAposta("Split"), true);
						facadeView.hitSplit();
						if (controller.checkPoints("Split")) {
							facadeView.setSplited(false);
						}
						facadeView.setSplited(false);
					}
				}

				if (x >= 35 && x <= 35 + 60 && y >= 280 && y <= 280 + 60) {
					if (!facadeView.isDealed()) {
						if (SwingUtilities.isRightMouseButton(e)) {
							System.out.println("Ficha1");
							facadeView.incAposta(-1, false);
						} else {
							facadeView.incAposta(1, false);
						}
					}
				}
				if (x >= 35 && x <= 35 + 60 && y >= 380 && y <= 380 + 60) {
					if (!facadeView.isDealed()) {
						System.out.println("Ficha5");
						if (SwingUtilities.isRightMouseButton(e)) {
							facadeView.incAposta(-5, false);
						} else {
							facadeView.incAposta(5, false);
						}
					}
				}
				if (x >= 35 && x <= 35 + 60 && y >= 480 && y <= 480 + 60) {
					if (!facadeView.isDealed()) {
						System.out.println("Ficha10");
						if (SwingUtilities.isRightMouseButton(e)) {
							facadeView.incAposta(-10, false);
						} else {
							facadeView.incAposta(10, false);
						}
					}
				}
				if (x >= 130 && x <= 130 + 60 && y >= 280 && y <= 280 + 60) {
					if (!facadeView.isDealed()) {
						System.out.println("Ficha20");
						if (SwingUtilities.isRightMouseButton(e)) {
							facadeView.incAposta(-20, false);
						} else {
							facadeView.incAposta(20, false);
						}
					}
				}
				if (x >= 130 && x <= 130 + 60 && y >= 380 && y <= 380 + 60) {
					if (!facadeView.isDealed()) {
						System.out.println("Ficha50");
						if (SwingUtilities.isRightMouseButton(e)) {
							facadeView.incAposta(-50, false);
						} else {
							facadeView.incAposta(50, false);
						}
					}
				}
				if (x >= 130 && x <= 130 + 60 && y >= 480 && y <= 480 + 60) {
					if (!facadeView.isDealed()) {
						System.out.println("Ficha100");
						if (SwingUtilities.isRightMouseButton(e)) {
							facadeView.incAposta(-100, false);
						} else {
							facadeView.incAposta(100, false);
						}
					}
				}
			}
		};

		janelaPrincipal.addMouseListener(mouseAdapter);

	}

	public void addLabels() {
		JanelaPrincipal janelaPrincipal = JanelaPrincipal.getInstance();
		this.betLabel = new JLabel(" R$");
		betLabel.setFont(new Font("Arial", Font.BOLD, 18));
		betLabel.setBounds(16, 670, 150, 30);
		betLabel.setBackground(Color.WHITE);
		betLabel.setOpaque(true);

		this.balanceLabel = new JLabel(" R$");
		balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
		balanceLabel.setBounds(16, 715, 150, 30);
		balanceLabel.setBackground(Color.WHITE);
		balanceLabel.setOpaque(true);

		janelaPrincipal.add(balanceLabel);
		janelaPrincipal.add(betLabel);
	}

	public void updateLabels() {
		FacadeView facadeView = FacadeView.getInstance();
		this.betLabel.setText(" R$ " + facadeView.getAposta(""));
		this.balanceLabel.setText(" R$ " + facadeView.getBalance());
	}

	public void canDeal() {
		System.out.println("Opa");
		final SplitFrame splitFrame = SplitFrame.getInstance();
		final FacadeView facadeView = FacadeView.getInstance();
		facadeView.setBurstBeforeSplit(false);
		facadeView.setDealed(false);
		facadeView.setAlreadySplited(false);
		facadeView.setAlreadyHited(false);
		splitFrame.setVisible(false);
	}
}
