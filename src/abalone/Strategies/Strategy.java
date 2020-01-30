package abalone.Strategies;

import abalone.Game.Board;

public interface Strategy {
    void setColor(int color);
    String determineMove(Board board);
}
