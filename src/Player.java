
import java.util.List;
import Marble.Marble;
public  abstract class Player {

    private String name;
    private int color;
/**
 * Creates a new player object
 * @requires name is not null
 * @requires the nr of the color
 * @ensures the name of the player
 * @ensures the player's colour
 */
    public Player(String name, int color) {
        this.name = name;
        this.color = color;
    }

    /**
     * @requires name != null
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * @requires color != null
     * @return the color of the marble that corresponds to the player.
     */
    public String getColorString() {
        return Marble.colors[color - 1];
    }



    /**
     *
     * @param board
     * @return the move made
     */
    public abstract List<Integer> determineMove(Board board);

    /**
     * Makes a valid move.
     * @param board
     */
    public void makeMove(Board board) {
        List<Integer> playerMove = determineMove(board);
        int direction = playerMove.get(0);
        playerMove.remove(0);
        board.move(direction, playerMove, color);



    }


}
