package dataaccess;
import model.*;
import service.exceptions.DataAccessException;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
