package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Fichas {
    private ArrayList<Ficha> fichas = new ArrayList<>();

    /* vvvvvvvvv EXEMPLO SINGLETON vvvvvvvvv */

    private static Fichas instance;

    private Fichas() {
        try {
            // Carregar imagens e criar objetos Ficha
            BufferedImage ficha1Img = ImageIO.read(new File("src/fichasImg/ficha_1$.png"));
            fichas.add(new Ficha(ficha1Img, 35, 250, 60, 60));

            BufferedImage ficha5Img = ImageIO.read(new File("src/fichasImg/ficha_5$.png"));
            fichas.add(new Ficha(ficha5Img, 35, 350, 60, 60));

            BufferedImage ficha10Img = ImageIO.read(new File("src/fichasImg/ficha_10$.png"));
            fichas.add(new Ficha(ficha10Img, 35, 450, 60, 60));

            BufferedImage ficha20Img = ImageIO.read(new File("src/fichasImg/ficha_20$.png"));
            fichas.add(new Ficha(ficha20Img, 130, 250, 60, 60));

            BufferedImage ficha50Img = ImageIO.read(new File("src/fichasImg/ficha_50$.png"));
            fichas.add(new Ficha(ficha50Img, 130, 350, 60, 60));

            BufferedImage ficha100Img = ImageIO.read(new File("src/fichasImg/ficha_100$.png"));
            fichas.add(new Ficha(ficha100Img, 130, 450, 60, 60));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static synchronized Fichas getInstance() {
        if (instance == null) {
            instance = new Fichas();
        }
        return instance;
    }

    /* ^^^^^^^^^ EXEMPLO SINGLETON ^^^^^^^^^ */

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    // Classe interna para representar cada ficha
    public class Ficha {
        private BufferedImage image;
        private int x, y, width, height;

        public Ficha(BufferedImage image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public BufferedImage getImage() {
            return image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
