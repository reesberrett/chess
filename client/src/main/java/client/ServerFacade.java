package client;

import com.google.gson.Gson;
import model.*;

import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URI;
import java.net.HttpURLConnection;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        var body = Map.of("username", username, "password", password, "email", email);
        return makeRequest("POST", "/user", body, null, AuthData.class);
    }

    public AuthData login(String username, String password) throws ResponseException {
        var body = Map.of("username", username, "password", password);
        return makeRequest("POST", "/session", body, null, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        makeRequest("DELETE", "/session", null, authToken, null);
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        var body = Map.of("gameName", gameName);
        record CreateResult(int gameID) {}
        var res = makeRequest("POST", "/game", body, authToken, CreateResult.class);
        return res.gameID();
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        record ListResult(List<GameData> games) {}
        var res = makeRequest("GET", "/game", null, authToken, ListResult.class);
        return res.games();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        var body = Map.of("playerColor", playerColor, "gameID", gameID);
        makeRequest("PUT", "/game", body, authToken, null);
    }

    public void clear() throws ResponseException {
        makeRequest("DELETE", "/db", null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (request != null) {
                http.addRequestProperty("Content-Type", "application/json");
                String reqData = gson.toJson(request);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (status / 100 != 2) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    var map = gson.fromJson(new InputStreamReader(respErr), Map.class);
                    String msg = (map != null && map.containsKey("message"))
                            ? map.get("message").toString().replace("Error: ", "")
                            : "Request failed";
                    throw new ResponseException(status, msg);
                }
            }
            throw new ResponseException(status, "Request failed");
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        T response = null;
        if (responseClass != null) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                response = gson.fromJson(reader, responseClass);
            }
        }
        return response;
    }
}
