package dataaccess;
import model.*;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void delete(String token) throws DataAccessException;

    void clear() throws DataAccessException;
}
