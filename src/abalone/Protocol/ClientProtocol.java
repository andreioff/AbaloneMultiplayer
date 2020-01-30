package abalone.Protocol;

import abalone.Exceptions.ClientDisconnected;
import abalone.Exceptions.ProtocolException;
import abalone.Exceptions.ServerUnavailableException;

public interface ClientProtocol {
    void start();
    void handleHello() throws ServerUnavailableException, ProtocolException;
    void sendMove(String move) throws ServerUnavailableException;
    void newGame() throws ServerUnavailableException;
    void handleResponse(String incoming) throws ServerUnavailableException, ClientDisconnected;
}
