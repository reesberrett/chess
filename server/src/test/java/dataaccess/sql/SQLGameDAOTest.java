package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.DataAccessException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {

    private SQLGameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createAndGetGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("Championship Match");
        assertTrue(gameID > 0);

        GameData retrieved = gameDAO.getGame(gameID);
        assertNotNull(retrieved);
        assertEquals("Championship Match", retrieved.gameName());
        assertNull(retrieved.whiteUsername());
        assertNull(retrieved.blackUsername());
        assertEquals(new ChessGame(), retrieved.game());
    }

    @Test
    public void createGameNullNameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        assertNull(gameDAO.getGame(99999));
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        gameDAO.createGame("Game 1");
        gameDAO.createGame("Game 2");

        Collection<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesEmptyNegative() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void updateGamePositive() throws Exception {
        int gameID = gameDAO.createGame("Move Test");
        GameData initial = gameDAO.getGame(gameID);

        // Make a legal opening pawn move (e2 -> e4) to verify ChessGame serialization persists moves
        ChessGame updatedChess = initial.game();
        updatedChess.makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(4, 5), null));

        GameData updatedData = new GameData(gameID, "whitePlayer", "blackPlayer", "Move Test", updatedChess);
        gameDAO.updateGame(updatedData);

        GameData retrieved = gameDAO.getGame(gameID);
        assertEquals("whitePlayer", retrieved.whiteUsername());
        assertEquals("blackPlayer", retrieved.blackUsername());
        assertEquals(updatedChess, retrieved.game());
    }

    @Test
    public void updateGameNonexistentNegative() {
        GameData fakeGame = new GameData(99999, "white", "black", "Fake", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(fakeGame));
    }

    @Test
    public void deleteGamePositive() throws DataAccessException {
        int gameID = gameDAO.createGame("Delete Me");
        GameData game = gameDAO.getGame(gameID);

        gameDAO.deleteGame(game);
        assertNull(gameDAO.getGame(gameID));
    }

    @Test
    public void deleteGameNegative() {
        GameData fakeGame = new GameData(99999, null, null, "Fake", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.deleteGame(fakeGame));
    }

    @Test
    public void clearPositive() throws DataAccessException {
        int id1 = gameDAO.createGame("Game A");
        int id2 = gameDAO.createGame("Game B");
        gameDAO.clear();

        assertNull(gameDAO.getGame(id1));
        assertNull(gameDAO.getGame(id2));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}