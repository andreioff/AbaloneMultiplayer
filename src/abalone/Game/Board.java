package abalone.Game;

import abalone.Game.Marble;

import java.util.*;

public class Board {
    private Map<Pair, Integer> CELLINDEX;
    private Map<Integer, Pair> INDEXCELL;
    private static final Pair[] directions = {
                    new Pair(-1, 1), new Pair(0, 1), new Pair(1, 0),
                    new Pair(1, -1), new Pair(0, -1), new Pair(-1, 0)
    };
    private static final String indexesBoard =
              "           00  01  02  03  04;"
            + "         05  06  07  08  09  10;"
            + "       11  12  13  14  15  16  17;"
            + "     18  19  20  21  22  23  24  25;"
            + "   26  27  28  29  30  31  32  33  34;"
            + "     35  36  37  38  39  40  41  42;"
            + "       43  44  45  46  47  48  49;"
            + "         50  51  52  53  54  55;"
            + "           56  57  58  59  60";

    private int[] score;
    private Marble[][] board;
    int players;

    public Board() {
        board = new Marble[9][9];
        setupHashMaps();
    }

    public void setupHashMaps() {
        INDEXCELL = new HashMap<>();
        CELLINDEX = new HashMap<>();
        Pair p;
        int index = 0;
        int marbles = 5;
        int start = 4;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < marbles; j++) {
                p = new Pair(i, start + j);
                INDEXCELL.put(index, p);
                CELLINDEX.put(p, index);
                index++;
            }
            if (i < 4) {
                marbles++;
                start--;
            } else {
                marbles--;
            }
        }
    }

    public void setup2() {
        Pair p;

        for (int i = 0; i <= 10; i++) {
            p = getCell(i);
            board[p.first()][p.second()] = new Marble(1);

            p = getCell(60 - i);
            board[p.first()][p.second()] = new Marble(2);
        }

        for (int i = 13; i <= 15; i++) {
            p = getCell(i);
            board[p.first()][p.second()] = new Marble(1);

            p = getCell(32 + i);
            board[p.first()][p.second()] = new Marble(2);
        }
    }

    public void setup3() {
        int marbels = 5;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < marbels; j++) {
                board[j][4 + i - j] = new Marble(1);
                board[j][8 - i] = new Marble(2);
                board[8 - i][j] = new Marble(3);
            }
            marbels++;
        }
    }

    public void setup4() {
        for (int i = 4; i >= 2; i--) {
            for (int j = 0; j < i; j++) {
                board[4 - i][j + 4] = new Marble(1);
                board[5 + j - i][i + 4] = new Marble(2);
                board[4 + i][5 + j - i] = new Marble(3);
                board[4 + j][4 - i] = new Marble(4);
            }
        }
    }

    /**
     @requires players >= 2 && players <= 4
     */
    public void setup(int players) {
        this.players = players;
        score = new int[players];
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

    public void reset() {
        for (int i = 0; i < 61; i++) {
            setField(i, null);
        }
        setup(players);
    }

    public boolean isDiagonal(List<Pair> cells) {
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

    public boolean isRow(List<Pair> cells) {
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

    public boolean isColumn(List<Pair> cells) {
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

    public boolean checkSumito(int directionIndex, List<Pair> cells, int color) {
        Pair cell = cells.get(0);
        Pair behindCell = Pair.add(cell, Pair.opposite(directions[directionIndex]));
        if (!isField(behindCell) || !cells.contains(behindCell)) return false;

        Pair futurePos = Pair.add(cell, directions[directionIndex]);
        cells.add(0, futurePos);

        futurePos = Pair.add(futurePos, directions[directionIndex]);
        if (!isField(futurePos)) return true;
        if (isEmptyField(futurePos)) return true;

        int futureField = getField(futurePos).getColorNr();
        if (cells.size() < 4 || futureField == color
                || (players == 4 && futureField == Marble.getTeammateColor(color)))
            return false;

        cells.add(0, futurePos);

        futurePos = Pair.add(futurePos, directions[directionIndex]);
        return !isField(futurePos) || isEmptyField(futurePos);
    }

    public boolean checkInLineMove4Players(int directionIndex, List<Pair> cells, int color) {
        if (players != 4) return true;

        int lastMarbleColor = getField(cells.get(cells.size() - 1)).getColorNr();
        if (isDiagonal(cells)) {
            if ((directionIndex == 0 || directionIndex == 3) && lastMarbleColor != color) return false;
        }

        if (isRow(cells)) {
            if ((directionIndex == 1 || directionIndex == 4) && lastMarbleColor != color) return false;
        }

        if (isColumn(cells)) {
            if ((directionIndex == 2 || directionIndex == 5) && lastMarbleColor != color) return false;
        }

        return true;
    }

    public boolean checkMove(int directionIndex, List<Pair> cells, int color) {
        if (!checkSelection(cells, color) || !checkInLineMove4Players(directionIndex, cells, color)) {
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
                if (currentColor == color || (players == 4 && currentColor == Marble.getTeammateColor(color))) {
                    if (!cells.contains(futurePos)) return false;
                } else return checkSumito(directionIndex, cells, color);
            }
        }
        return true;
    }

    public boolean colorMatch(Pair cell, int color) {
        int currentColor = getField(cell).getColorNr();
        if ((players < 4)) {
            return currentColor == color;
        }
        return currentColor == color || currentColor == Marble.getTeammateColor(color);
    }

    public boolean checkSelection(List<Pair> cells, int color) {
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
        ArrayList<Pair> cells = new ArrayList<>();
        for (Integer index : indexes) {
            cells.add(getCell(index));
        }
        cells.sort(new CompareByDirection(directions[directionIndex]));

        if (checkMove(directionIndex, cells, color)) {
            moveMarbles(directionIndex, cells, color);
            return true;
        }
        return false;
    }

    public void increaseScore(int color) {
        score[color - 1]++;
        if (players == 4) score[Marble.getTeammateColor(color) - 1]++;
    }

    public void updatePosition(Pair currentPos, int directionIndex, int color) {
        Pair futurePos = Pair.add(currentPos, directions[directionIndex]);
        if (isField(futurePos)) {
            setField(futurePos, getField(currentPos));
        } else {
            increaseScore(color);
        }
        setField(currentPos, null);
    }

    public void moveMarbles(int directionIndex, List<Pair> cells, int color) {
        for (Pair cell : cells) {
            updatePosition(cell, directionIndex, color);
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
        return color <= score.length && score[color - 1] == 6;
    }

    public boolean hasWinner() {
        for (int i = 1; i <= players; i++) {
            if (isWinner(i)) return true;
        }
        return false;
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

    public Map<Integer, Pair> getINDEXCELL() {
        return INDEXCELL;
    }

    public Map<Pair, Integer> getCELLINDEX() {
        return CELLINDEX;
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

    public String toArrayString() {
        Marble field;
        int fieldColor;
        StringBuilder array = new StringBuilder().append('[');
        for (int i = 0; i < 61; i++) {
            field = getField(i);
            fieldColor = field == null ? 0 : field.getColorNr();
            array.append(Integer.toString(fieldColor)).append(",");
        }
        array.setCharAt(array.length() - 1, ']');

        return array.toString();
    }

    public void setBoardFromArray(String array) {
        String[] splittedArray = array.split(",");
        for (int i = 0; i < 61; i++) {
            int color = Integer.parseInt(splittedArray[i]);
            setField(i, color == 0 ? null : new Marble(color));
        }
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.setup(2);
        System.out.println(board.toString());
        List<Integer> marbles = new ArrayList<>(Arrays.asList(50));
        board.move(0, marbles, 2);
        marbles = new ArrayList<>(Arrays.asList(44, 51, 57));
        board.move(5, marbles, 2);
        System.out.println(board.toString());
    }
}