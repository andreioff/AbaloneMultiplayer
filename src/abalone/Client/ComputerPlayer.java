package abalone.Client;

import abalone.Game.Board;
import abalone.Game.Marble;
import abalone.Game.Pair;

import java.util.ArrayList;
import java.util.List;

public class ComputerPlayer extends Player {
    private List<Integer> bestMove;
    private static final int DEPTH_SEARCH = 3;
    private int players;

    public ComputerPlayer(String name, GameClientView view, int color, int players) {
        super(name, view);
        setColor(color);
        this.players = players;
        bestMove = new ArrayList<>();
    }

    @Override
    public String determineMove(Board board, GameClientView view) {
        maxi(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH_SEARCH, getColor());
        StringBuilder move = new StringBuilder();
        move.append(bestMove.get(0));
        move.append(";[");
        bestMove.remove(0);
        for (Integer i : bestMove) {
            move.append(i);
            move.append(',');
        }
        move.setCharAt(move.length() - 1, ']');
        return move.toString();
    }

    private int maxi(Board board, int evaluation, int alpha, int beta, int depth, int color) {
        if (depth == 0 || board.hasWinner()) {
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

    private int mini(Board board, int evaluation, int alpha, int beta, int depth, int color) {
        if (depth == 0 || board.hasWinner()) {
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

    private int evaluateBoard(Board board, int[] oldScore, int color) {
        int score = 0;
        int oppColor = Marble.getNextColor(color, players);
        if (board.isWinner(color)) score += 500;
        if (board.isWinner(oppColor)) score -= 500;
        if (board.getScore()[color - 1] > oldScore[color - 1]) score += 10;
        if (board.getScore()[oppColor - 1] > oldScore[oppColor - 1]) score -= 10;
        Pair currentCell;
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
        score = score + 56 - centerDistance + ownGrouping - oppGrouping;
        return score;
    }
}
