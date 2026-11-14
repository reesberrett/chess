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
        if (params.length != 3) {
            throw new ResponseException(400, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
        }
        var authData = facade.register(params[0], params[1], params[2]);
        repl.setState(Repl.State.SIGNEDIN, authData.authToken());
        return String.format("Registered and logged in as %s.", authData.username());
    }

    private String login(String... params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
        }
        var authData = facade.login(params[0], params[1]);
        repl.setState(Repl.State.SIGNEDIN, authData.authToken());
        return String.format("Logged in as %s.", authData.username());
    }

    public String help() {
        return """
                Available commands:
                  register <USERNAME> <PASSWORD> <EMAIL> - create an account
                  login <USERNAME> <PASSWORD>            - log in to play chess
                  quit                                   - exit the program
                  help                                   - display available commands""";
    }
}
