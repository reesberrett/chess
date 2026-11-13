package dataaccess.sql;

import dataaccess.UserDAO;
import service.exceptions.DataAccessException;
import model.UserData;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        DatabaseInitializer.configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        //TODO
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        //TODO
    }

    @Override
    public void clear() throws DataAccessException {
        //TODO
    }
}
