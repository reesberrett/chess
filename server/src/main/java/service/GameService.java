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

    //helper function to verify tokens before preceding
    private AuthData verify(String token) throws UnauthorizedException, DataAccessException{
        //if token is null, throw exception
        if (token == null)
            throw new UnauthorizedException("Error: unauthorized");

        //if token is not in authDAO, throw exception
        AuthData auth = authDAO.getAuth(token);
        if (auth == null)
            throw new UnauthorizedException("Error: unauthorized");

        return auth;
    }

    public ListGamesResult listGames(String token) throws ServiceException, DataAccessException{
        //make sure token is verified, then return gameData for all games
        verify(token);
        return new ListGamesResult(gameDAO.listGames());
    }

    public Map<String, Integer> createGame(String token, CreateGameRequest request) throws ServiceException, DataAccessException{
        verify(token);
        //if game name of request is blank or doesnt exist, throw exception
        if (request == null || request.gameName() == null || request.gameName().isBlank())
            throw new BadRequestException("Error: bad request");

        //add game as a map with an ID onto gameDAO
        int id = gameDAO.createGame(request.gameName());
        return Map.of("gameID", id);
    }

    public void joinGame(String token, JoinGameRequest request) throws ServiceException, DataAccessException{
        AuthData auth = verify(token);
        //if requests, colors, or the game does not exist, throw exception
        if (request == null || request.color() == null)
            throw new BadRequestException("Error: bad request");

        GameData game = gameDAO.getGame((request.gameID()));
        if (game == null)
            throw new BadRequestException("Error: bad request");

        String newWhite = game.whiteUsername();
        String newBlack = game.blackUsername();

        //if request color is already taken, throw exception. If not, add username of request to AuthData
        if (request.color() == ChessGame.TeamColor.WHITE){
            if (newWhite != null)
                throw new AlreadyTakenException("Error: already taken");
            newWhite = auth.username();
        }
        else if (request.color() == ChessGame.TeamColor.BLACK){
            if (newBlack != null)
                throw new AlreadyTakenException("Error: already take");
            newBlack = auth.username();
        }
        else
            throw new BadRequestException("Error: bad request");

        //update game to include new usernames
        GameData updatedGame = new GameData(game.gameID(), game.gameName(), game.game(), newWhite, newBlack);
        gameDAO.updateGame(updatedGame);
    }
}
