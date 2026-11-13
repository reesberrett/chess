package dataaccess.sql;

import dataaccess.AuthDAO;
import model.AuthData;
import service.exceptions.DataAccessException;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        //TODO
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        //TODO
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        //TODO
    }

    @Override
    public void clear() throws DataAccessException {
        //TODO
    }
}
