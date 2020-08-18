package abalone.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


/**
 * Game Server TUI for user input and user messages
 */
public class GameServerTUI implements GameServerView {

    /** The PrintWriter to write messages to */
    private PrintWriter console;
    private BufferedReader in;

    /**
     * Constructs a new GameServerTUI. Initializes the console.
     */
    public GameServerTUI() {
        console = new PrintWriter(System.out, true);
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Show a the given message to the console
     * @param message the message to write to the standard output.
     */
    @Override
    public void showMessage(String message) {
        console.println(message);
    }

    /**
     * Asks the user to input a String.
     * @param question The question to show to the user
     * @returns a string with the input of the user
     */
    @Override
    public String getString(String question) {
        showMessage(question);
        String input = null;
        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * Asks the user to input a valid integer.
     * @param question The question to show to the user
     * @returns the integer entered by the user
     */
    @Override
    public int getInt(String question) {
        showMessage(question);
        int input = 0;
        try {
            input = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Make sure you enter only digits!");
            return getInt(question);
        }
        return input;
    }

    /**
     * Asks the user a yes or no question. If the user enters "yes" the method return true otherwise false.
     * @param question The question to show to the user
     * @returns true if the user entered "yes" and false otherwise
     */
    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String input;
        boolean output = false;
        try {
            input = in.readLine();
            if (input.contentEquals("yes")) {
                output = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}

