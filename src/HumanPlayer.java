import java.util.List;
import utils.TextIO;
public class HumanPlayer extends Player {

    /**
     * Creates a new player object
     *
     * @param name
     * @param color
     * @requires name is not null
     * @requires the nr of the color
     * @ensures the name of the player
     * @ensures the player's colour
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
        while(!(choice.matches(pattern)) || !(splitted.length >= 1 && splitted.length <= 3)) {
            System.out.println("Wrong input format or nr of marbles specified are out of bounds.");
            choice = TextIO.getlnString();
        }




    }
}
