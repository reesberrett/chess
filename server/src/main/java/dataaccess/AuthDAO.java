package dataaccess;
import model.*;
import service.exceptions.DataAccessException;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    void clear() throws DataAccessException;
}
