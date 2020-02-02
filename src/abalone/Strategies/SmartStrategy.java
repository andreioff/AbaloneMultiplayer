package abalone.Strategies;

import abalone.Game.Board;
import abalone.Game.Marble;
import abalone.Game.Pair;

import java.util.ArrayList;
import java.util.List;

public class SmartStrategy implements Strategy {
    //the best move found in a search in the game tree
    private List<Integer> bestMove;
    //the best move found in all searches
    private List<Integer> globalBestMove;
    //the max depth of a search in the game tree
    private static final int DEPTH_SEARCH = 3;
    //the max time allowed for searching the global best move
    private int MAX_TIME;
    //the number of players of the game
    private int players;
    //flags if a timeout occurred during a search
    private boolean timeout;
    //the time when the search started
    private long startTime;
    //the color that has to make the best move
    private int color;

    /**
        Creates a new SmartStrategy object
        @param players = the number of players of the game
        @param seconds = the nr of seconds allowed for calculations
        @requires seconds >= 3
        @requires players >= 2 && players <= 4
     */
    public SmartStrategy(int players, int seconds) {
        this.players = players;
        bestMove = new ArrayList<>();
        globalBestMove = new ArrayList<>();
        MAX_TIME = seconds;
    }

    /**
     * Set the color that has to make the best move
     * @param color = the color that this.color should be set to
     * @requires color >= 1 && color <= 4
     */
    @Override
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Determines the best move that the color that is playing can make.
     * @param board = the board of the game
     * @returns a string with the best move that can be made
     */
    @Override
    public String determineMove(Board board) {
        timeout = false;
        globalBestMove.clear();
        bestMove.clear();
        //start the timer
        startTime = System.currentTimeMillis();
        long stopTime;
        //increase the depth of the tree while the timeout flag is not set (iterative deepening)
        for (int d = 0;; d++) {
            //apply the minimax algorithm for the current max depth
            maxi(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH_SEARCH + d, color);
            if (!timeout) {
                //if the search finished without a timeout, change the global best move to the current best move found
                globalBestMove.clear();
                globalBestMove.addAll(bestMove);
            }
            //check how much time passed
            stopTime = System.currentTimeMillis();
            int timePassed = (int)(stopTime - startTime) / 1000;
            //if too much time passed, break the loop
            if (timePassed >= MAX_TIME) break;
        }

        //build the move string and return it
        StringBuilder move = new StringBuilder();
        move.append(globalBestMove.get(0));
        move.append(";[");
        globalBestMove.remove(0);
        for (Integer i : globalBestMove) {
            move.append(i);
            move.append(',');
        }
        move.setCharAt(move.length() - 1, ']');
        return move.toString();
    }

    /**
     * Minimax function that tries to maximise the score of a move
     * @param board = the board of the game
     * @param evaluation = the current move evaluation
     * @param alpha = the biggest score encountered so far (part of alpha beta pruning)
     * @param beta = the lowest score encountered so far (part of alpha beta pruning)
     * @param depth = how far is the function from the maximum depth
     * @param color = the current color that makes the move
     * @returns the biggest score of the best move encountered
     */
    private int maxi(Board board, int evaluation, int alpha, int beta, int depth, int color) {
        int timePassed = (int)(System.currentTimeMillis() - startTime) / 1000;
        //set the timeout flag if too much time has passed and return the alpha value
        if (timePassed >= MAX_TIME) {
            timeout = true;
            return alpha;
        }
        //return the current move evaluation if the max depth was reached
        if (depth == 0) {
            return evaluation;
        }

        List<List<Integer>> validMoves = board.getAllValidMoves(color);
        //for each valid move, make the move on a copy of the board, evaluate the move, get the rating of the move and
        //change the alpha value if a bigger rating was found. Change the best move if a bigger rating was found
        //at the top of the game tree
        for (List<Integer> move : validMoves) {
            Board copy = board.deepCopy();
            int direction = move.get(0);
            move.remove(0);
            int[] oldScore = copy.getScore();
            copy.move(direction, move, color);
            int eval = evaluateBoard(copy, oldScore, color);
            int rating = mini(copy.deepCopy(), eval, alpha, beta,
                    depth - 1, Marble.getNextColor(color, players));
            if (rating > alpha) {
                alpha = rating;
                if (depth == DEPTH_SEARCH) {
                    bestMove.clear();
                    bestMove.addAll(move);
                    bestMove.add(0, direction);
                }
            }
            if (alpha >= beta) return alpha;
        }
        return alpha;
    }

