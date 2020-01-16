import Marble.Marble;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Board {
    private static final HashMap<Pair<Integer, Integer>, Integer> CELLINDEX = new HashMap<>() {{
        put(new Pair<Integer, Integer>(0, 0), 1);
        put(new Pair<Integer, Integer>(0, 1), 2);
        put(new Pair<Integer, Integer>(0, 2), 3);
        put(new Pair<Integer, Integer>(0, 3), 4);
        put(new Pair<Integer, Integer>(0, 4), 5);
        put(new Pair<Integer, Integer>(1, 0), 6);
        put(new Pair<Integer, Integer>(1, 1), 7);
        put(new Pair<Integer, Integer>(1, 2), 8);
        put(new Pair<Integer, Integer>(1, 3), 9);
        put(new Pair<Integer, Integer>(1, 4), 10);
        put(new Pair<Integer, Integer>(1, 5), 11);
        put(new Pair<Integer, Integer>(2, 0), 12);
        put(new Pair<Integer, Integer>(2, 1), 13);
        put(new Pair<Integer, Integer>(2, 2), 14);
        put(new Pair<Integer, Integer>(2, 3), 15);
        put(new Pair<Integer, Integer>(2, 4), 16);
        put(new Pair<Integer, Integer>(2, 5), 17);
        put(new Pair<Integer, Integer>(2, 6), 18);
        put(new Pair<Integer, Integer>(3, 0), 19);
        put(new Pair<Integer, Integer>(3, 1), 20);
        put(new Pair<Integer, Integer>(3, 2), 21);
        put(new Pair<Integer, Integer>(3, 3), 22);
        put(new Pair<Integer, Integer>(3, 4), 23);
        put(new Pair<Integer, Integer>(3, 5), 24);
        put(new Pair<Integer, Integer>(3, 6), 25);
        put(new Pair<Integer, Integer>(3, 7), 26);
        put(new Pair<Integer, Integer>(4, 0), 27);
        put(new Pair<Integer, Integer>(4, 1), 28);
        put(new Pair<Integer, Integer>(4, 2), 29);
        put(new Pair<Integer, Integer>(4, 3), 30);
        put(new Pair<Integer, Integer>(4, 4), 31);
        put(new Pair<Integer, Integer>(4, 5), 32);
        put(new Pair<Integer, Integer>(4, 6), 33);
        put(new Pair<Integer, Integer>(4, 7), 34);
        put(new Pair<Integer, Integer>(4, 8), 35);
        put(new Pair<Integer, Integer>(5, 0), 36);
        put(new Pair<Integer, Integer>(5, 1), 37);
        put(new Pair<Integer, Integer>(5, 2), 38);
        put(new Pair<Integer, Integer>(5, 3), 39);
        put(new Pair<Integer, Integer>(5, 4), 40);
        put(new Pair<Integer, Integer>(5, 5), 41);
        put(new Pair<Integer, Integer>(5, 6), 42);
        put(new Pair<Integer, Integer>(5, 7), 43);
        put(new Pair<Integer, Integer>(6, 0), 44);
        put(new Pair<Integer, Integer>(6, 1), 45);
        put(new Pair<Integer, Integer>(6, 2), 46);
        put(new Pair<Integer, Integer>(6, 3), 47);
        put(new Pair<Integer, Integer>(6, 4), 48);
        put(new Pair<Integer, Integer>(6, 5), 49);
        put(new Pair<Integer, Integer>(6, 6), 50);
        put(new Pair<Integer, Integer>(7, 0), 51);
        put(new Pair<Integer, Integer>(7, 1), 52);
        put(new Pair<Integer, Integer>(7, 2), 53);
        put(new Pair<Integer, Integer>(7, 3), 54);
        put(new Pair<Integer, Integer>(7, 4), 55);
        put(new Pair<Integer, Integer>(7, 5), 56);
        put(new Pair<Integer, Integer>(8, 0), 57);
        put(new Pair<Integer, Integer>(8, 1), 58);
        put(new Pair<Integer, Integer>(8, 2), 59);
        put(new Pair<Integer, Integer>(8, 3), 60);
        put(new Pair<Integer, Integer>(8, 4), 61);
    }};
    private static final HashMap<Integer, Pair<Integer, Integer>> INDEXCELL = new HashMap<>() {{
        put(1, new Pair<Integer, Integer>(0, 0));
        put(2, new Pair<Integer, Integer>(0, 1));
        put(3, new Pair<Integer, Integer>(0, 2));
        put(4, new Pair<Integer, Integer>(0, 3));
        put(5, new Pair<Integer, Integer>(0, 4));
        put(6, new Pair<Integer, Integer>(1, 0));
        put(7, new Pair<Integer, Integer>(1, 1));
        put(8, new Pair<Integer, Integer>(1, 2));
        put(9, new Pair<Integer, Integer>(1, 3));
        put(10, new Pair<Integer, Integer>(1, 4));
        put(11, new Pair<Integer, Integer>(1, 5));
        put(12, new Pair<Integer, Integer>(2, 0));
        put(13, new Pair<Integer, Integer>(2, 1));
        put(14, new Pair<Integer, Integer>(2, 2));
        put(15, new Pair<Integer, Integer>(2, 3));
        put(16, new Pair<Integer, Integer>(2, 4));
        put(17, new Pair<Integer, Integer>(2, 5));
        put(18, new Pair<Integer, Integer>(2, 6));
        put(19, new Pair<Integer, Integer>(3, 0));
        put(20, new Pair<Integer, Integer>(3, 1));
        put(21, new Pair<Integer, Integer>(3, 2));
        put(22, new Pair<Integer, Integer>(3, 3));
        put(23, new Pair<Integer, Integer>(3, 4));
        put(24, new Pair<Integer, Integer>(3, 5));
        put(25, new Pair<Integer, Integer>(3, 6));
        put(26, new Pair<Integer, Integer>(3, 7));
        put(27, new Pair<Integer, Integer>(4, 0));
        put(28, new Pair<Integer, Integer>(4, 1));
        put(29, new Pair<Integer, Integer>(4, 2));
        put(30, new Pair<Integer, Integer>(4, 3));
        put(31, new Pair<Integer, Integer>(4, 4));
        put(32, new Pair<Integer, Integer>(4, 5));
        put(33, new Pair<Integer, Integer>(4, 6));
        put(34, new Pair<Integer, Integer>(4, 7));
        put(35, new Pair<Integer, Integer>(4, 8));
        put(36, new Pair<Integer, Integer>(5, 0));
        put(37, new Pair<Integer, Integer>(5, 1));
        put(38, new Pair<Integer, Integer>(5, 2));
        put(39, new Pair<Integer, Integer>(5, 3));
        put(40, new Pair<Integer, Integer>(5, 4));
        put(41, new Pair<Integer, Integer>(5, 5));
        put(42, new Pair<Integer, Integer>(5, 6));
        put(43, new Pair<Integer, Integer>(5, 7));
        put(44, new Pair<Integer, Integer>(6, 0));
        put(45, new Pair<Integer, Integer>(6, 1));
        put(46, new Pair<Integer, Integer>(6, 2));
        put(47, new Pair<Integer, Integer>(6, 3));
        put(48, new Pair<Integer, Integer>(6, 4));
        put(49, new Pair<Integer, Integer>(6, 5));
        put(50, new Pair<Integer, Integer>(6, 6));
        put(51, new Pair<Integer, Integer>(7, 0));
        put(52, new Pair<Integer, Integer>(7, 1));
        put(53, new Pair<Integer, Integer>(7, 2));
        put(54, new Pair<Integer, Integer>(7, 3));
        put(55, new Pair<Integer, Integer>(7, 4));
        put(56, new Pair<Integer, Integer>(7, 5));
        put(57, new Pair<Integer, Integer>(8, 0));
        put(58, new Pair<Integer, Integer>(8, 1));
        put(59, new Pair<Integer, Integer>(8, 2));
        put(60, new Pair<Integer, Integer>(8, 3));
        put(61, new Pair<Integer, Integer>(8, 4));
    }};
    private static final int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
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
        this.players = (players > 2 && players <= 4) ? players : 2;
        score = new int[this.players];
        board = new Marble[9][9];
        setup(this.players);
    }

    public void setup2() {
        Pair<Integer, Integer> p;

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
        int marbels = 6;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < marbels; j++)
                board[j][i] = new Marble(1);
            marbels--;
        }

        marbels = 5;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < marbels; j++)
                board[8 - i][j] = new Marble(2);
            marbels++;
        }

        marbels = 5;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < marbels; j++) {
                board[j][3 + j + i] = new Marble(3);
            }
        }
        board[5][7] = new Marble(3);
    }

    public void setup4() {
        for (int i = 4; i >= 2; i--) {
            for (int j = 0; j < i; j++) {
                board[4 - i][j + 4 - i] = new Marble(1);
            }

            for (int j = 0; j < i; j++) {
                board[5 + j - i][5 + j] = new Marble(2);
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

    private boolean isDiagonal(ArrayList<Pair<Integer, Integer>> cells) {
        Pair<Integer, Integer> p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if (Math.abs(p1.first() - p2.first()) != 1 || Math.abs(p1.second() - p2.second()) != 1)
                return false;
            p1 = p2;
        }
        return true;
    }

    private boolean isRow(ArrayList<Pair<Integer, Integer>> cells) {
        Pair<Integer, Integer> p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if (Math.abs(p1.first() - p2.first()) != 0 || Math.abs(p1.second() - p2.second()) != 1)
                return false;
            p1 = p2;
        }
        return true;
    }

    private boolean isColumn(ArrayList<Pair<Integer, Integer>> cells) {
        Pair<Integer, Integer> p1, p2;
        p1 = cells.get(0);
        for (int i = 1; i < cells.size(); i++) {
            p2 = cells.get(i);
            if (Math.abs(p1.first() - p2.first()) != 1 || Math.abs(p1.second() - p2.second()) != 0)
                return false;
            p1 = p2;
        }
        return true;
    }

    public boolean checkSelection(ArrayList<Integer> indexes, int color) {
        Pair<Integer, Integer> p;
        int currentColor;

        if (indexes.size() < 1 || indexes.size() > 3) return false;

        for (Integer index : indexes) {
            if (index < 1 || index > 61)
                return false;

            if (getField(index) == null)
                return false;
        }

        for (Integer index : indexes) {
            currentColor = getField(index).getColor();
            if ((players < 4 && currentColor != color) || (players == 4 && currentColor != color
                    && currentColor != Marble.getTeammateColor(color))) {
                return false;
            }
        }

        Collections.sort(indexes);
        ArrayList<Pair<Integer, Integer>> cells = new ArrayList<>();
        for (Integer index : indexes) {
            cells.add(getCell(index));
        }

        return isColumn(cells) || isRow(cells) || isDiagonal(cells);
    }

    public Marble getField(int index) {
        Pair<Integer, Integer> p = INDEXCELL.get(index);
        return board[p.first()][p.second()];
    }

    public Pair<Integer, Integer> getCell(int index) {
        return INDEXCELL.get(index);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int tab = 10;
        int marbelsPerRow = 5;
        String[] splittedIndexesBoard = indexesBoard.split(";");

        for (int i = 0; i < 9; i++) {
            StringBuilder row = new StringBuilder();
            row.append(String.format("%" + tab + "s", ""));
            for (int j = 0; j < marbelsPerRow; j++) {
                row.append(board[i][j] != null ? " " + board[i][j].getColor() + "  " : " .  ");
            }
            row.append(String.format("%" + tab + "s", ""));
            row.append(splittedIndexesBoard[i]);
            row.append(System.lineSeparator());
            str.append(row);
            if (i < 4) {
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
        Board board = new Board(4);
        System.out.println(board.toString());
    }
}