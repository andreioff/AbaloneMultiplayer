package abalone.Protocol;

import abalone.Server.GameClientHandler;

import java.util.List;

public interface ServerProtocol {
    String getHello(String name);
    void startGame(List<GameClientHandler> players);
    void makeMove();
    void nextTurn();
}