    /**
     * Minimax function that tries to minimize the score of a move. Same as the maxi method but this time the value of
     * beta changes.
     * @param board = the board of the game
     * @param evaluation = the current move evaluation
     * @param alpha = the biggest score encountered so far (part of alpha beta pruning)
     * @param beta = the lowest score encountered so far (part of alpha beta pruning)
     * @param depth = how far is the function from the maximum depth
     * @param color = the current color that makes the move
     * @returns the smallest score of the best move encountered
     */
    private int mini(Board board, int evaluation, int alpha, int beta, int depth, int color) {
        int timePassed = (int)(System.currentTimeMillis() - startTime) / 1000;
        if (timePassed >= MAX_TIME) {
            timeout = true;
            return beta;
        }
        if (depth == 0) {
            return evaluation;
        }
        List<List<Integer>> validMoves = board.getAllValidMoves(color);
        for (List<Integer> move : validMoves) {
            Board copy = board.deepCopy();
            int direction = move.get(0);
            move.remove(0);
            int[] oldScore = copy.getScore();
            copy.move(direction, move, color);
            int eval = evaluateBoard(copy, oldScore, color);
            int rating = maxi(copy.deepCopy(), eval, alpha, beta,
                    depth - 1, Marble.getNextColor(color, players));
            if (rating <= beta) {
                beta = rating;
            }

            if (alpha >= beta) return beta;
        }
        return beta;
    }

    /**
     * Evaluates the score of the current board situation for the given color.
     * @param board = the board of the game
     * @param oldScore = the score before the current move was made
     * @param color = the color that should be evaluated
     * @returns the score of the current board situation for the given color
     */
    private int evaluateBoard(Board board, int[] oldScore, int color) {
        int score = 0;
        int oppColor = Marble.getNextColor(color, players);
        if (board.isWinner(color)) score += 500;
        if (board.isWinner(oppColor)) score -= 500;
        //if the current color pushed off a marble, add score
        if (board.getScore()[color - 1] > oldScore[color - 1]) score += 10;
        //if the opponent color pushed off a marble, subtract score for the current color.
        if (board.getScore()[oppColor - 1] > oldScore[oppColor - 1]) score -= 10;
        Pair currentCell;
        /*
        calculate the sum of all distances of current color's marbles from the center, how grouped is the
        current color's marbles (the number of neighbours of the same color of each marble) and how grouped is the
        opponent marbles.
         */

        int centerDistance = 0, ownGrouping = 0, oppGrouping = 0;
        for (int i = 0; i < 61; i++) {
            if (!board.isEmptyField(i)) {
                currentCell = board.getCell(i);
                Pair futurePos;
                if (board.getField(i).getColorNr() == color) {
                    centerDistance += (Math.abs(currentCell.first() - 4) + Math.abs(currentCell.second() - 4));
                    for (int j = 0; j < 6; j++) {
                        futurePos = Pair.add(currentCell, board.getDirection(j));
                        if (board.isField(futurePos) && !board.isEmptyField(futurePos)
                                && board.getField(futurePos).getColorNr() == color) {
                            ownGrouping++;
                        }
                    }
                } else {
                    for (int j = 0; j < 6; j++) {
                        futurePos = Pair.add(currentCell, board.getDirection(j));
                        if (board.isField(futurePos) && !board.isEmptyField(futurePos)
                                && board.getField(futurePos).getColorNr() == oppColor) {
                            oppGrouping++;
                        }
                    }
                }
            }
        }
        /*
            for the final score, add score to the current move if the distance from the center to each current color's
            marbles is smaller (the smaller the distance, the bigger the score and vice versa) (56 is the maximum
            sum of distances to the center that can be achived. Subtracting the sum of distances calculated from this
            values gives bigger score if the marbles are close to the center and bigger score if they are further),
            add the calculated value of own grouping (the better grouped up the higher the score) and subtract from the
            score how good is the grouping of the opponent.
         */
        score = score + 56 - centerDistance + ownGrouping - oppGrouping;
        return score;
    }
}
