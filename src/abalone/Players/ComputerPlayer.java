package abalone.Players;

import abalone.Strategies.SmartStrategy;
import abalone.Strategies.Strategy;
import abalone.Game.Board;

public class ComputerPlayer extends Player {
    private Strategy strategy;

    /**
     * Creates a new computer player.
     * @param name = the name that this player should have
     * @param players = the number of players of the game for which the player is created
     * @param seconds = the number of seconds that the AI should spend calculating a valid move
     */
    public ComputerPlayer(String name, int players, int seconds) {
        super(name);
        strategy = new SmartStrategy(players, seconds);
    }

    /**
     * Sets the color of the player.
     * @param color = the color that this player should have.
     */
    @Override
    public void setColor(int color) {
        super.setColor(color);
        strategy.setColor(color);
    }

    /**
     * Uses the strategy to calculate a valid move and returns it.
     * @param board = the board of the game
     * @returns a String object containing the move that the strategy has chose.
     * @requires board != null
     */
    public String determineMove(Board board) {
        return strategy.determineMove(board);
    }

}