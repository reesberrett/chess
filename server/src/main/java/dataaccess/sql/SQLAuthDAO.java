package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DatabaseManager;
import model.AuthData;
import service.exceptions.DataAccessException;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        DatabaseInitializer.configureDatabase();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if (authData == null || authData.authToken() == null || authData.username() == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, authData.authToken());
            ps.setString(2, authData.username());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create auth: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        String sql = "SELECT token, username FROM auth WHERE token = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("token"), rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get auth: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE token = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to delete auth: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("TRUNCATE TABLE auth")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to clear auth table: " + e.getMessage());
        }
    }
}
