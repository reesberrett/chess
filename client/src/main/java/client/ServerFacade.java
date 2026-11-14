package client;

import com.google.gson.Gson;
import model.*;

import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost" + port;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        //TODO
    }

    public AuthData login(String username, String password) throws ResponseException {
        //TODO
    }

    public void logout(String authToken) throws ResponseException {
        //TODO
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        //TODO
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        //TODO
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        //TODO
    }

    public void clear() throws ResponseException {
        //TODO
    }
}
