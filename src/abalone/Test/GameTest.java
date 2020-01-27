package abalone.Test;

import static org.junit.Assert.assertThat;

import abalone.Game.Game;
import abalone.Game.Player;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;


public class GameTest {
    private Game game;
    private Player[] players;

    @Test
    public void printOutcomeTest2() {
        players = new Player[2];
        game = new Game(players);
        game.getBoard().setup(2);

        for (int i = 0; i <= 5; i++) {
            game.getBoard().increaseScore(1);
        }
        assertThat(game.printOutcome(), CoreMatchers.containsString("White"));

    }

    @Test
    public void printOutcomeTest3() {
        players = new Player[3];
        game = new Game(players);
        game.getBoard().setup(3);

        for (int i = 0; i <= 4; i++) {
            game.getBoard().increaseScore(1);
        }
        for (int j = 0; j <= 4; j++) {
            game.getBoard().increaseScore(3);
        }

        assertThat(game.printOutcome(), CoreMatchers.containsString("draw"));

    }

    @Test
    public void printOutcomeTest4() {
        players = new Player[4];
        game = new Game(players);
        game.getBoard().setup(4);

        for (int i = 0; i <= 5; i++) {
            game.getBoard().increaseScore(1);
        }
        assertThat(game.printOutcome(), CoreMatchers.containsString("Blue"));

    }
}