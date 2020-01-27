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

    /** The connected HotelServer */
    private GameServer srv;

    private String name;

    private String move = "not set";
    private String disconnected = "not set";

    /**
     * Constructs a new GameClientHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public GameClientHandler(Socket sock, GameServer srv, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.srv = srv;
            this.name = name;
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
            while (msg != null && !flagIsSet(disconnected)) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                out.flush();
                msg = in.readLine();
            }
            if (flagIsSet(disconnected)) sendNotification(ProtocolMessages.EXIT + ProtocolMessages.DELIMITER);
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
     * @throws IOException if an IO errors occur.
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

        if (command.length() == 1) {
            switch (command.charAt(0)) {
                case ProtocolMessages.HELLO:
                    handleHello(param, param2);
                    break;

                case ProtocolMessages.MOVE:
                    synchronized (move) { move = param + ProtocolMessages.DELIMITER + param2; };
                    break;

                case ProtocolMessages.EXIT:
                    out.write("");
                    break;

                default:
                    out.write(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "im");
                    break;
            }
        } else {
            out.write("Command must have only 1 character.");
        }
    }

    private void handleHello(String p1, String p2) throws IOException {
        if (p1 == null || srv.containsName(p1)) {
            out.write(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "invalidname");
            out.newLine();
        } else {
            int players;
            try {
                players = Integer.parseInt(p2);
            } catch (NumberFormatException e) {
                players = 0;
            }
            if (players > 4 || players < 2) {
                out.write(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "invalidamount");
                out.newLine();
            } else {
                out.write(srv.getHello(p1));
                out.newLine();
                name = p1;
                srv.addName(name);
                srv.addInQueue(this, players);
            }
        }
    }

    public void makeMove(Board board, int color) throws ClientDisconnected {
        int seconds;
        long startTime = System.currentTimeMillis();
        long stopTime;
        while (!flagIsSet(move)) {
            stopTime = System.currentTimeMillis();
            seconds = (int)((stopTime - startTime) / 1000);
            if (seconds > 30) {
                synchronized (disconnected) { disconnected = "set"; };
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

        synchronized (move) { move = "not set"; };

        if (!board.move(direction, marbles, color)) {
            sendNotification(ProtocolMessages.INVALID + ProtocolMessages.DELIMITER + "iv");
            makeMove(board, color);
        }
    }

    /**
     * Shut down the connection to this client by closing the socket and
     * the In- and OutputStreams.
     */
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down...");
        srv.removeClient(this);
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean flagIsSet(String flag) {
        synchronized (flag) {
            return !flag.contentEquals("not set");
        }
    }

    public String getName() {
        return name;
    }
}
