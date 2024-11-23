package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

class PainelCartas extends JPanel {
    private List<Image> cardImages;

    public PainelCartas() {
        cardImages = new ArrayList<>();
    }

    public void setCardImages(List<Image> images) {
        this.cardImages = images;
        repaint(); // Redesenha o painel com as novas imagens
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int spacing = 5; // Espaçamento entre as cartas
        int cardWidth = 60; // Largura fixa das cartas
        int cardHeight = 90; // Altura fixa das cartas

        int x = spacing;
        int y = spacing;

        for (Image img : cardImages) {
            if (x + cardWidth + spacing > getWidth()) {
                // Quebra de linha
                x = spacing;
                y += cardHeight + spacing;
            }
            g.drawImage(img, x, y, cardWidth, cardHeight, this);
            x += cardWidth + spacing;
        }
    }

}
