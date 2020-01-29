package abalone.Client;

import abalone.Game.Board;
import abalone.Game.Marble;

import java.util.List;

public  abstract class Player {

    private String name;
    private int color;
    private GameClientView view;
    /**
     * Creates a new player object.
     * @requires name is not null.
     * @requires the nr of the color.
     * @ensures the name of the player.
     * @ensures the player's colour.
     **/

    public Player(String name, GameClientView view) {
        this.name = name;
        this.view = view;
    }

    /**
     * Creates a new player object.
     * @requires name is not null.
     * @ensures this.name != null.
     **/

    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the player.
     * @return the name of the player.
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
     * Gets the color.
     * @return the color of the marble that corresponds to the player.
     */
    public String getColorString() {
        return Marble.colors[color - 1];
    }

    public int getColor() {
        return color;
    }

    /**
     * Abstract method for determining the move.
     * @param board != null
     * @return the move made
     */
    public abstract String determineMove(Board board, GameClientView view);

}
