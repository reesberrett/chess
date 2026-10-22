package service;
import chess.ChessGame;
import dataaccess.*;
import model.*;
import java.util.Map;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    private AuthData verify(String token){
        //TODO
    }

    public ListGamesResult listGames(String token){
        //TODO
    }

    public Map<String, Integer> createGame(String token, JoinGameRequest request){
        //TODO
    }

    public void joinGame(String token, JoinGameRequest request){
        //TODO
    }


}
