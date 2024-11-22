package view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;

class DealerFrame extends JFrame {
    private PainelCartas painelCartas;
    private JLabel labelPontos;

    /* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */
    private static DealerFrame instance;

    private DealerFrame() {
    }

    public static synchronized DealerFrame getInstance() {
        if (instance == null) {
            instance = new DealerFrame();
        }

        return instance;
    }
    /* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */

    public void initComponents() {
        painelCartas = new PainelCartas();
        setLayout(new BorderLayout());
        addLabel();
        add(painelCartas, BorderLayout.CENTER);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        FacadeView facadeView = FacadeView.getInstance();
        setBounds(facadeView.getJanelaPrincipalBounds().x + 1050,
                  facadeView.getJanelaPrincipalBounds().y, 205, 350);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void updateCartas(String tipo) {
        limpaPainel();
        FacadeView facadeView = FacadeView.getInstance();
        ArrayList<String> playerMao = facadeView.importaCarta(tipo);
        ArrayList<String> cartasPlayer = new ArrayList<>();

        for (int i = 0; i < playerMao.size(); i += 2) {
            cartasPlayer.add("src/cartasImg/" + playerMao.get(i + 1) + playerMao.get(i) + ".gif");
        }

        // Carrega as imagens e configura no painel
        List<Image> cardImages = new ArrayList<>();
        for (String caminho : cartasPlayer) {
            System.out.println(caminho);
            try {
                ImageIcon icon = new ImageIcon(caminho);
                Image img = icon.getImage();
                cardImages.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        painelCartas.setCardImages(cardImages);
    }

    public void updatePontos() {
        FacadeView facadeView = FacadeView.getInstance();
        labelPontos.setText("Pontos: " + facadeView.getSomaCarta("Dealer"));
    }

    public void addLabel() {
        labelPontos = new JLabel("Pontos: ", SwingConstants.CENTER);
        labelPontos.setVerticalAlignment(SwingConstants.TOP);
        add(labelPontos, BorderLayout.NORTH);
    }

    public void limpaPainel() {
        painelCartas.setCardImages(null); // Limpa as imagens
    }

    public void deleteFrame() {
        remove(painelCartas);
        remove(labelPontos);
        dispose();
    }
}
