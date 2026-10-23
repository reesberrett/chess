package server;

import io.javalin.*;
import io.javalin.http.Context;
import dataaccess.*;
import service.*;
import java.util.Map;

import com.google.gson.Gson;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);

        //route registration
        javalin.delete("/db", this::handleClear);
        javalin.post("/user", this::handleRegister);
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

    private void handleClear(Context c) throws Exception{
        clearService.clear();
        c.status(200).json(Map.of());
    }

    private void handleRegister(Context c) throws Exception{
        var request = gson.fromJson(c.body(), RegisterRequest.class);
        var result = userService.register(request);
        c.status(200).json(result);
    }

    private void handleLogin(Context c) throws Exception{
        var request = gson.fromJson(c.body(), LoginRequest.class);
        var result = userService.login(request);
        c.status(200).json(result);
    }

    private void handleLogout(Context c) throws Exception{
        String token = c.header("authorization");
        userService.logout(token);
        c.status(200).json(Map.of());
    }

    private void handleListGames(Context c) throws Exception{
        String token = c.header("authorization");
        var result = gameService.listGames(token);
        c.status(200).json(result);
    }

    private void handleCreateGame(Context c) throws Exception{
        String token = c.header("authorization");
        var request = gson.fromJson(c.body(), CreateGameRequest.class);
        var result = gameService.createGame(token, request);
        c.status(200).json(result);
    }

    private void handleJoinGame(Context c) throws Exception{
        String token = c.header("authorization");
        var request = gson.fromJson(c.body(), JoinGameRequest.class);
        gameService.joinGame(token, request);
        c.status(200).json(Map.of());
    }

    private void handleServiceException(ServiceException e, Context c) {
        c.status(e.getStatusCode()).json(Map.of("message", e.getMessage()));
    }

    private void handleGeneralException(Exception e, Context c) {
        c.status(500).json(Map.of("message", "Error: " + e.getMessage()));
    }
}