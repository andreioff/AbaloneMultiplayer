package abalone.Game;

import java.util.*;

public class Board {

    /**
     * Maps a cell's coordinates to the corresponding index.
     */
    private Map<Pair, Integer> CELLINDEX;

    /**
     * Maps an index to the corresponding cell coordinates.
     */
    private Map<Integer, Pair> INDEXCELL;

    /**
     * Array of directions. Each pair of coordinates from this array represents a 2d vector that added to a current
     * position leads to a neighbour cell. The position of each direction respects the order from the protocol.
     */
    private static final Pair[] directions = {
                    new Pair(-1, 1), new Pair(0, 1), new Pair(1, 0),
                    new Pair(1, -1), new Pair(0, -1), new Pair(-1, 0)
    };

    /**
     * String representation of the game board with the corresponding index for each field.
     */
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

    /*
    Array that keeps track of the score of the current game for each player.
     */
    private int[] score;

    /*
    2D representation of the game's board. If a field is null, it means the field is empty, otherwise it contains a
    Marble object.
     */
    private Marble[][] board;

    /*
    The number of players of the current game
     */
    private int players;

    /**
     * Creates an empty board and fills the maps with the corresponding values.
     * @ensures board != null
     * @ensures INDEXCELL != null
     * @ensures CELLINDEX != null
     */
    public Board() {
        board = new Marble[9][9];
        setupHashMaps();
    }

    /**
     * Initialize the maps and add fills them with the corresponding values.
     * @ensures INDEXCELL != null
     * @ensures CELLINDEX != null
     */
    public void setupHashMaps() {
        INDEXCELL = new HashMap<>();
        CELLINDEX = new HashMap<>();
        Pair p;
        //we start from index 0, with 5 marbles per row and from column 4 (in the board matrix)
        int index = 0;
        int marbles = 5;
        int start = 4;

        //for each row of the board
        for (int i = 0; i < 9; i++) {
            //for each column of the board
            for (int j = 0; j < marbles; j++) {
                //create a new pair of coordinates for the specific index.
                p = new Pair(i, start + j);
                //map the index to the pair of coordinates
                INDEXCELL.put(index, p);
                //map the pair to the index
                CELLINDEX.put(p, index);
                //increase the index
                index++;
            }
            if (i < 4) {
                //if we are still above the middle line, increase the nr of marbles per row.
                marbles++;
                //add one more column to the left of the next row
                start--;
            } else {
                //if we crossed the middle line, start every row from the column 0 and decrease by 1 the nr of marbles
                marbles--;
            }
        }
    }

    /**
     * Setup the board for a 2 players game.
     * @requires board != null
     * @ensures board contains 28 Marble objects
     */
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

    /**
     * Setup the board for a 3 players game.
     * @requires board != null
     * @ensures board contains 33 Marble objects
     */
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

    /**
     * Setup the board for a 4 players game.
     * @requires board != null
     * @ensures board contains 36 Marble objects
     */
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
     @requires board != null
     @ensures score != null
     @ensures this.players >= 2 && this.players <= 2
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

    /**
     * Resets the board by making all fields null then setups the board for the current amount of players
     * @requires board != null
     */
    public void reset() {
        for (int i = 0; i < 61; i++) {
            setField(i, null);
        }
        setup(players);
    }

    /**
     * Checks if 2 or more cells in a matrix are positioned diagonally. If the list contains only 1 cell, returns true.
     * @requires cells is sorted
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @param cells = an ordered list of coordinates pairs from the smallest row and column indexes to the biggest,
     *              or vice versa
     * @returns false if the cells are not positioned diagonally or if the list is not ordered and true otherwise.
     */
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

    /**
     * Checks if 2 or more cells in a matrix are positioned in a horizontal row. If the list contains only 1 cell,
     * returns true.
     * @requires cells is sorted
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @param cells = an ordered list of coordinates pairs from the smallest row and column indexes to the biggest,
     *              or vice versa
     * @returns false if the cells are not positioned in a horizontal row or if the list is not ordered properly
     * and true otherwise.
     */
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

