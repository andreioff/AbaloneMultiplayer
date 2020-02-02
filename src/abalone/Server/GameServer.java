package abalone.Server;

import abalone.Protocol.ProtocolMessages;
import abalone.Protocol.ServerProtocol;
import abalone.Exceptions.ExitProgram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Server for Networked Game Application
 *
 * Intended Functionality: interactively set up & monitor a new server
 */
public class GameServer implements Runnable, ServerProtocol {

    /** The ServerSocket of this GameServer */
    private ServerSocket ssock;

    /** List of names of the players, one for each connected client */
    private List<String> names;

    /** Next client number, increasing for every new connection */
    private int next_client_no;

    /** The view of this GameServer */
    private GameServerTUI view;

    /**
     * Queues for different type of games
     */
    private List<GameClientHandler> queue2;
    private List<GameClientHandler> queue3;
    private List<GameClientHandler> queue4;

    /**
     * A lock to ensure security against concurrency when methods that modify the queues are called simultaneously
     */
    private ReentrantLock lock;
    /**
     * Constructs a new GameServer. Initializes the names list, the queues,
     * the view, the next_client_no and the lock.
     */
    public GameServer() {
        names = new ArrayList<>();
        queue2 = new ArrayList<>();
        queue3 = new ArrayList<>();
        queue4 = new ArrayList<>();
        view = new GameServerTUI();
        next_client_no = 1;
        lock = new ReentrantLock();
    }

    /**
     * Opens a new socket by calling {@link #setup()} and starts a new
     * GameClientHandler for every connecting client.
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
                            new GameClientHandler(sock, this, name, view);
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
     * Removes a clientHandler from the queue where is waiting and his name from the names list.
     * @requires client != null
     */
    public synchronized void removeClient(GameClientHandler client) {
        boolean done = false;
        boolean isLocked;
        while (!done) {
            isLocked = lock.tryLock();
            if (!isLocked) {
                try {
                    lock.lock();
                    names.remove(client.getName());

                    boolean result = queue2.remove(client);
                    List<GameClientHandler> queue = queue2;
                    if (!result) {
                        result = queue3.remove(client);
                        queue = queue3;
                    }
                    if (!result) {
                        queue4.remove(client);
                        queue = queue4;
                    }
                    notifyPlayersDisconnection(queue, client.getName());
                    done = true;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     *  Add a name in the names list
     * @param name = the name that should be added
     * @requires name != null
     * @ensures names.contains(name)
     */
    public synchronized void addName(String name) {
        names.add(name);
    }

    /**
     * Checks if the given name is contained in the list of names
     * @param name = the name that should be searched for
     * @returns true if the given name is contained in the names list and false otherwise
     * @requires name != null
     */
    public synchronized boolean containsName(String name) {
        return names.contains(name);
    }

    /**
     * Adds the given client handler to the queue based on the number of players specified by the client and
     * starts a new game if there are enough players in the queue.
     * @param client = the client handler to be added in the queue
     * @param players = the number of players that the game should have based on the client's preference
     * @requires client != null
     * @requires players >= 1 && players <= 4
     */
    public synchronized void addInQueue(GameClientHandler client, int players) {
        boolean done = false;
        boolean isLocked;
        while (!done) {
            isLocked = lock.tryLock();
            if (!isLocked) {
                try {
                    lock.lock();
                    List<GameClientHandler> queue;
                    switch (players) {
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
                    notifyPlayersJoin(client, queue);
                    queue.add(client);
                    if (queue.size() == players) {
                        startGame(queue);
                    }
                    done = true;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * Sends to the players that are already waiting in the queue that a new player has joined and send the names
     * of all the players that are waiting in the queue to the client that just connected
     * @param currentClient = the client handler of the client that just connected
     * @param queue = the queue where the client handler should be added
     * @requires currentClient != null
     * @requires queue != null
     */
    public void notifyPlayersJoin(GameClientHandler currentClient, List<GameClientHandler> queue) {
        for (GameClientHandler client : queue) {
            client.sendNotification(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + currentClient.getName());
            currentClient.sendNotification(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + client.getName());
        }
    }

    /**
     * Notify all the players in a queue that a player has disconnected
     * @param queue = the queue with the players that should be notified
     * @param name = the name of the player that disconnected
     * @requires queue != null
     * @requires name != null
     */
    public void notifyPlayersDisconnection(List<GameClientHandler> queue, String name) {
        for (GameClientHandler client : queue) {
            client.sendNotification(ProtocolMessages.DISCONNECT + ProtocolMessages.DELIMITER + name);
        }
    }

    // ------------------ Server Methods --------------------------

    /**
     * Returns the hello command that should be returned to the client that made the handshake with the server
     * @param name = the name of the client
     * @returns a String object containing the hello message that should be sent to the client
     */
    public String getHello(String name) {
        return ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + name;
    }

    /**
     * Start a new game with the players from the given queue on a new thread.
     * @param queue = the queue of client handlers that the game should be created with.
     */
    @Override
    public void startGame(List<GameClientHandler> queue) {
        List<GameClientHandler> players = new ArrayList<>();
        Collections.shuffle(queue);
        String startMsg = getStartMessage(queue);
        for (GameClientHandler client : queue) {
            players.add(client);
            client.sendNotification(startMsg);
        }
        queue.clear();
        GameHandler game = new GameHandler(players);
        (new Thread(game)).start();
    }

    /**
     * Creates and returns the start game message that should be sent to all clients.
     * @param queue = the queue with the client handlers that will be put in a game
     * @returns the start game command with all the clients names
     */
    public String getStartMessage(List<GameClientHandler> queue) {
        StringBuilder msg = new StringBuilder(ProtocolMessages.START + ProtocolMessages.DELIMITER + '[');
        for (GameClientHandler client : queue) {
            msg.append(client.getName()).append(',');
        }
        msg.setCharAt(msg.length() - 1, ']');
        return msg.toString();
    }

    // ------------------ Main --------------------------

    /** Start a new GameServer */
    public static void main(String[] args) {
        GameServer server = new GameServer();
        System.out.println("Welcome to the Abalone Game's Server! Starting...");
        new Thread(server).start();
    }

}
