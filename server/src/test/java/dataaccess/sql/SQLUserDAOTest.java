package dataaccess.sql;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.exceptions.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {

    private SQLUserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("alice", "secret123", "alice@byu.edu");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("alice");
        assertNotNull(retrieved);
        assertEquals("alice", retrieved.username());
        assertEquals("alice@byu.edu", retrieved.email());
        assertNotEquals("secret123", retrieved.password());
        assertTrue(BCrypt.checkpw("secret123", retrieved.password()));
    }

    @Test
    public void createUserDuplicateNegative() throws DataAccessException {
        UserData user = new UserData("alice", "secret123", "alice@byu.edu");
        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        assertNull(userDAO.getUser("nonexistentUser"));
    }

    @Test
    public void clearPositive() throws DataAccessException {
        userDAO.createUser(new UserData("bob", "pass", "bob@byu.edu"));
        userDAO.clear();
        assertNull(userDAO.getUser("bob"));
    }

    @Test
    public void createUserNullFieldsNegative() {
        assertThrows(DataAccessException.class, () -> userDAO.createUser(null));
        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData(null, "pass", "a@b.com")));
        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("alice", null, "a@b.com")));
        assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("alice", "pass", null)));
    }
}