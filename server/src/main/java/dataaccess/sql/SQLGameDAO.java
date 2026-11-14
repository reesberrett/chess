package dataaccess.sql;

import dataaccess.GameDAO;
import model.GameData;
import service.exceptions.DataAccessException;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{

    @Override
    public int createGame(String gameName) throws DataAccessException {
        //TODO
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        //TODO
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        //TODO
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        //TODO
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        //TODO
    }

    @Override
    public void clear() throws DataAccessException {
        //TODO
    }
}
