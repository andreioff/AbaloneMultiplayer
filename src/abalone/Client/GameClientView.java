package abalone.Client;

import java.net.InetAddress;

import abalone.Exceptions.ServerUnavailableException;

/**
 * Interface for the Game Client View.
 */
public interface GameClientView {

    /**
     * Asks the user to input a move and a direction, then sends a move command to the server with these parameters.
     * @throws ServerUnavailableException in case of IO exceptions.
     */
    public void start() throws ServerUnavailableException;

    /**
     * Writes the given message to standard output.
     *
     * @param message the message to write to the standard output.
     */
    public void showMessage(String message);

    /**
     * Ask the user to input a valid IP. If it is not valid, show a message and ask
     * again.
     *
     * @return a valid IP
     */
    public InetAddress getIp();

    /**
     * Prints the question and asks the user to input a String.
     *
     * @param question The question to show to the user
     * @return The user input as a String
     */
    public String getString(String question);

    /**
     * Prints the question and asks the user to input an Integer.
     *
     * @param question The question to show to the user
     * @return The written Integer.
     */
    public int getInt(String question);

    /**
     * Prints the question and asks the user for a yes/no answer.
     *
     * @param question The question to show to the user
     * @return The user input as boolean.
     */
    public boolean getBoolean(String question);
}
