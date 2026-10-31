package server;

import io.javalin.http.Context;
import model.AuthData;
import service.exceptions.DataAccessException;
import service.exceptions.ServiceException;
import service.requests.RegisterRequest;
import service.services.*;
import com.google.gson.Gson;

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
        //TODO
    }
    public void handleLogout(Context c) throws DataAccessException, ServiceException {
        //TODO
    }
    public void handleCreateGame(Context c) throws DataAccessException, ServiceException {
        //TODO
    }
    public void handleJoinGame(Context c) throws DataAccessException, ServiceException {
        //TODO
    }
    public void handleListGames(Context c) throws DataAccessException, ServiceException {
        //TODO
    }

    public void handleServiceException(Context c) throws DataAccessException, ServiceException {
        //TODO
    }
    public void handleException(Context c) throws DataAccessException, ServiceException {
        //TODO
    }
}
