package abalone.Server;

import abalone.Game.Game;
import abalone.Protocol.ProtocolMessages;
import abalone.Protocol.ServerProtocol;
import abalone.Exceptions.ExitProgram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server for Networked Game Application
 *
 * Intended Functionality: interactively set up & monitor a new server
 */
public class GameServer implements Runnable, ServerProtocol {

    /** The ServerSocket of this HotelServer */
    private ServerSocket ssock;

    /** List of names of the players, one for each connected client */
    private List<String> names;

    /** Next client number, increasing for every new connection */
    private int next_client_no;

    /** The view of this HotelServer */
    private GameServerTUI view;

    private List<GameClientHandler> queue2;

    private List<GameClientHandler> queue3;

    private List<GameClientHandler> queue4;

    /**
     * Constructs a new HotelServer. Initializes the clients list,
     * the view and the next_client_no.
     */
    public GameServer() {
        names = new ArrayList<>();
        queue2 = new ArrayList<>();
        queue3 = new ArrayList<>();
        queue4 = new ArrayList<>();
        view = new GameServerTUI();
        next_client_no = 1;
    }

    /**
     * Opens a new socket by calling {@link #setup()} and starts a new
     * HotelClientHandler for every connecting client.
     *
     * If {@link #setup()} throws a ExitProgram exception, stop the program.
     * In case of any other errors, ask the user whether the setup should be
     * ran again to open a new socket.
     */
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the hotel application
                setup();

                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client "
                            + String.format("%02d", next_client_no++);
                    view.showMessage("New client [" + name + "] connected!");
                    GameClientHandler handler =
                            new GameClientHandler(sock, this, name);
                    new Thread(handler).start();
                }

            } catch (ExitProgram e1) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: "
                        + e.getMessage());

                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    /**
     * Opens a new ServerSocket at localhost on a user-defined port.
     *
     * The user is asked to input a port, after which a socket is attempted
     * to be opened. If the attempt succeeds, the method ends, If the
     * attempt fails, the user decides to try again, after which an
     * ExitProgram exception is thrown or a new port is entered.
     *
     * @throws ExitProgram if a connection can not be created on the given
     *                     port and the user decides to exit the program.
     * @ensures a serverSocket is opened.
     */
    public void setup() throws ExitProgram {
        ssock = null;
        while (ssock == null) {
            int port = view.getInt("Please enter the server port.");

            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                ssock = new ServerSocket(port, 0,
                        InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");

                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the "
                            + "program.");
                }
            }
        }
    }

    /**
     * Removes a clientHandler from the client list.
     * @requires client != null
     */
    public synchronized void removeClient(GameClientHandler client) {
        names.remove(client.getName());

        boolean result = queue2.remove(client);
        if (!result) {
            result = queue3.remove(client);
        }
        if (!result) {
            queue4.remove(client);
        }
    }

    public synchronized void addName(String name) {
        names.add(name);
    }

    public boolean containsName(String name) {
        return names.contains(name);
    }

    public synchronized void addInQueue(GameClientHandler client, int players) {
        List<GameClientHandler> queue;
        switch(players) {
            case 2:
                queue = queue2;
                break;

            case 3:
                queue = queue3;
                break;

            default:
                queue = queue4;
                break;
        }
        notifyPlayers(client, queue);
        queue.add(client);
        if (queue.size() == players) {
            startGame(queue);
        }
    }

    public void notifyPlayers(GameClientHandler currentClient, List<GameClientHandler> queue) {
        for (GameClientHandler client : queue) {
            client.sendNotification(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + currentClient.getName());
            currentClient.sendNotification(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + client.getName());
        }
    }

    // ------------------ Server Methods --------------------------

    @Override
    public String getHello(String name) {
        return ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + name;
    }

    @Override
    public void startGame(List<GameClientHandler> queue) {
        int qSize = queue.size();
        List<GameClientHandler> players = new ArrayList<>();
        String startMsg = getStartMessage(queue);
        for (GameClientHandler client : queue) {
            players.add(client);
            client.sendNotification(startMsg);
        }
        queue.clear();
        GameHandler game = new GameHandler(players);
        (new Thread(game)).start();
    }

    @Override
    public void makeMove() {

    }

    @Override
    public void nextTurn() {

    }

    public String getStartMessage(List<GameClientHandler> queue) {
        StringBuilder msg = new StringBuilder(ProtocolMessages.START + ProtocolMessages.DELIMITER + '[');
        int size = queue.size();
        for (GameClientHandler client : queue) {
            msg.append(client.getName()).append(',');
        }
        msg.setCharAt(msg.length() - 1, ']');
        return msg.toString();
    }

    // ------------------ Main --------------------------

    /** Start a new HotelServer */
    public static void main(String[] args) {
        GameServer server = new GameServer();
        System.out.println("Welcome to the Game Server! Starting...");
        new Thread(server).start();
    }

}
