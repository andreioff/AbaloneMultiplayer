package abalone.Client;

import abalone.Exceptions.ClientDisconnected;
import abalone.Game.Board;
import abalone.Players.ComputerPlayer;
import abalone.Players.HumanPlayer;
import abalone.Players.Player;
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
    private Player hintPlayer;
    private Board board;
    private boolean wantsToPlay;
    private int gamesPlayed;
    private int players;

    /**
     * Constructs a new GameClient. Initialises the view.
     */
    public GameClient() {
        view = new GameClientTUI(this);
        wantsToPlay = true;
        gamesPlayed = 0;
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
            while (wantsToPlay) {
                newGame();
                lobby();
                playGame();
            }
        } catch (ExitProgram | ProtocolException | ServerUnavailableException | ClientDisconnected e) {
            view.showMessage(e.getMessage());
            closeConnection();
        } catch (NullPointerException e) {
            view.showMessage("The connection with the server was lost!");
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
        } catch (NullPointerException ignored) {}
    }

    public void lobby() {
        String msg;
        try {
            view.showMessage("Welcome to the Abalone Game's Lobby. Waiting for players...");
            msg = in.readLine();
            while (msg.charAt(0) != 's') {
                if (msg.charAt(0) == 'j') {
                    view.showMessage(msg.split(ProtocolMessages.DELIMITER)[1] + " joined the game!");
                } else if (msg.charAt(0) == 'd') {
                    view.showMessage(msg.split(ProtocolMessages.DELIMITER)[1] + " disconnected!");
                }
                msg = in.readLine();
            }
            String[] playerNames = msg.substring(3, msg.length() - 1).split(",");
            for (int i = 0; i < playerNames.length; i++) {
                if (playerNames[i].contentEquals(name)) {
                    player.setColor(i + 1);
                    if (hintPlayer != null) hintPlayer.setColor(i + 1);
                    break;
                }
            }
            board = new Board();
            board.setup(playerNames.length);
            view.showMessage("The game has started!");
            view.showMessage("You are " + player.getColorString());
        } catch (IOException e) {
            view.showMessage("An error has occured!");
            closeConnection();
        }
    }

    @Override
    public void handleHello()
            throws ServerUnavailableException, ProtocolException {
        String name = getNameFromUser();
        int players = getNumberOfPlayers();
        initializePlayer(players);
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
            player.setName(name);
            this.players = players;
        } else {
            throw new ProtocolException("Could not handshake with the server!");
        }
    }

    public int getNumberOfPlayers() {
        int players = view.getInt("Please choose how many players do you want the game to have (2, 3 or 4): ");
        while (players < 2 || players > 4) {
            players = view.getInt("Wrong number of players. Make sure you enter a number between 2 and 4");
        }
        return players;
    }

    public String getPlayerType() {
        String playerType = view.getString("Choose the type of player:\n"
                + "-C (computer player) \n-H (human player)");
        while (!playerType.matches("(^-C$)|(^-H$)")) {
            name = view.getString("Invalid player type! Please choose again:\n"
                    + "-C (computer player) \n-H (human player)");
        }
        return playerType;
    }

    public String getNameFromUser() {
        String name = view.getString("Choose a username: ");
        while (name == null) {
            name = view.getString("Invalid username! Try again: ");
        }
        return name;
    }

    @Override
    public void sendMove(String move) throws ServerUnavailableException {
        sendMessage(move);
    }

    public String getMove() {
        return ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + player.determineMove(board);
    }

    public String getHint() {
        String[] move = hintPlayer.determineMove(board).split(ProtocolMessages.DELIMITER);
        return "Direction: " + move[0] + "; Indexes: " + move[1];
    };

    @Override
    public void newGame() throws ServerUnavailableException {
        if (gamesPlayed > 0) {
            players = getNumberOfPlayers();
            initializePlayer(players);
            player.setName(name);
            sendMessage(ProtocolMessages.GAME + ProtocolMessages.DELIMITER + name
                             + ProtocolMessages.DELIMITER + players);
        }
    }

    public void initializePlayer(int players) {
        String playerType = "-H";
        int seconds;
        if (players == 2) {
            playerType = getPlayerType();
        }
        if (playerType.contentEquals("-C")) {
            seconds = view.getInt("Choose how long do you want the AI to think: \n" +
                    "Input a number of seconds between 3 and 25: ");
            while (seconds < 3 || seconds > 25) {
                seconds = view.getInt("Invalid number of seconds.\n" +
                        "Please choose a number of seconds between 3 and 25: ");
            }
            player = new ComputerPlayer(name, players, seconds);
        } else if (players == 2) {
            seconds = view.getInt("Choose how long do you want the AI to think for a hint: \n" +
                    "Input a number of seconds between 3 and 10: ");
            while (seconds < 3 || seconds > 10) {
                seconds = view.getInt("Invalid nr of seconds.\n" +
                        "Please choose a number of seconds between 3 and 25: ");
            }
            hintPlayer = new ComputerPlayer(name, players, seconds);
            player = new HumanPlayer(name, view);
        } else {
            player = new HumanPlayer(name, view);
        }
    }

    public void playGame() throws ClientDisconnected, NullPointerException {
        try {
            String incoming;
            while ((incoming = in.readLine()).charAt(0) != 'x') {
                handleResponse(incoming);
            }
            String[] splittedIncoming = incoming.split(ProtocolMessages.DELIMITER);
            String result = splittedIncoming.length == 1 ? "The game has ended in a draw!"
                                                         : splittedIncoming[1] + " has won!";
            view.showMessage(result);
            gamesPlayed++;
            wantsToPlay = view.getBoolean("Do you want to play a new game? yes/no");
        } catch (IOException | ServerUnavailableException e) {
            view.showMessage("An error has occurred!");
            closeConnection();
        }
    }

    public void handleResponse(String incoming) throws ServerUnavailableException, ClientDisconnected {
        String current;
        switch (incoming.charAt(0)) {
            case ProtocolMessages.BOARD:
                setBoard(incoming.substring(3, incoming.length() - 1));
                view.showMessage(board.toString());
                break;

            case ProtocolMessages.NEXT:
                current = incoming.split(ProtocolMessages.DELIMITER)[1];
                if (current.contentEquals(name)) {
                    view.showMessage("It is now your turn!");
                    if (players == 2 && hintPlayer != null) {
                        view.showMessage("Please wait! Calculating the hint...");
                        view.showMessage(getHint());
                    }
                    view.start();
                } else {
                    view.showMessage("It is " + current + "'s turn!");
                }
                break;

            case ProtocolMessages.INVALID:
                view.showMessage("Invalid move! Please try again");
                view.start();
                break;

            case ProtocolMessages.DISCONNECT:
                String disconnectedName = incoming.split(ProtocolMessages.DELIMITER)[1];
                if (disconnectedName.contentEquals(name)) {
                    throw new ClientDisconnected("You have been disconnected for being afk!");
                }
                view.showMessage("Player " + disconnectedName + " has disconnected!");
                break;
        }
    }

    public void setBoard(String array) {
        board.setBoardFromArray(array);
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
