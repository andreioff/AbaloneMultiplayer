import Marble.Marble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Board {
    private static final HashMap<Pair, Integer> CELLINDEX = new HashMap<>() {{
        put(new Pair(0, 4), 1);
        put(new Pair(0, 5), 2);
        put(new Pair(0, 6), 3);
        put(new Pair(0, 7), 4);
        put(new Pair(0, 8), 5);
        put(new Pair(1, 3), 6);
        put(new Pair(1, 4), 7);
        put(new Pair(1, 5), 8);
        put(new Pair(1, 6), 9);
        put(new Pair(1, 7), 10);
        put(new Pair(1, 8), 11);
        put(new Pair(2, 2), 12);
        put(new Pair(2, 3), 13);
        put(new Pair(2, 4), 14);
        put(new Pair(2, 5), 15);
        put(new Pair(2, 6), 16);
        put(new Pair(2, 7), 17);
        put(new Pair(2, 8), 18);
        put(new Pair(3, 1), 19);
        put(new Pair(3, 2), 20);
        put(new Pair(3, 3), 21);
        put(new Pair(3, 4), 22);
        put(new Pair(3, 5), 23);
        put(new Pair(3, 6), 24);
        put(new Pair(3, 7), 25);
        put(new Pair(3, 8), 26);
        put(new Pair(4, 0), 27);
        put(new Pair(4, 1), 28);
        put(new Pair(4, 2), 29);
        put(new Pair(4, 3), 30);
        put(new Pair(4, 4), 31);
        put(new Pair(4, 5), 32);
        put(new Pair(4, 6), 33);
        put(new Pair(4, 7), 34);
        put(new Pair(4, 8), 35);
        put(new Pair(5, 0), 36);
        put(new Pair(5, 1), 37);
        put(new Pair(5, 2), 38);
        put(new Pair(5, 3), 39);
        put(new Pair(5, 4), 40);
        put(new Pair(5, 5), 41);
        put(new Pair(5, 6), 42);
        put(new Pair(5, 7), 43);
        put(new Pair(6, 0), 44);
        put(new Pair(6, 1), 45);
        put(new Pair(6, 2), 46);
        put(new Pair(6, 3), 47);
        put(new Pair(6, 4), 48);
        put(new Pair(6, 5), 49);
        put(new Pair(6, 6), 50);
        put(new Pair(7, 0), 51);
        put(new Pair(7, 1), 52);
        put(new Pair(7, 2), 53);
        put(new Pair(7, 3), 54);
        put(new Pair(7, 4), 55);
        put(new Pair(7, 5), 56);
        put(new Pair(8, 0), 57);
        put(new Pair(8, 1), 58);
        put(new Pair(8, 2), 59);
        put(new Pair(8, 3), 60);
        put(new Pair(8, 4), 61);
    }};
    private static final HashMap<Integer, Pair> INDEXCELL = new HashMap<>() {{
        put(1, new Pair(0, 4));
        put(2, new Pair(0, 5));
        put(3, new Pair(0, 6));
        put(4, new Pair(0, 7));
        put(5, new Pair(0, 8));
        put(6, new Pair(1, 3));
        put(7, new Pair(1, 4));
        put(8, new Pair(1, 5));
        put(9, new Pair(1, 6));
        put(10, new Pair(1, 7));
        put(11, new Pair(1, 8));
        put(12, new Pair(2, 2));
        put(13, new Pair(2, 3));
        put(14, new Pair(2, 4));
        put(15, new Pair(2, 5));
        put(16, new Pair(2, 6));
        put(17, new Pair(2, 7));
        put(18, new Pair(2, 8));
        put(19, new Pair(3, 1));
        put(20, new Pair(3, 2));
        put(21, new Pair(3, 3));
        put(22, new Pair(3, 4));
        put(23, new Pair(3, 5));
        put(24, new Pair(3, 6));
        put(25, new Pair(3, 7));
        put(26, new Pair(3, 8));
        put(27, new Pair(4, 0));
        put(28, new Pair(4, 1));
        put(29, new Pair(4, 2));
        put(30, new Pair(4, 3));
        put(31, new Pair(4, 4));
        put(32, new Pair(4, 5));
        put(33, new Pair(4, 6));
        put(34, new Pair(4, 7));
        put(35, new Pair(4, 8));
        put(36, new Pair(5, 0));
        put(37, new Pair(5, 1));
        put(38, new Pair(5, 2));
        put(39, new Pair(5, 3));
        put(40, new Pair(5, 4));
        put(41, new Pair(5, 5));
        put(42, new Pair(5, 6));
        put(43, new Pair(5, 7));
        put(44, new Pair(6, 0));
        put(45, new Pair(6, 1));
        put(46, new Pair(6, 2));
        put(47, new Pair(6, 3));
        put(48, new Pair(6, 4));
        put(49, new Pair(6, 5));
        put(50, new Pair(6, 6));
        put(51, new Pair(7, 0));
        put(52, new Pair(7, 1));
        put(53, new Pair(7, 2));
        put(54, new Pair(7, 3));
        put(55, new Pair(7, 4));
        put(56, new Pair(7, 5));
        put(57, new Pair(8, 0));
        put(58, new Pair(8, 1));
        put(59, new Pair(8, 2));
        put(60, new Pair(8, 3));
        put(61, new Pair(8, 4));
    }};
    private static final Pair[] directions = {
                    new Pair(-1, 1), new Pair(0, 1), new Pair(1, 0),
                    new Pair(1, -1), new Pair(0, -1), new Pair(-1, 0)
    };
    private static final String indexesBoard =
            "          01  02  03  04  05;" +
                    "        06  07  08  09  10  11;" +
                    "      12  13  14  15  16  17  18;" +
                    "    19  20  21  22  23  24  25  26;" +
                    "  27  28  29  30  31  32  33  34  35;" +
                    "    36  37  38  39  40  41  42  43;" +
                    "      44  45  46  47  48  49  50;" +
                    "        51  52  53  54  55  56;" +
                    "          57  58  59  60  61";

    private int[] score;
    private Marble[][] board;
    int players;

    public Board(int players) {
        assert players >= 2 && players <= 4;
        this.players = players;
        score = new int[players];
        board = new Marble[9][9];
        setupTest();
    }

    public void setupTest() {
        setField(1, new Marble(2));
        setField(2, new Marble(1));
        setField(3, new Marble(1));
    }

    public void setup2() {
        Pair p;

        for (int i = 1; i <= 11; i++) {
            p = INDEXCELL.get(i);
            board[p.first()][p.second()] = new Marble(1);

            p = INDEXCELL.get(61 - i + 1);
            board[p.first()][p.second()] = new Marble(2);
        }

        for (int i = 14; i <= 16; i++) {
            p = INDEXCELL.get(i);
            board[p.first()][p.second()] = new Marble(1);

            p = INDEXCELL.get(32 + i);
            board[p.first()][p.second()] = new Marble(2);
        }
    }

    public void setup3() {
        int marbels = 5;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < marbels; j++) {
                board[j][4 + i - j] = new Marble(1);
            }

            for (int j = 0; j < marbels; j++) {
                board[j][8 - i] = new Marble(2);
            }

            for (int j = 0; j < marbels; j++) {
                board[8 - i][j] = new Marble(3);
            }
            marbels++;
        }
    }

    public void setup4() {
        for (int i = 4; i >= 2; i--) {
            for (int j = 0; j < i; j++) {
                board[4 - i][j + 4] = new Marble(1);
            }

            for (int j = 0; j < i; j++) {
                board[5 + j - i][i + 4] = new Marble(2);
            }

            for (int j = 0; j < i; j++) {
                board[4 + i][5 + j - i] = new Marble(3);
            }

            for (int j = 0; j < i; j++) {
                board[4 + j][4 - i] = new Marble(4);
            }
        }
    }

    public void setup(int players) {
        assert players >= 2 && players <= 4;
        switch (players) {
            case 2:
                setup2();
                break;

            case 3:
                setup3();
                break;

            case 4:
                setup4();
                break;
        }
    }

    private boolean isDiagonal(ArrayList<Pair> cells) {
        Pair p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if ((Math.abs(p1.first() - p2.first()) != 1 || Math.abs(p1.second() - p2.second()) != 1))
                return false;
            p1 = p2;
        }
        return true;
    }

    private boolean isRow(ArrayList<Pair> cells) {
        Pair p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if (Math.abs(p1.first() - p2.first()) != 0 || Math.abs(p1.second() - p2.second()) != 1)
                return false;
            p1 = p2;
        }
        return true;
    }

    private boolean isColumn(ArrayList<Pair> cells) {
        Pair p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if (Math.abs(p1.first() - p2.first()) != 1 || Math.abs(p1.second() - p2.second()) != 0)
                return false;
            p1 = p2;
        }
        return true;
    }

    private boolean checkSumito(int directionIndex, ArrayList<Pair> cells, int color) {
        Pair cell = cells.get(0);
        Pair behindCell = Pair.add(cell, Pair.opposite(directions[directionIndex]));
        if (!isField(behindCell) || !cells.contains(behindCell)) return false;

        Pair futurePos = Pair.add(cell, directions[directionIndex]);
        cells.add(0, futurePos);

        futurePos = Pair.add(futurePos, directions[directionIndex]);
        if (!isField(futurePos)) return true;
        if (isEmptyField(futurePos)) return true;

        int futureField = getField(futurePos).getColorNr();
        if (cells.size() < 3 || futureField == color || futureField == Marble.getTeammateColor(color))
            return false;

        cells.add(0, futurePos);

        futurePos = Pair.add(futurePos, directions[directionIndex]);
        return !isField(futurePos) || isEmptyField(futurePos);
    }

    public boolean checkMove(int directionIndex, ArrayList<Pair> cells, int color) {
        if (!checkSelection(cells, color)) {
            return false;
        }
        Pair futurePos;
        int currentColor;
        for (Pair cell : cells) {
            futurePos = Pair.add(cell, directions[directionIndex]);
            if (!isField(futurePos)) return false;
            if (!isEmptyField(futurePos)) {
                if (cells.size() == 1) return false;

                currentColor = getField(futurePos).getColorNr();
                if (currentColor == color || currentColor == Marble.getTeammateColor(color)) {
                    if (!cells.contains(futurePos)) return false;
                } else if (checkSumito(directionIndex, cells, color)) {
                    return true;
                } else return false;
            }
        }

        return true;
    }

    private boolean colorMatch(Pair cell, int color) {
        int currentColor = getField(cell).getColorNr();
        if ((players < 4)) {
            return currentColor == color;
        }
        return currentColor == color || currentColor == Marble.getTeammateColor(color);
    }

    public boolean checkSelection(ArrayList<Pair> cells, int color) {
        boolean currentColorSelected = false;

        if (cells.size() < 1 || cells.size() > 3) return false;

        for (Pair cell : cells) {
            if (!isField(cell))
                return false;
            if (isEmptyField(cell))
                return false;
            if (!colorMatch(cell, color))
                return false;
            if (getField(cell).getColorNr() == color)
                currentColorSelected = true;
        }

        if (players == 4 && !currentColorSelected) return false;

        return isColumn(cells) || isRow(cells) || isDiagonal(cells);
    }

    public boolean move(int directionIndex, List<Integer> indexes, int color) {
        Collections.sort(indexes);
        List<Pair> cells = new ArrayList<>();
        for (Integer index : indexes) {
            cells.add(getCell(index));
        }
        cells.sort(new CompareByDirection(directions[directionIndex]));

        if (checkMove(directionIndex, (ArrayList<Pair>)cells, color)) {
            moveMarbles(directionIndex, (ArrayList<Pair>)cells);
            return true;
        }
        return false;
    }

    private void updatePosition(Pair currentPos, int directionIndex) {
        Pair futurePos = Pair.add(currentPos, directions[directionIndex]);
        Marble marble = isField(futurePos) ? getField(currentPos) : null;
        setField(futurePos, marble);
        setField(currentPos, null);
    }

    private void moveMarbles(int directionIndex, ArrayList<Pair> cells) {
        for (Pair cell : cells) {
            updatePosition(cell, directionIndex);
        }
    }

    public Marble getField(int index) {
        assert isField(index);
        Pair p = INDEXCELL.get(index);
        return board[p.first()][p.second()];
    }

    public Marble getField(Pair cell) {
        assert isField(cell);
        return board[cell.first()][cell.second()];
    }

    public Pair getCell(int index) {
        assert isField(index);
        return INDEXCELL.get(index);
    }

    public int getIndex(Pair cell) {
        assert isField(cell);
        return CELLINDEX.get(cell);
    }

    public int[] getScore() {
        return score;
    }

    public boolean isField(int index) {
        return INDEXCELL.containsKey(index);
    }

    public boolean isField(Pair p) {
        return CELLINDEX.containsKey(p);
    }

    public boolean isWinner(int color) {
        return score[color] == 6;
    }

    public void setField(int index, Marble marble) {
        assert isField(index);
        Pair p = INDEXCELL.get(index);
        board[p.first()][p.second()] = marble;
    }

    public void setField(Pair cell, Marble marble) {
        assert isField(cell);
        board[cell.first()][cell.second()] = marble;
    }

    public boolean isEmptyField(Pair cell) {
        return getField(cell) == null;
    }

    public boolean isEmptyField(int index) {
        return getField(index) == null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int tab = 10;
        int marbelsPerRow = 5;
        int start = 4;
        String[] splittedIndexesBoard = indexesBoard.split(";");

        for (int i = 0; i < 9; i++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%" + tab + "s", ""));
            for (int j = 0; j < marbelsPerRow; j++) {
                row.append(board[i][start + j] != null ? " " + board[i][start + j].getColorNr() + "  " : " .  ");
            }
            row.append(String.format("%" + tab + "s", ""));
            row.append(splittedIndexesBoard[i]);
            row.append(System.lineSeparator());
            str.append(row);
            if (i < 4) {
                start--;
                tab -= 2;
                marbelsPerRow++;
            } else {
                tab += 2;
                marbelsPerRow--;
            }
        }
        return str.toString();
    }

    public static void main(String[] args) {
        Board board = new Board(2);
        System.out.println(board.toString());
    }
}