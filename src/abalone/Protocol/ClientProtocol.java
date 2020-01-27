package abalone.Protocol;

import abalone.Exceptions.ProtocolException;
import abalone.Exceptions.ServerUnavailableException;

public interface ClientProtocol {
    public void handleHello() throws ServerUnavailableException, ProtocolException;
    public void sendMove(String move) throws ServerUnavailableException;
    public void newGame();
    public void sendExit();
}
