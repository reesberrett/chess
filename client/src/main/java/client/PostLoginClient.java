package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        //TODO
    }

    private String listGames() throws ResponseException {
        //TODO
    }

    private String joinGame(String... params) throws ResponseException {
        //TODO
    }

    private String observeGame(String... params) throws ResponseException {
        //TODO
    }

    private String logout() throws ResponseException {
        //TODO
    }

    private String help() throws ResponseException {
        //TODO
    }
}
