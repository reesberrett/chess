package dataaccess.sql;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exceptions.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest {

    private SQLAuthDAO authDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAndGetAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token-123", "alice");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("token-123");
        assertNotNull(retrieved);
        assertEquals("token-123", retrieved.authToken());
        assertEquals("alice", retrieved.username());
    }

    @Test
    public void createAuthDuplicateNegative() throws DataAccessException {
        AuthData auth = new AuthData("token-123", "alice");
        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        assertNull(authDAO.getAuth("invalid-token"));
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token-123", "alice");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("token-123");

        assertNull(authDAO.getAuth("token-123"));
    }

    @Test
    public void deleteAuthNegative() {
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistent-token"));
    }

    @Test
    public void clearPositive() throws DataAccessException {
        authDAO.createAuth(new AuthData("token-1", "alice"));
        authDAO.createAuth(new AuthData("token-2", "bob"));
        authDAO.clear();

        assertNull(authDAO.getAuth("token-1"));
        assertNull(authDAO.getAuth("token-2"));
    }
}