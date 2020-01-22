import utils.TextIO;
import Marble.*;
import static Marble.Marble.getTeammateColor;


public class Game {
    private Player name;

    private int turns;

    private Board board;

    private Player[] players;

    private int nrPlayers;

    public int current;

    public Game(int nrPlayers) {
        board = new Board(nrPlayers);
        current = 0;
        players = new Player[nrPlayers];

        for (int i = 0; i <= nrPlayers; i++) {
            String input = TextIO.getlnString();
            if(input.equals("-H")){
                players[i] = new HumanPlayer(name.getName(), i+1 );
            }
            else if (input.equals("-C")) {
                players[i] = new ComputerPlayer(name.getName(), i+1 );
            }

        }
    }
    /**
     * @requires nrPlayers > 0
    Starts the game.
     **/

    public void start() {
        boolean continueGame = true;
        while (continueGame) {
            board.reset();
            play();
            System.out.println("Do you want a rematch?(y/n)");
            continueGame = TextIO.getBoolean() ;
        }
    }


    /**
     * Displays current state of the game.
     */
    public void update() {
        System.out.println("\nThis is the current game situation: \n\n" + board.toString()
                + "\n");
    }

    /**
     *  Plays the game.
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
     * @requires score != null
     * Prints the result of the game.
     */
    public void printOutcome() {
        int[] score = board.getScore();
        int max = score[0];
        int index = 0;
        int i;
        boolean wasEqual =  false;
        for (i = 0; i <nrPlayers; i++) {
            if (max == score[i]) {
                wasEqual = true;
            }
            if (score[i] > max) {
                wasEqual = false;
                max = score[i];
                index = i;
            }

        }
        if (wasEqual && index == 0)
            System.out.println("The game ended in a draw.");

        if (nrPlayers < 4) {
            System.out.println(Marble.colors[index]);
        } else if (nrPlayers == 4) {
            System.out.println(Marble.colors[index] + "&&" + Marble.colors[getTeammateColor(index)]);
        }
    }

    /**
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean gameOver() {
        return (board.hasWinner() || turns == 96);

    }
}






