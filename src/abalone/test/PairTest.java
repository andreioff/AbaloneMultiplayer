package abalone.test;

import abalone.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PairTest {
    @Test
    public void testEquals() {
        Pair p1 = new Pair(1, 2);
        Pair p2 = new Pair(1, 2);
        Pair p3 = new Pair(2, 2);

        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p3.equals(p2));
    }

    @Test
    public void testAdd() {
        Pair p1 = new Pair(5, 2);
        Pair p2 = new Pair(-1, 2);
        Pair p3 = new Pair(4, 4);

        assertEquals(p3, Pair.add(p1, p2));
    }

    @Test
    public void testOpposite() {
        Pair p1 = new Pair(5, 2);
        Pair p2 = new Pair(-5, -2);

        assertEquals(p2, Pair.opposite(p1));
    }
}
