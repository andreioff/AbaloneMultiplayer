import static org.junit.Assert.assertThat;

import abalone.Board;
import abalone.Marble.Marble;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GameTest {
    private Game newGame1;
    private Game newGame2;
    private Game newGame3;
    private Player[] playerArr2 = new Player[2];
    private Player[] playerArr3 = new Player[3];
    private Player[] playerArr4 = new Player[4];

    /**
     * setUp for the tests.
     */
    @BeforeEach
    public void setUp() {
        newGame1 = new Game(playerArr2);
        newGame2 = new Game(playerArr3);
        newGame3 = new Game(playerArr4);


    }


    @Test
    public void printOutcomeTest2() {
        newGame1.getBoard().setup(2);

        for (int i = 0; i <= 5; i++) {

            newGame1.getBoard().increaseScore(1);
        }
        assertThat(newGame1.printOutcome(), CoreMatchers.containsString("White"));

    }

    @Test
    public void printOutcomeTest3() {
        newGame2.getBoard().setup(3);

        for (int i = 0; i <= 4; i++) {
            newGame2.getBoard().increaseScore(1);
        }
        for (int j = 0; j <= 4; j++) {
            newGame2.getBoard().increaseScore(3);
        }

        assertThat(newGame2.printOutcome(), CoreMatchers.containsString("draw"));

    }

    @Test
    public void printOutcomeTest4() {
        newGame3.getBoard().setup(4);

        for (int i = 0; i <= 5; i++) {
            newGame3.getBoard().increaseScore(1);
        }
        assertThat(newGame3.printOutcome(), CoreMatchers.containsString("Blue"));

    }
}