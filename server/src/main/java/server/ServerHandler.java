package server;

import io.javalin.http.Context;
import model.AuthData;
import service.exceptions.DataAccessException;
import service.exceptions.ServiceException;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.LoginRequest;
import service.requests.RegisterRequest;
import service.services.*;
import com.google.gson.Gson;

import java.util.Map;

public class ServerHandler {

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson;

    ServerHandler(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;
        this.gson = new Gson();
    }

    public void handleClear(Context c) throws DataAccessException, ServiceException {
        clearService.clear();
        c.status(200).result("{}").contentType("application/json");
    }
    public void handleRegister(Context c) throws DataAccessException, ServiceException {
        RegisterRequest request = gson.fromJson(c.body(), RegisterRequest.class);
        AuthData result = userService.register(request);
        c.status(200).result(gson.toJson(result));
    }
    public void handleLogin(Context c) throws DataAccessException, ServiceException {
        var request = gson.fromJson(c.body(), LoginRequest.class);
        var result = userService.login(request);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    public void handleLogout(Context c) throws DataAccessException, ServiceException{
        String token = c.header("Authorization");
        if (token == null) {
            token = c.header("authorization");
        }

        userService.logout(token);
        c.status(200).result("{}").contentType("application/json");
    }

    public void handleListGames(Context c) throws DataAccessException, ServiceException {
        String token = c.header("authorization");
        if (token == null) {
            token = c.header("Authorization");
        }

        var result = gameService.listGames(token);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    public void handleCreateGame(Context c) throws DataAccessException, ServiceException{
        String token = c.header("authorization");
        var request = gson.fromJson(c.body(), CreateGameRequest.class);
        var result = gameService.createGame(token, request);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    public void handleJoinGame(Context c) throws DataAccessException, ServiceException{
        String token = c.header("authorization");
        if (token == null) {
            token = c.header("Authorization");
        }

        var request = gson.fromJson(c.body(), JoinGameRequest.class);
        gameService.joinGame(token, request);
        c.status(200).result("{}").contentType("application/json");
    }

    public void handleServiceException(ServiceException e, Context c) {
        String msg = e.getMessage();
        if (msg == null || !msg.startsWith("Error:")) {
            msg = "Error: " + (msg == null ? "unknown" : msg);
        }

        c.status(e.getStatusCode()).result(gson.toJson(Map.of("message", msg)));
    }

    public void handleGeneralException(Exception e, Context c) {
        c.status(500).result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
