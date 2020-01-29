package abalone.Client;

import abalone.Exceptions.ExitProgram;
import abalone.Exceptions.ServerUnavailableException;

import java.net.InetAddress;

public class BackupView implements GameClientView {
    @Override
    public void start() throws ServerUnavailableException {

    }

    @Override
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public InetAddress getIp() {
        return null;
    }

    @Override
    public String getString(String question) {
        return null;
    }

    @Override
    public int getInt(String question) {
        return 0;
    }

    @Override
    public boolean getBoolean(String question) {
        return false;
    }
}
