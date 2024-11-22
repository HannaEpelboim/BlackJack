package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(); // Usa o construtor padrão que inicializa o saldo com 2400
    }

    @Test
    public void testBalanco() {
        Player p1 = new Player(); // Saldo inicial 2400
        Player p2 = new Player(); 
        Player p3 = new Player();
        Player p4 = new Player();
        Player p5 = new Player(); 
        Player p6 = new Player();
        Player p7 = new Player();
        Player p8 = new Player();
        Player p9 = new Player();
        
        // Teste de Perda
        p1.debitaAposta(100);
        p1.setAposta(100);
        p1.atualizaBalanco("Loss", 0, false); // Perde 100
        assertEquals(2300, p1.getBalanco()); // 2400 - 100 = 2300

        // Teste de Vitória
        p1.debitaAposta(50);
        p1.setAposta(50);
        p1.atualizaBalanco("Win", 0, false); // Ganha o dobro da aposta (50 * 2)
        assertEquals(2350, p1.getBalanco()); // 2250 + 100 = 2350

        // Teste de Blackjack
        p2.debitaAposta(100); // Aposta 100
        p2.setAposta(100);
        p2.atualizaBalanco("Blackjack", 0, false); // Ganha 2.5 vezes a aposta
        assertEquals(2550, p2.getBalanco()); // 2400 - 100 + 250 = 2550

        // Teste de Empate
        p3.debitaAposta(100); // Aposta 100
        p3.setAposta(100);
        p3.atualizaBalanco("Empate", 0, false); // Empata, aposta devolvida
        assertEquals(2400, p3.getBalanco()); // Saldo não muda

        // Teste de Renda (Surrender)
        p4.debitaAposta(100);
        p4.setAposta(100);
        p4.atualizaBalanco("Surrender", 0, false); // Perde metade da aposta
        assertEquals(2350, p4.getBalanco()); // 2400 - 50 = 2350

        // Teste de Perda no Split
        p5.debitaAposta(100); // Débito da aposta
        p5.setApostaSplit(100); // Define aposta no split
        p5.atualizaBalanco("Loss", 0, true); // Perde a aposta no split
        assertEquals(2300, p5.getBalanco()); // 2400 - 100 = 2300

        // Teste de Vitória no Split
        p6.debitaAposta(50); // Débito da aposta
        p6.setApostaSplit(50); // Define aposta no split
        p6.atualizaBalanco("Win", 0, true); // Ganha o dobro da aposta no split
        assertEquals(2450, p6.getBalanco()); // 2400 - 50 + 100 = 2450

        // Teste de Blackjack no Split
        p7.debitaAposta(100); // Débito da aposta
        p7.setApostaSplit(100); // Define aposta no split
        p7.atualizaBalanco("Blackjack", 0, true); // Ganha 2.5 vezes a aposta no split
        assertEquals(2550, p7.getBalanco()); // 2400 - 100 + 250 = 2550

        // Teste de Empate no Split
        p8.debitaAposta(100); // Débito da aposta
        p8.setApostaSplit(100); // Define aposta no split
        p8.atualizaBalanco("Empate", 0, true); // Empate no split
        assertEquals(2400, p8.getBalanco()); // Saldo não muda

        // Teste de Renda (Surrender) no Split
        p9.debitaAposta(100); // Débito da aposta
        p9.setApostaSplit(100); // Define aposta no split
        p9.atualizaBalanco("Surrender", 0, true); // Rende-se no split
        assertEquals(2350, p9.getBalanco()); // 2400 - 100 + 50 = 2350
    }


    @Test
    public void testPodeApostar() {
        Player p1 = new Player();
        assertTrue(p1.podeApostar(10)); // Aposta válida
        assertTrue(p1.podeApostar(2400)); // Aposta igual ao saldo
        assertFalse(p1.podeApostar(3000)); // Aposta maior que o saldo
    }

       
    @Test
    public void testDebitaAposta() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();

        p1.debitaAposta(10);
        assertEquals(2390, p1.getBalanco()); // 2400 - 10

        p2.debitaAposta(2400);
        assertEquals(0, p2.getBalanco()); // Saldo zerado

        p3.debitaAposta(3000); // Débito maior que o saldo
        assertEquals(-600, p3.getBalanco()); // Saldo negativo
    }

    @Test
    public void testCreditaGanho() {
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();

        p1.creditaGanho(10);
        assertEquals(2410, p1.getBalanco()); // 2400 + 10

        p2.creditaGanho(2400);
        assertEquals(4800, p2.getBalanco()); // 2400 + 2400

        p3.creditaGanho(3000);
        assertEquals(5400, p3.getBalanco()); // 2400 + 3000
    }
    
    @Test
    public void testSomaCartasSemAs() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "5")); // 5 de Copas
        mao.add(new Carta("d", "10")); // 10 de Ouros
        player.setMao(mao);

        assertEquals(15, player.somaCartas()); // Soma deve ser 5 + 10 = 15
    }

    @Test
    public void testSomaCartasComUmAs() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás de Copas
        mao.add(new Carta("s", "8")); // 8 de Espadas
        player.setMao(mao);

        assertEquals(19, player.somaCartas()); // Soma deve ser 11 + 8 = 19
    }

    @Test
    public void testSomaCartasComDoisAsesAjuste() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás de Copas
        mao.add(new Carta("d", "a")); // Ás de Ouros
        mao.add(new Carta("s", "9")); // 9 de Espadas
        player.setMao(mao);

        assertEquals(21, player.somaCartas()); // Ajusta para 11 + 1 + 9 = 21
    }

    @Test
    public void testSomaCartasComTresAsesAjuste() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás de Copas
        mao.add(new Carta("d", "a")); // Ás de Ouros
        mao.add(new Carta("s", "a")); // Ás de Espadas
        player.setMao(mao);

        assertEquals(13, player.somaCartas()); // Ajusta para 11 + 1 + 1 = 13
    }

    @Test
    public void testSomaCartasComFaceCards() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "k")); // Rei de Copas
        mao.add(new Carta("d", "q")); // Rainha de Ouros
        mao.add(new Carta("s", "j")); // Valete de Espadas
        player.setMao(mao);

        assertEquals(30, player.somaCartas()); // 10 + 10 + 10 = 30
    }

    @Test
    public void testSomaCartasComAsEFaceCards() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás de Copas
        mao.add(new Carta("d", "k")); // Rei de Ouros
        mao.add(new Carta("s", "j")); // Valete de Espadas
        player.setMao(mao);

        assertEquals(21, player.somaCartas()); // Ajusta para 11 + 10 + 10 = 21
    }

    @Test
    public void testSomaCartasComNumerosAltos() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "9")); // 9 de Copas
        mao.add(new Carta("d", "8")); // 8 de Ouros
        mao.add(new Carta("s", "5")); // 5 de Espadas
        player.setMao(mao);

        assertEquals(22, player.somaCartas()); // 9 + 8 + 5 = 22 (estoura)
    }

    @Test
    public void testSomaCartasComNumerosEAsAjustados() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás de Copas
        mao.add(new Carta("d", "9")); // 9 de Ouros
        mao.add(new Carta("s", "5")); // 5 de Espadas
        player.setMao(mao);

        assertEquals(15, player.somaCartas()); // Ajusta para 1 + 9 + 5 = 15
    }
}