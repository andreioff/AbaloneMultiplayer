package abalone.Protocol;

import abalone.Server.GameClientHandler;

import java.util.List;

public interface ServerProtocol {
    void startGame(List<GameClientHandler> players);
    void removeClient(GameClientHandler client);
    void addName(String name);
    void addInQueue(GameClientHandler client, int players);
}
