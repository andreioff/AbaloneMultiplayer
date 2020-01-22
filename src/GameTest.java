import Marble.Marble;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;


public class GameTest {
    private Game newGame1;
    private Game newGame2;
    private Game newGame3;



    @BeforeEach
    public void setUp() {
        newGame1 = new Game(2);
        newGame2 = new Game(3);
        newGame3 = new Game(4);

    }

    @Test
    public void playTest() {


    }

    @Test
    public void printOutcomeTest() {

    }
}