package abalone.Server;

import abalone.Exceptions.ClientDisconnected;
import abalone.Game.Board;
import abalone.Protocol.ProtocolMessages;

import java.util.List;

import static abalone.Game.Marble.getTeammateColor;

public class GameHandler implements Runnable {
    private static final int NUMBER_OF_TURNS = 96;
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
    private List<GameClientHandler> players;

    /**
     * the number of players.
     **/
    private int nrPlayers;

    /**
     * the index of the current player.
     **/
    private int current;

    /**
     * Creates a new GameHandler
     * @param playerArr = the list of GameClientHandlers that will take part in this game
     **/
    public GameHandler(List<GameClientHandler> playerArr) {
        board = new Board();
        current = 0;
        players = playerArr;
        nrPlayers = players.size();
        board.setup(nrPlayers);
        setPlayersInGameStatus(true);
    }

    /**
     * Starts the game.
     * @requires nrPlayers >= 2 && nrPlayers <= 4
     **/

    public void run() {
        board.reset();
        play();
    }

    /**
     * Sends to each client the board and who is the next player
     */
    public void update() {
        String sendBoard = ProtocolMessages.BOARD + ProtocolMessages.DELIMITER + board.toArrayString();
        String sendNext = ProtocolMessages.NEXT + ProtocolMessages.DELIMITER + players.get(current).getName();
        for (GameClientHandler player : players) {
            player.sendNotification(sendBoard);
            player.sendNotification(sendNext);
        }
    }

    /**
     * Plays the game.
     */

    public void play() {
        try {
            turns = 0;
            while (!gameOver()) {
                update();
                players.get(current).makeMove(board, current + 1);
                turns++;
                current = (current + 1) % nrPlayers;
            }
            setPlayersInGameStatus(false);
            sendMessage(getOutcome());
        } catch (ClientDisconnected e) {
            String name = e.getMessage();
            kickPlayer(name);
            setPlayersInGameStatus(false);
            sendMessage(ProtocolMessages.DISCONNECT + ProtocolMessages.DELIMITER + name);
            sendMessage(ProtocolMessages.END + ProtocolMessages.DELIMITER);
        }
    }

    public void kickPlayer(String name) {
        players.removeIf(client -> client.getName().contentEquals(name));
    }

    /**
     * @returns the outcome of the game (winner/s name/s or empty string for draw)
     */
    public String getOutcome() {
        String outcome = ProtocolMessages.END + ProtocolMessages.DELIMITER;
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
            outcome += players.get(index).getName() + " && " + players.get(getTeammateColor(index + 1) - 1).getName();
        } else if (wasEqual && index == 0) {
            outcome +=  "";
        } else {
            outcome += players.get(index).getName();
        }
        return outcome;
    }

    /**
     * Sets the in game status of each client handler to the speficied value
     * @param value the value that the status should be set to
     */
    private void setPlayersInGameStatus(boolean value) {
        for (GameClientHandler player : players) {
            player.setInGameStatus(value);
        }
    }

    /**
     * Sends to each client the given msg
     * @param msg = the message to be sent
     */
    public void sendMessage(String msg) {
        for (GameClientHandler player : players) {
            player.sendNotification(msg);
        }
    }

    /**
     * Returns whether the game is over or not.
     * @returns true if the game is over, false otherwise.
     */
    public boolean gameOver() {
        return (board.hasWinner() || turns == NUMBER_OF_TURNS);
    }

    /**
     * Returns the board.
     * @returns the board
     * @ensures return != null
     */
    public Board getBoard() {
        return board;
    }
}




