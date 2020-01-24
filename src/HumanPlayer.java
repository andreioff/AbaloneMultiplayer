import abalone.Board;
import java.util.ArrayList;

import java.util.List;

import org.w3c.dom.DOMStringList;
import utils.TextIO;


public class HumanPlayer extends Player {

    /**
     * Creates a new player object.
     *
     * @param name != null
     * @param color != null
     * @requires name is not null
     * @requires the number of the color
     * @ensures the name of the player
     * @ensures the player's color
     */
    public HumanPlayer(String name, int color) {
        super(name, color);
    }

    @Override
    public List<Integer> determineMove(Board board) {

        String prompt = "> " + getName() + " (" + getColorString() + ")"
                + ", what is your choice? Make sure a list of indexes delimited by spaces";

        System.out.println(prompt);
        String choice = TextIO.getlnString();

        String pattern = "^(\\d+([ ]?\\d)*)$";
        String[] splitted =  choice.split(" ");
        while (!(choice.matches(pattern)) || !(splitted.length >= 1 && splitted.length <= 3)) {
            System.out.println("Wrong input format or nr of marbles specified are out of bounds.");
            choice = TextIO.getlnString();

        }

        List<Integer> moveList = new ArrayList<>();

        String dir = "> " + getName() + " (" + getColorString() + ")"
                + ", please choose a direction: ";
        System.out.println(dir);
        int dirChoice = TextIO.getInt();

        while (!(dirChoice >= 0 && dirChoice <= 5)) {
            System.out.println(dir);
            dirChoice = TextIO.getInt();

        }
        moveList.add(0, dirChoice);
        return moveList;

    }
}
