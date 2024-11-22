package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class CartaTest {

    private Carta carta;

    @Before
    public void setUp() {
        carta = new Carta();
    }

    @Test
    public void testaGeteSet() {
        Carta carta = new Carta("h", "4"); // "h" é o naipe "Copas" no modelo atual
        assertEquals("h", carta.getNaipe());
        assertEquals("4", carta.getValor());

        carta.setNaipe("d"); // "d" representa "Ouros"
        carta.setValor("2");
        assertEquals("d", carta.getNaipe());
        assertEquals("2", carta.getValor());
    }


    
    @Test
    public void testCriaBaralho() {
        Carta carta = new Carta();
        ArrayList<Carta> baralho = carta.criaBaralho();

        // Verifica se o baralho não é nulo
        assertNotNull(baralho);

        // Verifica se o baralho contém 416 cartas
        assertEquals(416, baralho.size());

        // Verifica se há 8 repetições de cada carta no baralho
        for (int i = 0; i < baralho.size(); i++) {
            Carta cartaAtual = baralho.get(i);
            int count = 0;
            for (Carta c : baralho) {
                if (c.getNaipe().equals(cartaAtual.getNaipe()) &&
                    c.getValor().equals(cartaAtual.getValor())) {
                    count++;
                }
            }
            assertEquals(8, count); // Cada carta deve aparecer exatamente 8 vezes
        }

        // O baralho embaralhado deve ser diferente do ordenado
        ArrayList<Carta> baralhoOrdenado = new Carta().criaBaralho();
        Collections.sort(baralhoOrdenado, new java.util.Comparator<Carta>() {
            @Override
            public int compare(Carta c1, Carta c2) {
                int naipeComp = c1.getNaipe().compareTo(c2.getNaipe());
                if (naipeComp != 0) {
                    return naipeComp;
                }
                return c1.getValor().compareTo(c2.getValor());
            }
        });

        boolean embaralhado = false;
        for (int i = 0; i < baralho.size(); i++) {
            if (!baralho.get(i).equals(baralhoOrdenado.get(i))) {
                embaralhado = true;
                break;
            }
        }
        assertTrue(embaralhado); // Verifica se o baralho está embaralhado
    }


    @Test
    public void testCarta() {
        Carta carta1 = new Carta("s", "k"); // "s" é "Espadas", "k" é "Rei"
        Carta carta2 = new Carta("d", "j"); // "d" é "Ouros", "j" é "Valete"
        Carta carta3 = new Carta("h", "6"); // "h" é "Copas"
        Carta carta4 = new Carta("h", "3");
        Carta carta5 = new Carta("c", "Inexistente"); // "c" é "Paus"
        Carta carta6 = new Carta("Inexistente", "2");

        assertEquals("k", carta1.getValor());
        assertEquals("s", carta1.getNaipe());

        assertEquals("j", carta2.getValor());
        assertEquals("d", carta2.getNaipe());

        assertEquals("6", carta3.getValor());
        assertEquals("h", carta3.getNaipe());

        assertEquals("3", carta4.getValor());
        assertEquals("h", carta4.getNaipe());

        assertEquals("Inexistente", carta5.getValor());
        assertEquals("c", carta5.getNaipe());

        assertEquals("2", carta6.getValor());
        assertEquals("Inexistente", carta6.getNaipe());
    }
}