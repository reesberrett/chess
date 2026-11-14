package server;

import io.javalin.*;
import io.javalin.http.Context;
import java.util.Map;

import dataaccess.*;
import dataaccess.sql.*;
import com.google.gson.Gson;
import service.exceptions.DataAccessException;
import service.exceptions.ServiceException;
import service.requests.*;
import service.services.*;

public class Server {

    private final Javalin javalin;
    private final Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
        });

        ClearService clearService;
        UserService userService;
        GameService gameService;
        try {
            UserDAO userDAO = new SQLUserDAO();
            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();

            clearService = new ClearService(userDAO, authDAO, gameDAO);
            userService = new UserService(userDAO, authDAO);
            gameService = new GameService(gameDAO, authDAO);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initiate SQL DAOs: " + e.getMessage());
        }

        ServerHandler handler = new ServerHandler(clearService, userService, gameService);

        //route registration
        javalin.delete("/db", handler::handleClear);
        javalin.post("/user", handler::handleRegister);
        javalin.post("/session", handler::handleLogin);
        javalin.delete("/session", handler::handleLogout);
        javalin.get("/game", handler::handleListGames);
        javalin.post("/game", handler::handleCreateGame);
        javalin.put("/game", handler::handleJoinGame);

        javalin.exception(ServiceException.class, handler::handleServiceException);
        javalin.exception(Exception.class, handler::handleGeneralException);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}