    /**
     * Checks if 2 or more cells in a matrix are positioned in a column. If the list contains only 1 cell,
     * returns true.
     * @requires cells is sorted
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @param cells = an ordered list of coordinates pairs from the smallest row and column indexes to the biggest,
     *              or vice versa
     * @returns false if the cells are not positioned in a column or if the list is not ordered properly
     * and true otherwise.
     */
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

    /**
     * Checks if the marbles selected are in a sumito position if they move in the direction specified by
     * directionIndex. If this is the case, it adds the opponent marbles into the list in order to be moved
     * with the selected marbles.
     * @param directionIndex = the index of the direction in which the selected marbles should move
     * @param cells = a list with the position of the marbles that should be moved, sorted by the direction
     *              in which they are moving. For example, the positions (0, 5), (0, 6), (0, 7) in the direction
     *              (0, 1) will be sorted as shown: (0, 7), (0, 6), (0, 5).
     * @param color = the color of the marbles that should be moved
     * @returns true if the selected marbles are in a sumito position and false otherwise.
     * @requires cells is sorted by direction
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @requires color >= 1 && color <= 4
     * @requires the next field to the selected marbles in the direction given is occupied by an opponent marble
     * @ensures the coordinates of the marbles that will be pushed are in the list with the marbles selected.
     * @ensures cells is sorted by direction
     */
    public boolean checkSumito(int directionIndex, List<Pair> cells, int color) {
        //get the first marble that will be moved
        Pair cell = cells.get(0);
        //check if the field behind this marble (the cell in the opposite direction) is selected in order
        //to perform the sumito.
        Pair behindCell = Pair.add(cell, Pair.opposite(directions[directionIndex]));
        if (!isField(behindCell) || !cells.contains(behindCell)) return false;

        //if a sumito can be performed, add the marble that will be pushed in the list
        Pair futurePos = Pair.add(cell, directions[directionIndex]);
        cells.add(0, futurePos);

        //check if the next position is off the board or empty
        futurePos = Pair.add(futurePos, directions[directionIndex]);
        if (!isField(futurePos)) return true;
        if (isEmptyField(futurePos)) return true;

        //if the field is not empty or off the board check if the color's marble that is in the field
        //is an opponent marble
        int futureField = getField(futurePos).getColorNr();
        if (cells.size() < 4 || futureField == color
                || (players == 4 && futureField == Marble.getTeammateColor(color)))
            return false;

        //if it is, add it in the list
        cells.add(0, futurePos);

        //check again if the marbles can be pushed off the board or in an empty field
        futurePos = Pair.add(futurePos, directions[directionIndex]);
        return !isField(futurePos) || isEmptyField(futurePos);
    }

    /**
     * Checks for the 4 players games if the inline moves can be made (the color that is playing is also pushing)
     * @param directionIndex = the index of the direction in which the selected marbles should move
     * @param cells = a list with the position of the marbles that should be moved, sorted by the direction
     *              in which they are moving. For example, the positions (0, 5), (0, 6), (0, 7) in the direction
     *              (0, 1) will be sorted as shown: (0, 7), (0, 6), (0, 5).
     * @param color = the color of the marbles that should be moved.
     * @returns false if the selected marbles are inline and cannot be move, and true otherwise.
     * @requires cells is sorted by direction
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @requires color >= 1 && color <= 4
     */
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

    /**
     * Checks if the selected marbles can be moved in the direction specified by the directionIndex. If it is a
     * sumito case, it also adds the marbles that will be pushed into the list of selected marbles.
     * @param directionIndex = the index of the direction in which the selected marbles should move
     * @param cells = a list with the position of the marbles that should be moved
     * @param color = the color of the marbles that should be moved.
     * @return true if the selection can be moved in the specified direction and false otherwise.
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @ensures cells is sorted by the direction given
     * @ensures the coordinates of the marbles that might be pushed are in the list with the marbles selected.
     */
    public boolean checkMove(int directionIndex, List<Pair> cells, int color) {
        //sort the coordinates by direction
        cells.sort(new CompareByDirection(directions[directionIndex]));
        if (!checkSelection(cells, color) || !checkInLineMove4Players(directionIndex, cells, color)) {
            return false;
        }

        Pair futurePos;
        int currentColor;
        for (Pair cell : cells) {
            //get the position in which the current cell will go
            futurePos = Pair.add(cell, directions[directionIndex]);
            if (!isField(futurePos)) return false;
            if (!isEmptyField(futurePos)) {
                if (cells.size() == 1) return false;

                currentColor = getField(futurePos).getColorNr();
                //if the future position of the cell is occupied by a marble with the same color or,
                //in a 4 players game, by a marble of the teammate that is not into the selection, return false
                if (currentColor == color || (players == 4 && currentColor == Marble.getTeammateColor(color))) {
                    if (!cells.contains(futurePos)) return false;
                } else {
                    //else it means that the future position of the current marble is occupied by an opponent marble
                    //and it might be a sumito case.
                    return checkSumito(directionIndex, cells, color);
                }
            }
        }
        return true;
    }

