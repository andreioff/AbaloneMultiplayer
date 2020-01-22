package abalone.test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import abalone.Marble.Marble;
import abalone.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import abalone.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testSetAndGetFieldCell() {
        Pair p = new Pair(0, 4);
        board.setField(p, new Marble(1));
        assertEquals(1, board.getField(p).getColorNr());

        p = new Pair(0, 5);
        assertNull(board.getField(p));
    }

    @Test
    public void testSetAndGetFieldIndex() {
        board.setField(1, new Marble(1));
        assertEquals(1, board.getField(1).getColorNr());
        assertNull(board.getField(2));
    }

    @Test
    public void testIsFieldIndex() {
        assertFalse(board.isField(-1));
        assertFalse(board.isField(61));
        assertTrue(board.isField(0));
        assertTrue(board.isField(1));
        assertTrue(board.isField(60));
    }

    @Test
    public void testIsFieldCell() {
        assertFalse(board.isField(new Pair(0, 3)));
        assertFalse(board.isField(new Pair(8, 5)));
        assertFalse(board.isField(new Pair(9, 9)));
        assertTrue(board.isField(new Pair(0, 4)));
        assertTrue(board.isField(new Pair(8, 4)));
    }

    @Test
    public void testIsEmptyField() {
        board.setField(0, new Marble(1));
        assertFalse(board.isEmptyField(0));
        assertTrue(board.isEmptyField(1));
        assertFalse(board.isEmptyField(new Pair(0, 4)));
        assertTrue(board.isEmptyField(new Pair(0, 5)));
    }

    @Test
    public void testIncreasePlayersAndGetScore() {
        board.setup(3);
        board.increaseScore(1);
        board.increaseScore(2);
        board.increaseScore(2);
        board.increaseScore(3);
        board.increaseScore(3);
        board.increaseScore(3);
        int[] score = board.getScore();
        assertEquals(1, score[0]);
        assertEquals(2, score[1]);
        assertEquals(3, score[2]);
    }

    @Test
    public void testGetScore4Players() {
        board.setup(4);
        board.increaseScore(1);
        board.increaseScore(2);
        board.increaseScore(2);
        board.increaseScore(3);
        board.increaseScore(3);
        board.increaseScore(3);
        board.increaseScore(4);
        int[] score = board.getScore();
        assertEquals(4, score[0]);
        assertEquals(3, score[1]);
        assertEquals(4, score[2]);
        assertEquals(3, score[3]);
    }

    @Test
    public void testIsWinner() {
        board.setup(3);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(2);
        assertTrue(board.isWinner(1));
        assertFalse(board.isWinner(2));
        assertFalse(board.isWinner(3));
        assertFalse(board.isWinner(4));
    }

    @Test
    public void testIsWinner4Players() {
        board.setup(4);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(2);
        assertTrue(board.isWinner(1));
        assertFalse(board.isWinner(2));
        assertTrue(board.isWinner(3));
        assertFalse(board.isWinner(4));
    }

    @Test
    public void testHasWinner() {
        board.setup(4);
        board.increaseScore(4);
        board.increaseScore(4);
        board.increaseScore(4);
        board.increaseScore(4);
        board.increaseScore(4);
        board.increaseScore(1);
        board.increaseScore(1);
        board.increaseScore(3);
        board.increaseScore(1);
        board.increaseScore(3);
        assertFalse(board.hasWinner());
        board.increaseScore(4);
        assertTrue(board.hasWinner());
    }

    @Test
    public void testGetIndex() {
        assertEquals(0, board.getIndex(new Pair(0, 4)));
        assertEquals(60, board.getIndex(new Pair(8, 4)));
    }

    @Test
    public void testGetCell() {
        Pair p = board.getCell(0);
        assertEquals(0, p.first());
        assertEquals(4, p.second());

        p = board.getCell(60);
        assertEquals(8, p.first());
        assertEquals(4, p.second());
    }

    @Test
    public void testIsColumn() {
        int[][] indexes = {
                {17, 25, 34},
                {11, 18, 26},
                {26, 27, 28},
        };
        boolean[] results = {true, false, false};

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cells.clear();
            for (int j = 0; j < indexes[i].length; j++) {
                cells.add(board.getCell(indexes[i][j]));
            }
            assertEquals(results[i], board.isColumn(cells));
        }
    }

    @Test
    public void testIsRow() {
        int[][] indexes = {
                {32, 33, 34},
                {15, 23, 30},
                {43, 50, 56},
        };
        boolean[] results = {true, false, false};

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cells.clear();
            for (int j = 0; j < indexes[i].length; j++) {
                cells.add(board.getCell(indexes[i][j]));
            }
            assertEquals(results[i], board.isRow(cells));
        }
    }

    @Test
    public void testIsDiagonal() {
        int[][] indexes = {
                {49, 55, 60},
                {21, 30, 39},
                {40, 41, 42},
        };
        boolean[] results = {true, false, false};

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < indexes.length; i++) {
            cells.clear();
            for (int index : indexes[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.isDiagonal(cells));
        }
    }

    @Test
    public void testUpdatePosition() {
        board.setup(2);
        board.setField(30, new Marble(1));
        board.updatePosition(board.getCell(30), 0, 1);
        assertEquals(1, board.getField(22).getColorNr());
        assertNull(board.getField(30));

        board.setField(26, board.getField(30));
        board.setField(30, null);
        board.updatePosition(board.getCell(26), 3, 2);
        assertNull(board.getField(26));
        assertEquals(1, board.getScore()[1]);
    }

    @Test
    public void testMoveMarbles() {
        int[] indexes = {49, 55, 60};

        List<Pair> cells = new ArrayList<>();
        for (int index : indexes) {
            board.setField(index, new Marble(1));
            cells.add(board.getCell(index));
        }
        board.moveMarbles(5, cells, 1);

        assertEquals(1, board.getField(41).getColorNr());
        assertEquals(1, board.getField(48).getColorNr());
        assertEquals(1, board.getField(54).getColorNr());
        for (int index : indexes) {
            assertNull(board.getField(index));
        }
    }

    @Test
    public void testMoveMarblesSumito() {
        board.setup(2);
        int[] indexes = {43, 42, 41};
        List<Pair> cells = new ArrayList<>();

        for (int index : indexes) {
            board.setField(index, new Marble(1));
            cells.add(board.getCell(index));
        }
        board.moveMarbles(1, cells, 1);

        assertEquals(1, board.getField(42).getColorNr());
        assertEquals(1, board.getField(43).getColorNr());
        assertNull(board.getField(41));
        assertEquals(1, board.getScore()[0]);
    }

    @Test
    public void testSetup2() {
        board.setup(2);
        assertEquals(2, board.getScore().length);
        assertEquals(1, board.getField(0).getColorNr());
        assertEquals(2, board.getField(56).getColorNr());
    }

    @Test
    public void testSetup3() {
        board.setup(3);
        assertEquals(3, board.getScore().length);
        assertEquals(1, board.getField(0).getColorNr());
        assertEquals(2, board.getField(3).getColorNr());
        assertEquals(3, board.getField(56).getColorNr());
    }

    @Test
    public void testSetup4() {
        board.setup(4);
        assertEquals(4, board.getScore().length);
        assertEquals(1, board.getField(0).getColorNr());
        assertEquals(2, board.getField(10).getColorNr());
        assertEquals(3, board.getField(58).getColorNr());
        assertEquals(4, board.getField(35).getColorNr());
    }

    @Test
    public void testColorMatch() {
        board.setup(4);
        assertTrue(board.colorMatch(board.getCell(0), 1));
        assertTrue(board.colorMatch(board.getCell(57), 1));
        assertFalse(board.colorMatch(board.getCell(17), 1));
        assertTrue(board.colorMatch(board.getCell(10), 2));
        assertTrue(board.colorMatch(board.getCell(10), 4));
    }

    @Test
    public void testCheckSelection() {
        board.setup(3);
        int[][] indexes = {
                {1, 6, 12},
                {3, 9, 16},
                {52, 53, 54},
                {1, 2},
                {3, 4},
                {32, 60},
                {5, 12, 11},
        };
        int[] colors = {1, 2, 3, 1, 1, 3, 1};
        boolean[] results = {true, true, true, false, false, false, false};

        List<Pair> cells = new ArrayList<>();
        assertFalse(board.checkSelection(cells, 1));

        cells.clear();
        cells.add(new Pair(8, 5));
        cells.add(new Pair(8, 6));
        assertFalse(board.checkSelection(cells, 3));

        for (int i = 0; i < indexes.length; i++) {
            cells.clear();
            for (int index : indexes[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkSelection(cells, colors[i]));
        }
    }

    @Test
    public void testCheckSelection4Players() {
        board.setup(4);

        for (int i = 27; i <= 43; i++) {
            board.setField(i, null);
        }
        for (int i = 0; i < 3; i++) {
            board.setField(28 + i, new Marble(1));
            board.setField(31 + i, new Marble(3));
            board.setField(36 + i, new Marble(2));
            board.setField(39 + i, new Marble(4));
        }

        int[][] indexes = {
                {28, 29, 30},
                {29, 30, 31},
                {30, 31, 32},
                {36, 37, 38},
                {37, 38, 39},
                {38, 39, 40},
                {31, 32, 33},
                {28, 36},
                {32, 41}
        };
        int[] colors = {1, 1, 1, 2, 2, 2, 1, 1, 1};
        boolean[] results = {true, true, true, true, true, true, false, false, false};

        List<Pair> cells = new ArrayList<>();

        for (int i = 0; i < indexes.length; i++) {
            cells.clear();
            for (int index : indexes[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkSelection(cells, colors[i]));
        }
    }

    @Test
    public void testCheckInLineMove4Players() {
        board.setup(4);

        int[] indexes = {21, 22, 29, 30, 31, 38, 39};
        int[][] selections = {
                {39, 30, 21},
                {38, 30, 22},
                {31, 30, 29}
        };
        int[] colors = {1, 1, 1, 3, 3, 3, 3};
        int[] directions = {2, 3, 1, 5, 0, 4};

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        List<Pair> cells = new ArrayList<>();

        for (int i = 0; i < selections.length; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(board.getCell(index));
            }
            assertTrue(board.checkInLineMove4Players(directions[i], cells, 1));
            Collections.reverse(cells);
            assertFalse(board.checkInLineMove4Players(directions[i + 3], cells, 1));
        }
    }

    @Test
    public void testCheckSumito() {
        ArrayList<ArrayList<Pair>> cells = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(board.getCell(4), board.getCell(3))),
                new ArrayList<>(Arrays.asList(board.getCell(9), board.getCell(8))),
                new ArrayList<>(Arrays.asList(board.getCell(16),
                                    board.getCell(15), board.getCell(14))),
                new ArrayList<>(Arrays.asList(board.getCell(25))),
                new ArrayList<>(Arrays.asList(board.getCell(33),
                                    board.getCell(32), board.getCell(31))),
                new ArrayList<>(Arrays.asList(board.getCell(40), board.getCell(39))),
                new ArrayList<>(Arrays.asList(board.getCell(48), board.getCell(47)))
        ));
        boolean[] results = {true, true, true, false, true, false, false};

        for (List<Pair> list : cells) {
            for (Pair cell : list) {
                board.setField(cell, new Marble(1));
            }
        }
        board.setField(5, new Marble(2));
        board.setField(10, new Marble(2));
        board.setField(17, new Marble(2));
        board.setField(26, new Marble(2));
        board.setField(34, new Marble(2));
        board.setField(35, new Marble(3));
        board.setField(41, new Marble(2));
        board.setField(42, new Marble(1));
        board.setField(49, new Marble(2));
        board.setField(50, new Marble(3));

        for (int i = 0; i < 7; i++) {
            assertEquals(results[i], board.checkSumito(1, cells.get(i), 1));
        }
    }

    @Test
    public void testCheckSumito4Players() {
        board.setup(4);
        for (int i = 1; i < 62; i++)
            board.setField(i, null);

        board.setField(3, new Marble(1));
        board.setField(4, new Marble(3));
        board.setField(5, new Marble(2));

        board.setField(8, new Marble(3));
        board.setField(9, new Marble(1));
        board.setField(10, new Marble(2));

        board.setField(14, new Marble(1));
        board.setField(15, new Marble(1));
        board.setField(16, new Marble(3));
        board.setField(17, new Marble(2));

        board.setField(24, new Marble(1));
        board.setField(25, new Marble(3));
        board.setField(26, new Marble(1));
        board.setField(27, new Marble(2));
        board.setField(28, new Marble(4));

        board.setField(31, new Marble(1));
        board.setField(32, new Marble(3));
        board.setField(33, new Marble(2));
        board.setField(34, new Marble(1));

        board.setField(39, new Marble(3));
        board.setField(40, new Marble(1));
        board.setField(41, new Marble(2));
        board.setField(42, new Marble(4));

        ArrayList<ArrayList<Pair>> cells = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(board.getCell(4), board.getCell(3))),
                new ArrayList<>(Arrays.asList(board.getCell(9), board.getCell(8))),
                new ArrayList<>(Arrays.asList(board.getCell(16), board.getCell(15), board.getCell(14))),
                new ArrayList<>(Arrays.asList(board.getCell(26), board.getCell(25), board.getCell(24))),
                new ArrayList<>(Arrays.asList(board.getCell(32), board.getCell(31))),
                new ArrayList<>(Arrays.asList(board.getCell(40), board.getCell(39)))
        ));
        boolean[] results = {true, true, true, true, false, false};

        for (int i = 0; i < 6; i++) {
            assertEquals(results[i], board.checkSumito(1, cells.get(i), 1));
        }
    }

    @Test
    public void testCheckMove() {
        ArrayList<ArrayList<Pair>> cells = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(board.getCell(3), board.getCell(2))),
                new ArrayList<>(Arrays.asList(board.getCell(8), board.getCell(7), board.getCell(6))),
                new ArrayList<>(Arrays.asList(board.getCell(25), board.getCell(24))),
                new ArrayList<>(Arrays.asList(board.getCell(33), board.getCell(32))),
                new ArrayList<>(Arrays.asList(board.getCell(41), board.getCell(40), board.getCell(39))),
                new ArrayList<>(Arrays.asList(board.getCell(47), board.getCell(53), board.getCell(58)))
        ));
        boolean[] results = {false, false, true, false, true, false};

        for (List<Pair> list : cells) {
            for (Pair cell : list) {
                board.setField(cell, new Marble(1));
            }
        }
        board.setField(4, new Marble(1));
        board.setField(9, new Marble(2));
        board.setField(10, new Marble(3));
        board.setField(11, new Marble(3));
        board.setField(26, new Marble(2));
        board.setField(34, new Marble(3));
        board.setField(35, new Marble(2));
        board.setField(42, new Marble(2));
        board.setField(43, new Marble(2));
        board.setField(48, new Marble(2));

        for (int i = 0; i < 6; i++) {
            assertEquals(results[i], board.checkMove(1, cells.get(i), 1));
        }
    }

    @Test
    public void testCheckMove4Players() {
        board.setup(4);
        for (int i = 1; i < 62; i++) {
            board.setField(i,null);
        }
        ArrayList<ArrayList<Pair>> cells = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(board.getCell(3), board.getCell(2))),
                new ArrayList<>(Arrays.asList(board.getCell(8), board.getCell(7), board.getCell(6))),
                new ArrayList<>(Arrays.asList(board.getCell(25),
                                    board.getCell(24), board.getCell(23))),
                new ArrayList<>(Arrays.asList(board.getCell(33),
                                    board.getCell(32), board.getCell(31))),
                new ArrayList<>(Arrays.asList(board.getCell(39),
                                    board.getCell(46), board.getCell(52))),
                new ArrayList<>(Arrays.asList(board.getCell(41),
                                    board.getCell(48), board.getCell(54)))
        ));
        boolean[] results = {false, true, false, false, true, false};

        board.setField(2, new Marble(1));
        board.setField(3, new Marble(1));
        board.setField(4, new Marble(3));
        board.setField(6, new Marble(1));
        board.setField(7, new Marble(3));
        board.setField(8, new Marble(3));
        board.setField(23, new Marble(3));
        board.setField(24, new Marble(3));
        board.setField(25, new Marble(1));
        board.setField(31, new Marble(1));
        board.setField(32, new Marble(3));
        board.setField(33, new Marble(1));
        board.setField(34, new Marble(1));
        board.setField(35, new Marble(4));
        board.setField(39, new Marble(1));
        board.setField(46, new Marble(3));
        board.setField(52, new Marble(1));
        board.setField(41, new Marble(3));
        board.setField(48, new Marble(3));
        board.setField(54, new Marble(1));
        board.setField(55, new Marble(1));

        for (int i = 0; i < 6; i++) {
            assertEquals(results[i], board.checkMove(1, cells.get(i), 1));
        }
    }
}
