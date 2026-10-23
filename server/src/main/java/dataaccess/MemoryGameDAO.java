package dataaccess;
import chess.*;
import model.*;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public int createGame(String gameName){
        int gameID = nextID + 1;
        GameData game = new GameData(gameID, gameName, new ChessGame(), null, null);
        games.put(gameID, game);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames(){
        return games.values();
    }

    @Override
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }

    @Override
    public void deleteGame(GameData game){
        games.remove(game.gameID());
    }

    @Override
    public void clear(){
        games.clear();
        nextID = 1;
    }
}