    /**
     * Checks if the marble from the given cell matches the given color.
     * @param cell = a valid pair of coordinates
     * @param color = the color that will be used for checking
     * @return true if the marble from the given cell has the same color as the given one or as the teammate color
     * and false otherwise.
     * @requires cell != null
     * @requires isField(cell) && !isEmptyField(cell)
     * @requires color >= 1 && color <= 4
     */
    public boolean colorMatch(Pair cell, int color) {
        int currentColor = getField(cell).getColorNr();
        if ((players < 4)) {
            return currentColor == color;
        }
        return currentColor == color || currentColor == Marble.getTeammateColor(color);
    }

    /**
     * Checks if the given list of marbles is a valid selection
     * @return true if the selected marbles are a valid selection and false otherwise
     * @requires cells != null
     * @requires cells.size() >= 1 && cells.size() <= 3
     * @requires color >= 1 && color <= 4
     */
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

        //if the game has 4 players and the current color chose only his teammate marbles, return false.
        if (players == 4 && !currentColorSelected) return false;

        return isColumn(cells) || isRow(cells) || isDiagonal(cells);
    }

    /**
     * Checks if the given indexes and direction are a valid move for the given color and, if that is the case,
     * it moves the marbles one position in the specified direction.
     * @param directionIndex = the index of the direction in which the selected marbles should move
     * @param indexes = a list with the indexes of the marbles that should be moved
     * @param color = the color of the marbles that should be moved.
     * @returns true if the selection is a valid move and the marbles were moved, and false otherwise.
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @requires indexes != null
     * @requires color >= 1 && color <= 4
     */
    public boolean move(int directionIndex, List<Integer> indexes, int color) {
        Collections.sort(indexes);
        ArrayList<Pair> cells = new ArrayList<>();
        for (Integer index : indexes) {
            cells.add(getCell(index));
        }

        if (checkMove(directionIndex, cells, color)) {
            moveMarbles(directionIndex, cells, color);
            return true;
        }
        return false;
    }

    /**
     * Increase the score of a specific color and its teammate (only in 4 players games)
     * @param color = the color that should get the score increased
     * @requires color >= 1 && color <= 4
     * @ensures score[color - 1] = old(score[color - 1]) + 1;
     */
    public void increaseScore(int color) {
        score[color - 1]++;
        if (players == 4) score[Marble.getTeammateColor(color) - 1]++;
    }

    /**
     * Moves the marble from the currentPos, one position in the direction specified by the directionIndex.
     * @param currentPos = a pair of coordinates where the current marble is situated.
     * @param directionIndex = the direction in which the marble should be moved
     * @param color = the color of the marble from the current position
     * @requires isField(currentPos) && (getField(currentPos).getColorNr() == color
     *                                   || getField(currentPos).getColorNr() == Marble.getTeammateColor(color))
     * @requires color >= 1 && color <= 4
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @ensures the marble from the current position is moved to the next cell in the given direction
     */
    public void updatePosition(Pair currentPos, int directionIndex, int color) {
        Pair futurePos = Pair.add(currentPos, directions[directionIndex]);
        if (isField(futurePos)) {
            setField(futurePos, getField(currentPos));
        } else {
            //add score for the color that made the move if it pushes the marble off the board
            increaseScore(color);
        }
        //set the field from where the marble was moved to null
        setField(currentPos, null);
    }

    /**
     * Moves all the marbles at the coordinates from the given list in the direction specified by the given
     * direction index, increasing the score of the given color if any of the enemy marbles are pushed off the board
     * @param directionIndex = the index of the direction on which the marbles should be move
     * @param cells = a list of pairs of coordinates of the marbles that should be moved
     * @param color = the color of which score will be increase if any of the enemy marbles are pushed off the board
     * @requires cells != null && checkMove(directionIndex, cells, color)
     * @requires color >= 1 && color <= 4
     * @requires directionIndex >= 0 && directionIndex <= 5
     * @ensures all given marbles are moved one position in the given direction
     * @ensures if a marble is pushed of the board ==> score[color - 1] = old(score[color - 1]) + 1;
     */
    public void moveMarbles(int directionIndex, List<Pair> cells, int color) {
        for (Pair cell : cells) {
            updatePosition(cell, directionIndex, color);
        }
    }

    /**
     * Returns the object from the field with the given index
     * @param index = the index of the field that should be returned
     * @returns a Marble object if the field with the given index contains a marble object and null otherwise
     * @requires isField(index)
     */
    public Marble getField(int index) {
        assert isField(index);
        Pair p = INDEXCELL.get(index);
        return board[p.first()][p.second()];
    }

    /**
     * Returns the object from the field with the given pair of coordinates
     * @param cell = the pair of coordinates of the field that should be returned
     * @returns a Marble object if the field with the given pair of coordinates contains a marble object
     *          and null otherwise
     * @requires isField(cell)
     */
    public Marble getField(Pair cell) {
        assert isField(cell);
        return board[cell.first()][cell.second()];
    }

    /**
     * Returns the pair of coordinates of the field with the given index
     * @param index = the index of a valid field
     * @return a Pair object with the coordinates of the field with the given index
     * @requires isField(index)
     * @ensures return != null
     */
    public Pair getCell(int index) {
        assert isField(index);
        return INDEXCELL.get(index);
    }

    /**
     * Returns the index of the field with the given pair of coordinates
     * @param cell = the pair of coordinates of a valid field
     * @returns an integer representing the index of the field with the given pair of coordinates
     * @requires isField(cell)
     * @ensures return != null
     */
    public int getIndex(Pair cell) {
        assert isField(cell);
        return CELLINDEX.get(cell);
    }

    /**
     * Returns the score array of the current game
     * @returns an array of integers with the score of the current game
     */
    public int[] getScore() {
        return score;
    }

    /**
     * Returns the direction at the given index from the array of directions of this object
     * @param index = the index of the direction that should be returned
     * @returns a Pair object containing the coordinates of the direction at the given index
     * @requires index >= 0 && index <= 5
     * @ensures return != null
     */
    public Pair getDirection(int index) {
        return directions[index];
    }

    /**
     * Returns a list of lists with all valid moves of the given color
     * @param color = the color the possible moves should be computed for
     * @return a list of lists with all the possible moves that the given color has for the current state of
     *         the game. The lists contain on the first position the direction of the move and, starting with
     *         the second position, between 1 and 3 indexes with marbles that matches the given color and can be moved
     *         in the direction from the first position.
     * @requires color >= 1 && color <= 4
     * @ensures return != null;
     * @ensures return.size() >= 1
     * @ensures for each list in return, list.size() >= 2 && list.size() <= 4
     */
    public List<List<Integer>> getAllValidMoves(int color) {
        List<List<Integer>> moves = new ArrayList<>();
        List<Pair> selection = new ArrayList<>();
        for (int i = 0; i < 61; i++) {
            if (!isEmptyField(i) && colorMatch(getCell(i), color)) {
                selection.add(getCell(i));
                validMoves(selection, color, moves);
                selection.clear();
            }
        }
        return moves;
    }

    /**
     * Adds to the given list all possible moves that contains the marbles from the given selection and can be made
     * by the given color.
     * @param selection = a list with coordinates pairs of the marbles that should be contained in a move
     * @param color = the color of the marbles that a move can have
     * @param moves = the list where the valid moves should be added. A valid move contains on the first position
     *              the direction of the move and, starting with the second position, between 1 and 3 indexes with
     *              marbles that matches the given color and can be moved in the direction from the first position.
     * @required selection.size() >= 1 && selection.size() <= 3
     * @requires color >= 1 && color <= 4
     * @requires moves != null
     * @ensures for each list in moves, list.size() >= 2 && list.size() <= 4
     */
    public void validMoves(List<Pair> selection, int color, List<List<Integer>> moves) {
        int size = selection.size();
        for (int i = 0; i < 6; i++) {
            if (checkMove(i, selection, color)) {
                while (selection.size() > size) {
                    selection.remove(0);
                }
                List<Integer> move = new ArrayList<>();
                for (Pair p : selection) {
                    move.add(getIndex(p));
                }
                move.add(0, i);
                if (move.size() == 4) moves.add(0, move);
                else moves.add(move);
            }
            selection.sort(new CompareByDirection(new Pair(-1, -1)));
            if (selection.size() < 3 && i >= 1 && i <= 3) {
                Pair lastElement = selection.get(selection.size() - 1);
                Pair futurePos = Pair.add(lastElement, directions[i]);
                if (isField(futurePos) && !isEmptyField(futurePos) && colorMatch(futurePos, color)) {
                    List<Pair> newSelection = new ArrayList<>();
                    for (Pair p : selection) {
                        newSelection.add(new Pair(p.first(), p.second()));
                    }
                    newSelection.add(futurePos);
                    validMoves(newSelection, color, moves);
                }
            }
        }
    }

    /**
     * Returns a deep copy of the current board.
     * @returns a new Board object identical with the values of this object
     * @ensures !return.equals(this)
     * @ensures all values of return are identical with this object
     */
    public Board deepCopy() {
        Board copy = new Board();
        copy.setup(players);
        copy.setScore(score);
        for (int i = 0; i < 61; i++) {
            if (isEmptyField(i)) {
                copy.setField(i, null);
            } else {
                copy.setField(i, new Marble(getField(i).getColorNr()));
            }
        }
        return copy;
    }

    /**
     * Set the score array's values to be equal with the values from the given array.
     * @param score = an array of integers with the length equals to the number of players of this object
     * @requires score.length() == this.players
     * @ensures values from this.score == values from score
     */
    public void setScore(int[] score) {
        for (int i = 0; i < players; i++) {
            this.score[i] = score[i];
        }
    }

    /**
     * Checks if the given index is a valid field on the board.
     * @param index = the index of the field to be checked
     * @return true if the current index is a valid field on the current board and false otherwise
     */
    public boolean isField(int index) {
        return INDEXCELL.containsKey(index);
    }

    /**
     * Checks if the given pair of coordinates is a valid field on the board.
     * @param p = the index of the field to be checked
     * @return true if the current index is a valid field on the current board and false otherwise
     */
    public boolean isField(Pair p) {
        return CELLINDEX.containsKey(p);
    }

    /**
     * Checks if the given color is a winner of the game.
     * @param color = the color to be checked
     * @returns true if the given color is a valid color and has pushed 6 marbles of the board and false otherwise
     */
    public boolean isWinner(int color) {
        return color >= 1 && color <= score.length && score[color - 1] == 6;
    }

    /**
     * Checks if one of the color from the current game is a winner
     * @return true if one of the playing colors is a winner(has pushed 6 marbles off the board) and false otherwise.
     */
    public boolean hasWinner() {
        for (int i = 1; i <= players; i++) {
            if (isWinner(i)) return true;
        }
        return false;
    }

    /**
     * Set the field at the given index with the given marble
     * @param index = the index of the field to be set
     * @param marble = a Marble object that the field should be set with
     * @requires isField(index)
     */
    public void setField(int index, Marble marble) {
        assert isField(index);
        Pair p = INDEXCELL.get(index);
        board[p.first()][p.second()] = marble;
    }

    /**
     * Set the field at the given pair of coordinates with the given marble
     * @param cell = the pair of coordinates of the field to be set
     * @param marble = a Marble object that the field should be set with
     * @requires isField(cell)
     */
    public void setField(Pair cell, Marble marble) {
        assert isField(cell);
        board[cell.first()][cell.second()] = marble;
    }

    /**
     * Checks if the field at the given pair of coordinates is an empty field
     * @param cell = the field's pair of coordinates
     * @return true if the field at the given pair of coordinates is an empty field and false otherwise
     * @requires isField(cell)
     */
    public boolean isEmptyField(Pair cell) {
        return getField(cell) == null;
    }

    /**
     * Checks if the field at the given index is an empty field
     * @param index = the field's index
     * @return true if the field at the given index is an empty field and false otherwise
     * @requires isField(index)
     */
    public boolean isEmptyField(int index) {
        return getField(index) == null;
    }

    /**
     * Returns the Map with indexes as keys and pairs of coordinates as values
     * @return the Map with indexes between 0 and 60 as keys and pairs of coordinates as values
     * @ensures return != null
     * @ensures return.size() == 61
     */
    public Map<Integer, Pair> getINDEXCELL() {
        return INDEXCELL;
    }

    /**
     * Returns the Map with pairs of coordinates as keys and indexes as values
     * @return the Map with pairs of coordinates as keys and indexes between 0 and 60 as values
     * @ensures return != null
     * @ensures return.size() == 61
     */
    public Map<Pair, Integer> getCELLINDEX() {
        return CELLINDEX;
    }

    /**
     * Returns a string representation of the current board.
     * @returns a String object representing the current state of the board where an empty field is marked with "." and
     * a marble by a one of the letters: "W", "B", "Y", "R"
     * @ensures return != null
     */
    @Override
    public String toString() {
        //board string representation
        StringBuilder str = new StringBuilder();
        int padding = 10;
        int marbelsPerRow = 5;
        int start = 4;
        //get each row of the board with indexes
        String[] splittedIndexesBoard = indexesBoard.split(";");

        //for each row
        for (int i = 0; i < 9; i++) {
            //create a new row string representation
            StringBuilder row = new StringBuilder();
            //add padding accordingly
            row.append(String.format("%" + padding + "s", ""));
            for (int j = 0; j < marbelsPerRow; j++) {
                //append to the row string representation a "." for an empty field or a letter for a marble
                row.append(board[i][start + j] != null ? " " + board[i][start + j].getColor().charAt(0) + "  "
                                                       : " .  ");
            }
            //add padding to the end of the row and append a line from the board with indexes
            row.append(String.format("%" + padding + "s", ""));
            row.append(splittedIndexesBoard[i]);
            row.append(System.lineSeparator());
            //append the new created row to the board string representation
            str.append(row);
            //if we did not cross the middle row decrease padding and starting column and increase the marbles per row
            if (i < 4) {
                start--;
                padding -= 2;
                marbelsPerRow++;
            } else {
                //else, increase padding and lower the number of marbles per row
                padding += 2;
                marbelsPerRow--;
            }
        }
        return str.toString();
    }

    /**
     * Returns a string array representation of the current board
     * @return a string object with only 1 row that contains all the field from the board, delimited by spaces, where:
     *         0 = empty field
     *         1 = a white marble
     *         2 = a black marble
     *         3 = a yellow marble
     *         4 = a red marble
     * @ensures return != null
     */
    public String toArrayString() {
        Marble field;
        int fieldColor;
        StringBuilder array = new StringBuilder().append('[');
        for (int i = 0; i < 61; i++) {
            field = getField(i);
            fieldColor = field == null ? 0 : field.getColorNr();
            array.append(fieldColor).append(",");
        }
        array.setCharAt(array.length() - 1, ']');

        return array.toString();
    }

    /**
     * Setup the current board with the fields from a string array representation of a board
     * @param array = a string array representation of a board
     * @requires array != null
     * @requires array.split(,).length() == 61
     * @requires all values delimited by commas from the given string are between 0 and 4, where:
     *           0 = empty field
     *           1 = a white marble
     *           2 = a black marble
     *           3 = a yellow marble
     *           4 = a red marble
     */
    public void setBoardFromArray(String array) {
        String[] splittedArray = array.split(",");
        for (int i = 0; i < 61; i++) {
            int color = Integer.parseInt(splittedArray[i]);
            setField(i, color == 0 ? null : new Marble(color));
        }
    }
}