package abalone.Server;

import abalone.Exceptions.ClientDisconnected;
import abalone.Game.Board;
import abalone.Protocol.ProtocolMessages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GameClientHandler for the Game Server application.
 * This class can handle the communication with one
 * client.
 */
public class GameClientHandler implements Runnable {

    /** The socket and In- and OutputStreams */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected GameServer */
    private GameServer srv;

    /**
     * The view of the server to be used for outputting the incoming commands
     */
    private GameServerView view;

    /**
     * The name of the client connected to the server.
     */
    private String name;

    /**
     * The move command received from the client. This string is used by the game itself to make the move of the client
     * on the server's board.
     */
    private String move;

    /**
     * Atomic flags used to signal the states of this object or variables of it. (the client is disconnected, the
     * client is in game or the move was received or not. These variables, being Atomic instead of normal type,
     * ensures the security against concurrency.
     */
    private final AtomicBoolean disconnected = new AtomicBoolean(false);
    private final AtomicBoolean receivedMove = new AtomicBoolean(false);
    private final AtomicBoolean inGame = new AtomicBoolean(false);

    /**
     * Constructs a new GameClientHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     * @param view The view of the server
     */
    public GameClientHandler(Socket sock, GameServer srv, String name, GameServerView view) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
            this.name = name;
            this.view = view;
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            while (msg != null && !isDisconnected()) {
                view.showMessage("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                msg = in.readLine();
            }
            if (isDisconnected()) {
                sendNotification(ProtocolMessages.DISCONNECT + ProtocolMessages.DELIMITER + name);
                sendNotification(ProtocolMessages.END + ProtocolMessages.DELIMITER);
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * Handles commands received from the client by calling the according
     * methods at the GameServer. For example, when the message "h;Name;Players_amount"
     * is received, the method getHello of GameServer should be called
     * and the output must be sent to the client.
     *
     * If the received input is not valid, send an incorrect "e;im"
     * message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO error occur.
     */
    private void handleCommand(String msg) throws IOException {
        String command;
        String param, param2;
        String[] splitted;
        splitted = msg.split(ProtocolMessages.DELIMITER);
        command = splitted[0];
        param = null;
        param2 = null;

        if (splitted.length > 1) {
            param = splitted[1];
        }
        if (splitted.length > 2) {
            param2 = splitted[2];
        }

        switch (command.charAt(0)) {
            case ProtocolMessages.HELLO:
                handleHello(param, param2);
                break;

            case ProtocolMessages.MOVE:
                //if a move is received, pass it to the move variable and flag that the variable contains a new move
                move = param + ProtocolMessages.DELIMITER + param2;
                receivedMove.set(true);
                break;

            case ProtocolMessages.GAME:
                //only clients that are not in a game are allowed to request to join in a new game.
                if (!inGame.get()) {
                    srv.addInQueue(this, Integer.parseInt(param2));
                }
                break;

            default:
                sendNotification(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "im");
                break;
        }
    }

    /**
     * Handles the handshake with the client. Sends proper messages (as described in the protocol) if the parameters
     * are invalid. If the parameters are valid, send the hello message and add the client in a queue based on the
     * players amount.
     * @param p1 = the name of the client connected
     * @param p2 = the amount of players that the client specified
     */
    private void handleHello(String p1, String p2) {
        if (p1 == null || srv.containsName(p1)) {
            sendNotification(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "invalidname");
        } else {
            int players;
            try {
                players = Integer.parseInt(p2);
            } catch (NumberFormatException e) {
                players = 0;
            }
            if (players > 4 || players < 2) {
                sendNotification(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "invalidamount");
            } else {
                sendNotification(srv.getHello(p1));
                name = p1;
                srv.addName(name);
                srv.addInQueue(this, players);
            }
        }
    }

    /**
     * While the flag that the move was received by the client is not set, count how many seconds passed and disconnect
     * the client if 30 seconds have passed. If the flag is set meanwhile, process the move and try to make it. If the
     * move is invalid send a message back to the client and wait for a new move. Else, make the move and unset
     * the flag.
     * @param board = the board of the game
     * @param color = the color that makes a move
     * @throws ClientDisconnected if no message is received from the client within 30 seconds
     */
    public void makeMove(Board board, int color) throws ClientDisconnected {
        int seconds;
        long startTime = System.currentTimeMillis();
        long stopTime;
        while (!moveWasReceived()) {
            stopTime = System.currentTimeMillis();
            seconds = (int) ((stopTime - startTime) / 1000);
            if (seconds > 30) {
                disconnected.set(true);
                throw new ClientDisconnected(name);
            }
        }
        String[] split = move.split(";");
        int direction = Integer.parseInt(split[0]);
        String indexes = split[1].substring(1, split[1].length() - 1);
        List<Integer> marbles = new ArrayList<>();
        for (String index : indexes.split(",")) {
            marbles.add(Integer.parseInt(index));
        }
        if (!board.move(direction, marbles, color)) {
            receivedMove.set(false);
            sendNotification(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "iv");
            makeMove(board, color);
        }
        receivedMove.set(false);
        move = null;
    }

    /**
     * Shut down the connection to this client by closing the socket and
     * the In- and OutputStreams and removing this object from any queue or list.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down...");
        srv.removeClient(this);
        try {
            in.close();
            out.close();
            sock.close();
            in = null;
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the given message to the client through the socket outstream.
     * @param msg = the message to be sent
     */
    public void sendNotification(String msg) {
        try {
            if (out != null) {
                view.showMessage("> [" + name + "] Sending: " + msg);
                out.write(msg);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the state of the disconnected flag
     * @returns the boolean value of the disconnected flag. (true if the flag is set and false otherwise).
     */
    private boolean isDisconnected() {
        return disconnected.get();
    }

    /**
     * Returns the state of the receivedMove flag
     * @returns the boolean value of the receivedMove flag. (true if the flag is set and false otherwise).
     */
    private boolean moveWasReceived() {
        return receivedMove.get();
    }

    /**
     * Sets the state of the inGame flag (true if the client is in game and false otherwise)
     * @param value = the value that the flag should be set to.
     */
    public void setInGameStatus(boolean value) {
        inGame.set(value);
    }

    /**
     * Returns the name of this client handler
     * @returns the name of this object
     */
    public String getName() {
        return name;
    }
}
