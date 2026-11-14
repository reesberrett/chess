package client;

import java.util.Arrays;

public class PreLoginClient {

    private final ServerFacade facade;
    private final Repl repl;

    public PreLoginClient(ServerFacade facade, Repl repl) {
        this.facade = facade;
        this.repl = repl;
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.trim().split("\\s+");
        var cmd = (tokens.length > 0 && !tokens[0].isEmpty()) ? tokens[0].toLowerCase() : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            case "help" -> help();
            default -> "Unknown command. Type 'help' for available commands.";
        };
    }

    private String register(String... params) throws ResponseException {
        //TODO
    }

    private String login(String... params) throws ResponseException {
        //TODO
    }

    private String help() throws ResponseException {
        //TODO
    }
}
