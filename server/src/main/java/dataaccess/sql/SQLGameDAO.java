package dataaccess.sql;

import dataaccess.GameDAO;
import dataaccess.DatabaseManager;
import model.GameData;
import service.exceptions.DataAccessException;
import chess.ChessGame;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

public class SQLGameDAO implements GameDAO{

    private final Gson gson = new Gson();

    public SQLGameDAO() throws DataAccessException {
        DatabaseInitializer.configureDatabase();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.isBlank()) {
            throw new DataAccessException("Error: bad request");
        }
        String sql = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String gameJson = gson.toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, null);
            ps.setString(2, null);
            ps.setString(3, gameName);
            ps.setString(4, gameJson);
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create game: " + e.getMessage());
        }
        throw new DataAccessException("Unable to retrieve generated game ID");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("gameName"), chessGame
                    );
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                ChessGame chessGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                games.add(new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName"),
                        chessGame
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to list games: " + e.getMessage());
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        String gameJson = gson.toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, game.whiteUsername());
            ps.setString(2, game.blackUsername());
            ps.setString(3, game.gameName());
            ps.setString(4, gameJson);
            ps.setInt(5, game.gameID());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Error: game does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update game: " + e.getMessage());
        }
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sql = "DELETE FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, game.gameID());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("Error: game does not exist");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to delete game: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("TRUNCATE TABLE game")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to clear game table: " + e.getMessage());
        }
    }
}
