package abalone.Client;

import abalone.Protocol.ProtocolMessages;
import abalone.Exceptions.ExitProgram;
import abalone.Exceptions.ServerUnavailableException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClientTUI implements GameClientView {

    GameClient client;
    Scanner in;

    public GameClientTUI(GameClient client) {
        this.client = client;
        in = new Scanner(System.in);
    }


    @Override
    public void start() throws ServerUnavailableException {
        try {
            String input;
            input = client.getMove();
            handleUserInput(input);
        } catch(ExitProgram e) {
            client.sendExit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        switch (input.charAt(0)) {
            case ProtocolMessages.MOVE:
                client.sendMove(input);
                break;
            case ProtocolMessages.EXIT:
                throw new ExitProgram("User exited!");
            default:
                showMessage("Unknown command");
                break;
            }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

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

    @Override
    public int getInt(String question) {
        showMessage(question);
        int input = 0;
        try {
            input = Integer.parseInt(in.nextLine());
            if (input < 0) throw new NumberFormatException();
            return input;
        } catch (NumberFormatException e) {
            showMessage("You must enter an integer greater than 0");
            return getInt(question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean getBoolean(String question) {
        showMessage(question);
        String input = null;
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
