package dataaccess;
import chess.*;
import model.*;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new LinkedHashMap<>();
    private int nextID = 1;

    @Override
    public int createGame(String gameName){
        //assign id to game and increment it (this bug got me for a while lol)
        int gameID = nextID;
        nextID++;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames(){
        return new java.util.ArrayList<>(games.values());
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
    public synchronized void clear(){
        games.clear();
        nextID = 1;
    }
}
