package model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class BlackjackTest {

    private Player player;
    private FacadeModel model;

    @Before
    public void setUp() {
        player = new Player();
        model = FacadeModel.getInstance();
        model.initComponents();
    }

    @Test
    public void testSomaCartasComAs() {
        ArrayList<Carta> mao = new ArrayList<>();
        mao.add(new Carta("h", "a")); // Ás
        mao.add(new Carta("d", "9")); // 9
        player.setMao(mao);

        assertEquals(20, player.somaCartas());

        mao.add(new Carta("s", "k")); // Rei
        assertEquals(20, player.somaCartas()); // Ás ajustado para 1
    }

    @Test
    public void testDistribuiCartas() {
        model.distribuiCartas();
        assertEquals(2, model.exportaCartas("Player").size() / 2); // 2 cartas
        assertEquals(2, model.exportaCartas("Dealer").size() / 2); // 2 cartas
    }

    @Test
    public void testAtualizaBalancoComVitoria() {
        player.atualizaAposta(100, false); // Aposta inicial
        player.atualizaBalanco("Win", 0, false);

        assertEquals(2600, player.getBalanco()); // Saldo incrementado
        assertEquals(0, player.getAposta("Player")); // Aposta resetada
    }

    @Test
    public void testPodeApostar() {
        assertTrue(player.podeApostar(100));
        assertFalse(player.podeApostar(3000)); // Excede o saldo
    }
    


    @Test
    public void testFinalizaPartidaEmpate() {
        ArrayList<Carta> maoPlayer = new ArrayList<>();
        maoPlayer.add(new Carta("h", "10"));
        maoPlayer.add(new Carta("d", "10"));

        ArrayList<Carta> maoDealer = new ArrayList<>();
        maoDealer.add(new Carta("c", "10"));
        maoDealer.add(new Carta("s", "10"));

        player.setMao(maoPlayer);

        model.finalizaPartida();

        assertEquals(2400, player.getBalanco()); // Saldo inalterado no empate
    }

    @Test
    public void testFinalizaPartidaComDerrota() {
        ArrayList<Carta> maoPlayer = new ArrayList<>();
        maoPlayer.add(new Carta("h", "10"));
        maoPlayer.add(new Carta("d", "5"));

        ArrayList<Carta> maoDealer = new ArrayList<>();
        maoDealer.add(new Carta("c", "10"));
        maoDealer.add(new Carta("s", "6"));

        player.setMao(maoPlayer);

        model.finalizaPartida();

        assertEquals(2400 - player.getAposta("Player"), player.getBalanco()); // Saldo decrementado
    }

    @Test
    public void testSurrender() {
        player.atualizaAposta(200, false);
        model.surrender();

        assertEquals(2400, player.getBalanco()); // Metade da aposta retornada
    }
}