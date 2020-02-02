package abalone.Client;

import abalone.Exceptions.ServerUnavailableException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClientTUI implements GameClientView {

    //the client that uses this textual user interface
    GameClient client;

    //a scanner used to receive input from the user
    Scanner in;

    /**
     * Creates a new game client tui and initialize the scanner to be able to read from the console.
     * @param client = the client for which this tui is created
     */
    public GameClientTUI(GameClient client) {
        this.client = client;
        in = new Scanner(System.in);
    }

    /**
     * Gets the move command from the client and sends it to the server.
     * @throws ServerUnavailableException if IO error occurs
     */
    @Override
    public void start() throws ServerUnavailableException {
        String input;
        input = client.getMove();
        client.sendMove(input);
    }

    /**
     * Displays the given message to the console
     * @param message the message to write to the standard output.
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Asks the user to input an ip address until the address is correct.
     * @return an InetAddress object containing the ip address received from the user
     */
    @Override
    public InetAddress getIp() {
        InetAddress address = null;
        try {
            System.out.print("Enter a valid IP address: ");
            address = InetAddress.getByName(in.nextLine());
        } catch (UnknownHostException e) {
            System.out.println("Invalid address!");
            return getIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * Asks the user to input a string.
     * @param question The question to show to the user
     * @returns a String object containing the user's input or null if the user entered an empty string
     */
    @Override
    public String getString(String question) {
        showMessage(question);
        String input = null;
        try {
            input = in.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * Asks the user to input a positive integer until the input is a valid positive integer.
     * @param question The question to show to the user
     * @returns a positive integer containing the value specified by the user
     * @ensures return != null
     * @ensures return >= 0
     */
    @Override
    public int getInt(String question) {
        showMessage(question);
        int input;
        try {
            input = Integer.parseInt(in.nextLine());
            if (input < 0) throw new NumberFormatException();
            return input;
        } catch (NumberFormatException e) {
            showMessage("You must enter an integer greater than 0");
            return getInt(question);
        }
    }

    /**
     * Asks the user an yes or no question and returns true if the user enterd "yes" and false otherwise
     * @param question The question to show to the user
     * @returns a boolean containing the value true if the user entered the message "yes" and false otherwise
     */
    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String input;
        boolean output = false;
        try {
            input = in.nextLine();
            if (input.contentEquals("yes")) {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
