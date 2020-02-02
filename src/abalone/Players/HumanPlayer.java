package abalone.Players;

import abalone.Client.GameClientView;
import abalone.Game.Board;
import abalone.Protocol.ProtocolMessages;

public class HumanPlayer extends Player {
    private GameClientView view;
    /**
     * Creates a new human player object.
     *
     * @param name = the name that this player should have
     * @param view = the view that this player should use to ask the user to input a move
     * @requires name != null
     * @requires view != null
     */
    public HumanPlayer(String name, GameClientView view) {
        super(name);
        this.view = view;
    }

    /**
     * Continuously ask the user to input a list of indexes in the correct format and a valid direction and returns
     * them as a string
     * @param board = the board of the game
     * @returns a String object containing the move entered by the user.
     * @ensures return != null
     */
    @Override
    public String determineMove(Board board) {

        String prompt = "You can type now! Make sure your input is a list of, at most, 3 indexes delimited by spaces.";
        String choice = view.getString(prompt);

        String pattern = "^(\\d+([ ]?\\d)*)$";
        String[] splittedChoice =  choice.split(" ");
        while (!choice.matches(pattern) || splittedChoice.length < 1 || splittedChoice.length > 3) {
            choice = view.getString("Wrong input format or nr of marbles specified is out of bounds.");
            splittedChoice = choice.split(" ");
        }

        view.showMessage(getDirectionsLegend());
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

    /**
     * Returns a list that explains where each direction points to.
     * @return a String object containing the explanation where each direction points to.
     * @ensures return != null
     */
    private String getDirectionsLegend() {
        return "Here is the list with the possible directions:\n" +
                "0 -> top-right\n" +
                "1 -> right\n" +
                "2 -> bottom-right\n" +
                "3 -> bottom-left\n" +
                "4 -> left\n" +
                "5 -> top-left";
    }
}
