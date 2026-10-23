package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;

    @BeforeEach
    public void setup() throws Exception {
        var uDao = new MemoryUserDAO();
        var aDao = new MemoryAuthDAO();
        var gDao = new MemoryGameDAO();
        clearService = new ClearService(uDao, aDao, gDao);
        userService = new UserService(uDao, aDao);
        gameService = new GameService(gDao, aDao);
        clearService.clear();
    }

    @Test
    public void registerSuccess() throws Exception {
        var result = userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        assertNotNull(result.token());
        assertEquals("user1", result.username());
    }

    @Test
    public void registerDuplicateFail() throws Exception {
        userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        assertThrows(AlreadyTakenException.class, () ->
                userService.register(new RegisterRequest("user1", "newpass", "other@test.com"))
        );
    }

    @Test
    public void loginSuccess() throws Exception {
        userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        var result = userService.login(new LoginRequest("user1", "pass"));
        assertNotNull(result.token());
    }

    @Test
    public void loginBadPassword() throws Exception {
        userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        assertThrows(UnauthorizedException.class, () ->
                userService.login(new LoginRequest("user1", "wrongpass"))
        );
    }

    @Test
    public void logoutSuccess() throws Exception {
        var auth = userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        assertDoesNotThrow(() -> userService.logout(auth.token()));
    }

    @Test
    public void logoutBadToken() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("invalid-token"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        var auth = userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        var result = gameService.createGame(auth.token(), new CreateGameRequest("Match1"));
        assertTrue(result.containsKey("gameID"));
    }

    @Test
    public void createGameUnauthorized() {
        assertThrows(UnauthorizedException.class, () ->
                gameService.createGame("badToken", new CreateGameRequest("Match1"))
        );
    }

    @Test
    public void joinGameSuccess() throws Exception {
        var auth = userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        var game = gameService.createGame(auth.token(), new CreateGameRequest("Match1"));
        assertDoesNotThrow(() ->
                gameService.joinGame(auth.token(), new JoinGameRequest(ChessGame.TeamColor.WHITE, game.get("gameID")))
        );
    }

    @Test
    public void joinGameAlreadyTaken() throws Exception {
        var auth1 = userService.register(new RegisterRequest("user1", "pass", "email1@test.com"));
        var auth2 = userService.register(new RegisterRequest("user2", "pass", "email2@test.com"));
        var game = gameService.createGame(auth1.token(), new CreateGameRequest("Match1"));
        gameService.joinGame(auth1.token(), new JoinGameRequest(ChessGame.TeamColor.WHITE, game.get("gameID")));

        assertThrows(AlreadyTakenException.class, () ->
                gameService.joinGame(auth2.token(), new JoinGameRequest(ChessGame.TeamColor.WHITE, game.get("gameID")))
        );
    }

    @Test
    public void clearTest() throws Exception {
        var auth = userService.register(new RegisterRequest("user1", "pass", "email@test.com"));
        gameService.createGame(auth.token(), new CreateGameRequest("Match1"));
        clearService.clear();

        assertThrows(UnauthorizedException.class, () -> gameService.listGames(auth.token()));
    }
}
