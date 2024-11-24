package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

class Popup extends JFrame{
	JButton newGame;
	JButton loadGame;
	/* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */
	
	private static Popup instance;
	
	private Popup() {
	}
	
	public static synchronized Popup getInstance() {
		if (instance == null) {
			instance  = new Popup();
		}
	
		return instance;
	}
	
	/* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */
	
	public void close() {
		WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
	}
	
	public void init() {
		Popup popup = Popup.getInstance();
		popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popup.setResizable(false);
		popup.addButtons();
		popup.pack();
		popup.setLocationRelativeTo(null);
		popup.setVisible(true);
		
	}
	
	public void addButtons() {
		final FacadeView facadeView = FacadeView.getInstance();
		final Popup popup = Popup.getInstance();
		JPanel panel = new JPanel() {
		    @Override
		    public Dimension getPreferredSize() {
		        return new Dimension(150, 75);
		    };
		};
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		loadGame = new JButton("Load Game");
		loadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller controller = Controller.getInstance();
				controller.loadGame();
			}
		});
		newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.setVisible(false);
				facadeView.gameInit();
			}
		});
		
		panel.add(loadGame);
		panel.add(newGame);
		
		popup.add(panel);
	}
	
	public void deleteFrame() {
		removeAll();
		dispose();
	}
}
