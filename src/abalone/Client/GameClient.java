package abalone.Client;

import abalone.Game.Board;
import abalone.Protocol.ClientProtocol;
import abalone.Protocol.ProtocolMessages;
import abalone.Exceptions.ExitProgram;
import abalone.Exceptions.ProtocolException;
import abalone.Exceptions.ServerUnavailableException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient implements ClientProtocol {
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
    private GameClientTUI view;
    private String name;
    private Player player;
    private Board board;

    /**
     * Constructs a new HotelClient. Initialises the view.
     */
    public GameClient() {
        view = new GameClientTUI(this);
    }

    /**
     * Starts a new GameClient by creating a connection, followed by the
     * HELLO handshake as defined in the protocol. After a successful
     * connection and handshake, the view is started. The view asks for
     * user input and handles all further calls to methods of this class.
     *
     * When errors occur, or when the user terminates a server connection, the
     * user is asked whether a new connection should be made.
     */
    public void start() {
        try {
            createConnection();
            handleHello();
            lobby();
            playGame();
        } catch(ExitProgram | ProtocolException | ServerUnavailableException e) {
            view.showMessage(e.getMessage());
            closeConnection();
        }
    }

    /**
     * Creates a connection to the server. Requests the IP and port to
     * connect to at the view (TUI).
     *
     * The method continues to ask for an IP and port and attempts to connect
     * until a connection is established or until the user indicates to exit
     * the program.
     *
     * @throws ExitProgram if a connection is not established and the user
     * 				       indicates to want to exit the program.
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            InetAddress addr = view.getIp();
            int port = view.getInt("Enter the port: ");
            // try to open a Socket to the server
            try {
                view.showMessage("Attempting to connect to " + addr + ":"
                        + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(
                        serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(
                        serverSock.getOutputStream()));
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + addr.toString() + " and port " + port + ".");

                boolean response = view.getBoolean("Do you want to try again?");
                if(!response) {
                    throw new ExitProgram("User wanted to exit.");
                }
            }
        }
    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null.
     *
     * Always make sure to close current connections via shutdown()
     * before calling this method!
     */
    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }

    /**
     * Sends a message to the connected server, followed by a new line.
     * The stream is then flushed.
     *
     * @param msg the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public synchronized void sendMessage(String msg)
            throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                view.showMessage(e.getMessage());
                throw new ServerUnavailableException("Could not write "
                        + "to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not write "
                    + "to server.");
        }
    }

    /**
     * Reads and returns one line from the server.
     *
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     */
    public String readLineFromServer()
            throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                }
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read "
                    + "from server.");
        }
    }

    /**
     * Closes the connection by closing the In- and OutputStreams, as
     * well as the serverSocket.
     */
    public void closeConnection() {
        view.showMessage("Closing the connection...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lobby() {
        String msg;
        try {
            msg = in.readLine();
            while (msg.charAt(0) != 's') {
                view.showMessage(msg.split(ProtocolMessages.DELIMITER)[1] + " joined the game!");
                msg = in.readLine();
            }
            view.showMessage("The game has started!");
        } catch (IOException e) {
            view.showMessage("An error has occured!");
            closeConnection();
        }
    }

    @Override
    public void handleHello()
            throws ServerUnavailableException, ProtocolException {

        String name = view.getString("Choose a username: ");
        while (name == null) {
            name = view.getString("Invalid username! Try again: ");
        }

        int players = view.getInt("Please choose how many players do you want the game to have (2, 3 or 4): ");
        while (players < 2 || players > 4) {
            players = view.getInt("Wrong number of players. Make sure you enter a number between 2 and 4");
        }

        String playerType = view.getString("Choose the type of player:\n"
                                            + "-C (computer player) \n-H (human player)");
        while (!playerType.matches("(^-C$)|(^-H$)")) {
            name = view.getString("Invalid player type! Please choose again:\n"
                                    + "-C (computer player) \n-H (human player)");
        }

        String nameAlreadyExist = ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "invalidname";

        sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER
                    + name + ProtocolMessages.DELIMITER + players);
        String receivedMessage = readLineFromServer();

        while (receivedMessage.contentEquals(nameAlreadyExist)) {
            name = view.getString("The name already exists! Please choose another one: ");
            sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER
                    + name + ProtocolMessages.DELIMITER + players);
            receivedMessage = readLineFromServer();
        }
        
        String expectedMessage = ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + name;
        if (receivedMessage.contentEquals(expectedMessage)) {
            this.name = name;
            player = new HumanPlayer(name, view);
            view.showMessage("Welcome to the Abalone Game's Lobby. Waiting for players...");
        } else {
            throw new ProtocolException("Could not handshake with the server!");
        }
    }

    @Override
    public void sendMove(String move) throws ServerUnavailableException {
        sendMessage(move);
    }

    public String getMove() {
        return ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + player.determineMove(board, view);
    }

    @Override
    public void newGame() {

    }

    public void playGame() {
        String current;
        board = new Board();
        try {
            String incoming;
            while ((incoming = in.readLine()).charAt(0) != 'x') {
                if (incoming.charAt(0) == 'b') {
                    setBoard(incoming.substring(3, incoming.length() - 1));
                    view.showMessage(board.toString());
                } else if (incoming.charAt(0) == 'n') {
                    current = incoming.split(ProtocolMessages.DELIMITER)[1];
                    if (current.contentEquals(name)) {
                        view.showMessage("It is now your turn!");
                        view.start();
                    } else {
                        view.showMessage("It is " + current + "'s turn!");
                    }
                } else if (incoming.charAt(0) == ProtocolMessages.INVALID) {
                    view.showMessage("Invalid move! Please try again");
                    view.start();
                } else if (incoming.charAt(0) == ProtocolMessages.DISCONNECT) {
                    view.showMessage("Player " + incoming.split(ProtocolMessages.DELIMITER)[1] + " has disconnected!");
                }
            }
            String[] splittedIncoming = incoming.split(ProtocolMessages.DELIMITER);
            String result = splittedIncoming.length == 1 ? "The game has ended in a draw!"
                                                         : splittedIncoming[1] + " has won!";
            view.showMessage(result);
        } catch (IOException | ServerUnavailableException e) {
            view.showMessage("An error has occurred!");
            closeConnection();
        }
    }

    public void setBoard(String array) {
        board.setBoardFromArray(array);
    }

    @Override
    public void sendExit() {

    }

    /**
     * This method starts a new GameClient.
     *
     * @param args
     */
    public static void main(String[] args) {
        (new GameClient()).start();
    }
}
