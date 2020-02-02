package abalone.Players;

import abalone.Game.Board;
import abalone.Game.Marble;

import java.util.List;

public  abstract class Player {

    private String name;
    private int color;

    /**
     * Creates a new player object.
     * @requires name != null.
     * @ensures this.name != null.
     **/

    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     * @returns the name of the player.
     * @ensures return != null
     **/
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     * @requires name != null
     * @ensures this.name != null
     **/
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the color of the player.
     * @requires color >= 1 && color <= 4
     * @ensures this.color >= 1 && this.color <= 4
     **/
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Gets the color as a string.
     * @return a string with the color of the marble that corresponds to the player.
     */
    public String getColorString() {
        return Marble.colors[color - 1];
    }

    /**
     * Returns the number of the color of this player
     * @returns the number of the color of this player
     * @ensures return >= 1 && return <= 4
     */
    public int getColor() {
        return color;
    }

    /**
     * Abstract method for determining the move.
     * @param board = the board of the game
     * @returns the chosen move as a String object
     * @requires board != null
     */
    public abstract String determineMove(Board board);

}
