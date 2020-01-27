package abalone.Test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import abalone.Game.Marble;
import abalone.Game.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import abalone.Game.Board;

import java.util.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testSetupHashesAndGetHashes() {
        Map<Integer, Pair> INDEXCELL = board.getINDEXCELL();
        Map<Pair, Integer> CELLINDEX = board.getCELLINDEX();

        assertTrue(INDEXCELL.containsKey(0));
        assertTrue(INDEXCELL.containsKey(60));
        assertFalse(INDEXCELL.containsKey(-1));
        assertFalse(INDEXCELL.containsKey(61));

        assertTrue(CELLINDEX.containsValue(0));
        assertTrue(CELLINDEX.containsValue(60));
        assertFalse(CELLINDEX.containsValue(-1));
        assertFalse(CELLINDEX.containsValue(61));

        assertEquals(new Pair(0, 4), INDEXCELL.get(0));
        assertEquals(new Pair(8, 4), INDEXCELL.get(60));
        assertEquals(0, CELLINDEX.get(new Pair(0, 4)));
        assertEquals(60, CELLINDEX.get(new Pair(8, 4)));
    }

    @Test
    public void testReset() {
        board.setup(4);
        board.setField(30, new Marble(4));
        assertEquals(4, board.getField(30).getColorNr());

        board.reset();
        assertNull(board.getField(30));
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
        int[] indexes = {42, 41, 40};
        List<Pair> cells = new ArrayList<>();

        for (int index : indexes) {
            board.setField(index, new Marble(1));
            cells.add(board.getCell(index));
        }
        board.moveMarbles(1, cells, 1);

        assertEquals(1, board.getField(41).getColorNr());
        assertEquals(1, board.getField(42).getColorNr());
        assertNull(board.getField(40));
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
        int[][] selections = {
                {3, 2},
                {8, 7},
                {15, 14, 13},
                {24},
                {32, 31, 30},
                {39, 38},
                {47, 46}
        };

        int[] indexes = {4, 9, 16, 25, 33, 34, 40, 41, 48, 49};
        int[] colors = {2, 2, 2, 2, 2, 3, 2, 1, 2, 3};
        boolean[] results = {true, true, true, false, true, false, false};

        List<Pair> cells = new ArrayList<>();
        for (int[] selection : selections) {
            for (int index : selection) {
                board.setField(index, new Marble(1));
            }
        }

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        for (int i = 0; i < 7; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkSumito(1, cells, 1));
        }
    }

    @Test
    public void testCheckSumito4Players() {
        board.setup(4);
        for (int i = 0; i < 61; i++)
            board.setField(i, null);

        int[] indexes = {2, 3, 4, 7, 8, 9, 13, 14, 15, 16, 23, 24, 25, 26, 27, 30, 31, 32, 33, 38, 39, 40, 41};
        int[] colors = {1, 3, 2, 3, 1, 2, 1, 1, 3, 2, 1, 3, 1, 2, 4, 1, 3, 2, 1, 3, 1, 2, 4};

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        int[][] selections = {
                {3, 2},
                {8, 7},
                {15, 14, 13},
                {25, 24, 23},
                {31, 30},
                {39, 38}
        };
        boolean[] results = {true, true, true, true, false, false};

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkSumito(1, cells, 1));
        }
    }

    @Test
    public void testCheckMove() {
        int[][] selections = {
                {2, 1},
                {7, 6, 5},
                {24, 23},
                {32, 31},
                {40, 39, 38},
                {46, 52, 57}
        };
        boolean[] results = {false, false, true, false, true, false};

        for (int[] selection : selections) {
            for (int index : selection) {
                board.setField(board.getCell(index), new Marble(1));
            }
        }

        int[] indexes = {3, 8, 9, 10, 25, 33, 34, 41, 42, 47};
        int[] colors = {1, 2, 3, 3, 2, 3, 2, 2, 2, 2};

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cells.clear();
            for (int index : selections[i]){
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkMove(1, cells, 1));
        }
    }

    @Test
    public void testCheckMove4Players() {
        board.setup(4);
        for (int i = 0; i < 61; i++) {
            board.setField(i,null);
        }

        int[][] selections = {
                {2, 1},
                {7, 6, 5},
                {24, 23, 22},
                {32, 31, 30},
                {38, 45, 51},
                {40, 47, 53}
        };

        int[] indexes = {1, 2, 3, 5, 6, 7, 22, 23, 24, 30, 31, 32, 33, 34, 38, 45, 51, 40, 47, 53, 54};
        int[] colors = {1, 1, 3, 1, 3, 3, 3, 3, 1, 1, 3, 1, 1, 4, 1, 3, 1, 3, 3, 1, 1};
        boolean[] results = {false, true, false, false, true, false};

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        List<Pair> cells = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(board.getCell(index));
            }
            assertEquals(results[i], board.checkMove(1, cells, 1));
        }
    }

    @Test
    public void testMove() {
        board.setup(2);
        int[][] selections = {
                {20, 12, 29},
                {41, 40},
                {25, 34},
                {25, 34}
        };
        int[] directions = {3, 1, 2, 3};

        for (int i = 0; i < selections.length - 1; i++) {
            for (int index : selections[i]) {
                board.setField(index, new Marble(2));
            }
        }
        board.setField(42, new Marble(1));

        List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < selections.length; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(index);
            }
            board.move(directions[i], cells, 2);
        }

        int[] currentPositions = {19, 28, 37};
        for (int i = 0; i < selections[0].length; i++) {
            assertNull(board.getField(selections[0][i]));
            assertEquals(2, board.getField(currentPositions[i]).getColorNr());
        }

        assertNull(board.getField(40));
        assertEquals(2, board.getField(41).getColorNr());
        assertEquals(2, board.getField(42).getColorNr());
        assertEquals(1, board.getScore()[1]);

        assertEquals(2, board.getField(25).getColorNr());
        assertEquals(2, board.getField(34).getColorNr());
    }

    @Test
    public void testMove4Players() {
        board.setup(4);
        for (int i = 11; i <= 42; i++) {
            board.setField(i, null);
        }

        int[][] selections = {
                {20, 12, 29},
                {41, 40},
                {25, 34},
                {25, 34}
        };
        int[] directions = {3, 1, 2, 3};
        int[] indexes = {20, 12, 29, 41, 40, 25, 34, 42};
        int[] colors = {4, 2, 2, 4, 2, 4, 2, 3};

        for (int i = 0; i < indexes.length; i++) {
            board.setField(indexes[i], new Marble(colors[i]));
        }

        List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < selections.length; i++) {
            cells.clear();
            for (int index : selections[i]) {
                cells.add(index);
            }
            board.move(directions[i], cells, 2);
        }

        int[] currentPositions = {19, 28, 37};
        int[] orderedColors = {2, 4, 2};
        for (int i = 0; i < selections[0].length; i++) {
            assertNull(board.getField(selections[0][i]));
            assertEquals(orderedColors[i], board.getField(currentPositions[i]).getColorNr());
        }

        assertNull(board.getField(40));
        assertEquals(2, board.getField(41).getColorNr());
        assertEquals(4, board.getField(42).getColorNr());
        assertEquals(1, board.getScore()[1]);
        assertEquals(1, board.getScore()[3]);

        assertEquals(4, board.getField(25).getColorNr());
        assertEquals(2, board.getField(34).getColorNr());
    }
}
