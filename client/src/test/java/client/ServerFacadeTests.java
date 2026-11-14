package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    void clearDatabase() throws Exception {
        facade.clear();
    }

    @Test
    void registerPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegativeDuplicate() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.register("player1", "password", "p1@email.com"));
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertEquals("player1", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    void loginNegativeBadPassword() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.login("player1", "wrong"));
    }

    @Test
    void logoutPositive() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    void logoutNegativeInvalidToken() {
        assertThrows(ResponseException.class, () -> facade.logout("bad-token"));
    }

    @Test
    void createGamePositive() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        int gameId = facade.createGame(auth.authToken(), "MyGame");
        assertTrue(gameId > 0);
    }

    @Test
    void createGameNegativeUnauthorized() {
        assertThrows(ResponseException.class, () -> facade.createGame("bad-token", "MyGame"));
    }

    @Test
    void listGamesPositive() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        facade.createGame(auth.authToken(), "Game1");
        facade.createGame(auth.authToken(), "Game2");
        var games = facade.listGames(auth.authToken());
        assertEquals(2, games.size());
    }

    @Test
    void listGamesNegativeUnauthorized() {
        assertThrows(ResponseException.class, () -> facade.listGames("bad-token"));
    }

    @Test
    void joinGamePositive() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        int gameId = facade.createGame(auth.authToken(), "Game1");
        assertDoesNotThrow(() -> facade.joinGame(auth.authToken(), "WHITE", gameId));
        var games = facade.listGames(auth.authToken());
        assertEquals("player1", games.getFirst().whiteUsername());
    }

    @Test
    void joinGameNegativeColorTaken() throws Exception {
        var auth1 = facade.register("player1", "password", "p1@email.com");
        var auth2 = facade.register("player2", "password", "p2@email.com");
        int gameId = facade.createGame(auth1.authToken(), "Game1");
        facade.joinGame(auth1.authToken(), "WHITE", gameId);
        assertThrows(ResponseException.class, () -> facade.joinGame(auth2.authToken(), "WHITE", gameId));
    }
}
