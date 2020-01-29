package abalone.Client;

import abalone.Game.Board;
import abalone.Protocol.ProtocolMessages;

public class HumanPlayer extends Player {

    /**
     * Creates a new player object.
     *
     * @param name != null
     * @requires name is not null
     * @requires the number of the color
     * @ensures the name of the player
     * @ensures the player's color
     */
    public HumanPlayer(String name, GameClientView view) {
        super(name, view);
    }

    @Override
    public String determineMove(Board board, GameClientView view) {

        String prompt = "Make sure you type a list of at most 3 indexes delimited by spaces.";
        String choice = view.getString(prompt);

        String pattern = "^(\\d+([ ]?\\d)*)$";
        String[] splittedChoice =  choice.split(" ");
        while (!choice.matches(pattern) || splittedChoice.length < 1 || splittedChoice.length > 3) {
            choice = view.getString("Wrong input format or nr of marbles specified is out of bounds.");
            splittedChoice = choice.split(" ");
        }

        String dir = "Please choose direction between 0 and 5: ";
        int dirChoice = view.getInt(dir);

        while (!(dirChoice >= 0 && dirChoice <= 5)) {
            dirChoice = view.getInt("Invalid direction! " + dir);
        }

        StringBuilder move = new StringBuilder();
        move.append(dirChoice);
        move.append(ProtocolMessages.DELIMITER + '[');
        for (String index : splittedChoice) {
            move.append(index + ',');
        }
        move.setCharAt(move.length() - 1, ']');
        return move.toString();
    }
}
