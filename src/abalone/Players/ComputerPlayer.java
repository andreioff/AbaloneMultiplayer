package abalone.Players;

import abalone.Strategies.SmartStrategy;
import abalone.Strategies.Strategy;
import abalone.Game.Board;

public class ComputerPlayer extends Player {
    private Strategy strategy;

    public ComputerPlayer(String name, int players, int seconds) {
        super(name);
        strategy = new SmartStrategy(players, seconds);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        strategy.setColor(color);
    }

    public String determineMove(Board board) {
        return strategy.determineMove(board);
    }

}