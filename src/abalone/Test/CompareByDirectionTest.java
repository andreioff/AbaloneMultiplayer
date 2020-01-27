package abalone.Test;

import abalone.Game.CompareByDirection;
import abalone.Game.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareByDirectionTest {
    @Test
    public void testCompare() {
        //2, 3, 4, 5, 1, 0
        Pair[] directions = {
                new Pair(1, 0), new Pair(1, -1), new Pair(0, -1),
                new Pair(-1, 0), new Pair(0, 1), new Pair(-1, 1)
        };
        Pair[][] results = {
                {new Pair(1, 4), new Pair(1, 5), new Pair(1, 6)},
                {new Pair(1, 6), new Pair(1, 5), new Pair(1, 4)}
        };
        int j = 0;

        List<Pair> cells = new ArrayList<>(Arrays.asList(
                new Pair(1, 4), new Pair(1, 5), new Pair(1, 6)
        ));

        for (int i = 0; i < 6; i++) {
            cells.sort(new CompareByDirection(directions[i]));
            for (int k = 0; k < 3; k++) {
                assertEquals(results[j][k], cells.get(k));
            }
            if (i == 3) j++;
        }

    }
}
