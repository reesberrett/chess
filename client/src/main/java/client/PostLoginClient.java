package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.ChessBoard;
import model.GameData;

public class PostLoginClient {
    private final ServerFacade facade;
    private final Repl repl;
    private String authToken;
    private List<GameData> savedGames;

    public PostLoginClient(ServerFacade facade, Repl repl) {
        this.facade = facade;
        this.repl = repl;
        this.savedGames = new ArrayList<>();
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        this.savedGames.clear();
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.trim().split("\\s+");
        var cmd = (tokens.length > 0 && !tokens[0].isEmpty()) ? tokens[0].toLowerCase() : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (cmd) {
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "logout" -> logout();
            case "help" -> help();
            default -> "Unknown command. Type 'help' for available commands.";
        };
    }

    private String createGame(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: create <NAME>");
        }
        facade.createGame(authToken, params[0]);
        return String.format("Created game %s.", params[0]);
    }

    private String listGames() throws ResponseException {
        savedGames = facade.listGames(authToken);
        if (savedGames.isEmpty()) {
            return "No games currently available";
        }
        StringBuilder sb = new StringBuilder("Games:\n");
        for (int i = 0; i < savedGames.size(); i++) {
            GameData game = savedGames.get(i);
            String white = (game.whiteUsername() != null) ? game.whiteUsername() : "open";
            String black = (game.blackUsername() != null) ? game.blackUsername() : "open";
            sb.append(String.format("  %d. %s (White: %s | Black: %s)%n", i + 1, game.gameName(), white, black));
        }
        return sb.toString().stripTrailing();
    }

    private String joinGame(String... params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: join <ID> [WHITE|BLACK]");
        }
        GameData game = getGameByListNumber(params[0]);
        String color = params[1].toUpperCase();
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            throw new ResponseException(400, "Team color must be WHITE or BLACK");
        }
        facade.joinGame(authToken, color, game.gameID());
        drawInitialBoard(color.equals("WHITE"));
        return String.format("Joined '%s' as %s.", game.gameName(), color);
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: observe <ID>");
        }
        GameData game = getGameByListNumber(params[0]);
        drawInitialBoard(true);
        return String.format("Observing '%s'.", game.gameName());
    }

    private String logout() throws ResponseException {
        facade.logout(authToken);
        repl.setState(Repl.State.SIGNEDOUT, null);
        return "Logged out.";
    }

    private GameData getGameByListNumber(String numberStr) throws ResponseException {
        if (savedGames.isEmpty()) {
            throw new ResponseException(400, "Run 'list' first to see available game numbers");
        }
        int index;
        try {
            index = Integer.parseInt(numberStr) - 1;
        } catch (NumberFormatException _) {
            throw new ResponseException(400, "Game number must be a valid integer");
        }
        if (index < 0 || index >= savedGames.size()) {
            throw new ResponseException(400, "Invalid game number. Run 'list' to see valid numbers");
        }
        return savedGames.get(index);
    }

    private void drawInitialBoard(boolean whiteView) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        BoardDrawer.drawBoard(board, whiteView);
    }

    public String help() {
        return """
                Available commands:
                  create <NAME>             - create a new game
                  list                      - list existing games
                  join <ID> [WHITE|BLACK]   - join a game as White or Black
                  observe <ID>              - observe a game
                  logout                    - log out of your account
                  help                      - display available commands""";
    }
}
