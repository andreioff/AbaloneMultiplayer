import static abalone.Marble.Marble.getTeammateColor;

import abalone.Board;
import abalone.Marble.Marble;
import utils.TextIO;



public class Game {
    /**
     * the number of the turns.
     **/
    private int turns;

    /**
     * the board.
     **/
    private Board board;

    /**
     * the players array.
     **/
    private Player[] players;

    /**
     * the number of players.
     **/
    private int nrPlayers;

    /**
     * the index of the current player.
     **/
    private int current;

    /**
     * Game class constructor.
     **/
    public Game(Player[] playerArr) {
        board = new Board();
        current = 0;
        players = playerArr;
        nrPlayers = players.length;
        board.setup(nrPlayers);

    }

    /**
     * Starts the game.
     * @requires nrPlayers > 0
     **/

    public void start() {

        boolean continueGame = true;
        while (continueGame) {
            board.reset();
            play();
            System.out.println("Do you want a rematch?(y/n)");
            continueGame = TextIO.getBoolean();
        }
    }

    /**
     * Displays current state of the game.
     */
    public void update() {
        System.out.println("\nThis is the current game situation: \n\n"
                + board.toString() + "\n");
    }

    /**
     * Plays the game.
     */

    public void play() {
        turns = 0;
        update();
        while (!gameOver()) {
            while (current <= nrPlayers) {
                players[current].makeMove(board);
                turns++;
                current++;
                update();
            }
            current = 0;


        }
        printOutcome();
    }

    /**
     * score != null
     * Prints the result of the game.
     */
    public String printOutcome() {
        int[] score = board.getScore();
        int max = score[0];
        int index = 0;
        int i;
        boolean wasEqual = false;
        for (i = 1; i < nrPlayers; i++) {
            if (max == score[i]) {
                wasEqual = true;
            }
            if (score[i] > max) {
                wasEqual = false;
                max = score[i];
                index = i;
            }
        }
        if (nrPlayers == 4) {
            return Marble.colors[index] + "&&" + Marble.colors[getTeammateColor(index + 1) - 1];

        } else if (wasEqual && index == 0) {
            return "The game ended in a draw.";

        } else if (nrPlayers < 4) {
            return Marble.colors[index];

        }
        return "Error";
    }


    /**
     * Returns whether the game is over or not.
     * @return true if the game is over, false otherwise.
     */
    public boolean gameOver() {
        return (board.hasWinner() || turns == 96);

    }

    /**
     * Returns the board.
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {
        Player[] players = {new HumanPlayer("Marcel", 1), new HumanPlayer("Nelson", 2)};
        Game newGame = new Game(players);
        newGame.start();

    }

}




