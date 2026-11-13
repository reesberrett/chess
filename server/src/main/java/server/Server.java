package server;

import io.javalin.*;
import io.javalin.http.Context;
import java.util.Map;

import dataaccess.*;
import com.google.gson.Gson;
import service.exceptions.DataAccessException;
import service.exceptions.ServiceException;
import service.requests.*;
import service.services.*;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson = new Gson();
    private final ServerHandler handler;

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
        });

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        clearService = new ClearService(userDAO, authDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        handler = new ServerHandler(clearService, userService, gameService);

        //route registration
        javalin.delete("/db", handler::handleClear);
        javalin.post("/user", handler::handleRegister);
        javalin.post("/session", this::handleLogin);
        javalin.delete("/session", this::handleLogout);
        javalin.get("/game", this::handleListGames);
        javalin.post("/game", this::handleCreateGame);
        javalin.put("/game", this::handleJoinGame);

        javalin.exception(ServiceException.class, this::handleServiceException);
        javalin.exception(Exception.class, this::handleGeneralException);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    //helper functions for handling operations
    private void handleClear(Context c) throws DataAccessException {
        clearService.clear();
        c.status(200).result("{}").contentType("application/json");
    }

    private void handleRegister(Context c) throws DataAccessException, ServiceException {
        var request = gson.fromJson(c.body(), RegisterRequest.class);
        var result = userService.register(request);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    private void handleLogin(Context c) throws DataAccessException, ServiceException {
        var request = gson.fromJson(c.body(), LoginRequest.class);
        var result = userService.login(request);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    private void handleLogout(Context c) throws DataAccessException, ServiceException{
        String token = c.header("Authorization");
        if (token == null)
            token = c.header("authorization");

        userService.logout(token);
        c.status(200).result("{}").contentType("application/json");
    }

    private void handleListGames(Context c) throws DataAccessException, ServiceException {
        String token = c.header("authorization");
        if (token == null)
            token = c.header("Authorization");

        var result = gameService.listGames(token);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    private void handleCreateGame(Context c) throws DataAccessException, ServiceException{
        String token = c.header("authorization");
        var request = gson.fromJson(c.body(), CreateGameRequest.class);
        var result = gameService.createGame(token, request);
        c.status(200).result(gson.toJson(result)).contentType("application/json");
    }

    private void handleJoinGame(Context c) throws DataAccessException, ServiceException{
        String token = c.header("authorization");
        if (token == null)
            token = c.header("Authorization");

        var request = gson.fromJson(c.body(), JoinGameRequest.class);
        gameService.joinGame(token, request);
        c.status(200).result("{}").contentType("application/json");
    }

    private void handleServiceException(ServiceException e, Context c) {
        String msg = e.getMessage();
        if (msg == null || !msg.startsWith("Error:"))
            msg = "Error: " + (msg == null ? "unknown" : msg);

        c.status(e.getStatusCode()).result(gson.toJson(Map.of("message", msg)));
    }

    private void handleGeneralException(Exception e, Context c) {
        c.status(500).result(gson.toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}