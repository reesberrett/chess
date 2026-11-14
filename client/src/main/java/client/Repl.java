package client;

import java.util.Scanner;

public class Repl {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private State state = State.SIGNEDOUT;

    public enum State { SIGNEDOUT, SIGNEDIN }

    public Repl(String serverUrl) {
        var facade = new ServerFacade(serverUrl);
        this.preLoginClient = new PreLoginClient(facade, this);
        this.postLoginClient = new PostLoginClient(facade, this);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type 'help' to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = (state == State.SIGNEDOUT)
                        ? preLoginClient.eval(line)
                        : postLoginClient.eval(line);
                if (!result.equals("quit") && !result.isEmpty()) {
                    System.out.println(result);
                }
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setState(State newState, String authToken) {
        this.state = newState;
        postLoginClient.setAuthToken(authToken);
    }

    private void printPrompt() {
        String status = (state == State.SIGNEDOUT) ? "[LOGGED_OUT]" : "[LOGGED_IN]";
        System.out.print("\n" + status + " >>> ");
    }
}