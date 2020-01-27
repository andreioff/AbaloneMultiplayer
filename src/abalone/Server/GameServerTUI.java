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

    @Override
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public String getString(String question) {
        showMessage(question);
        String input = null;
        try {
            input = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    public int getInt(String question) {
        showMessage(question);
        int input = 0;
        try {
            input = Integer.parseInt(in.readLine());
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String input = null;
        boolean output = false;
        try {
            input = in.readLine();
            if (input.contentEquals("yes")) {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}

