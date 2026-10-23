package server;

import io.javalin.*;
import dataaccess.*;
import service.*;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);



    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void handleClear() throws Exception{
        //TODO
    }
}