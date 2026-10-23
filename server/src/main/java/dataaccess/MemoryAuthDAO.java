package dataaccess;
import model.*;
import java.util.Map;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, AuthData> tokens = new HashMap<>();

    @Override
    public void createAuth(AuthData auth){
        tokens.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String token){
        return tokens.get(token);
    }

    @Override
    public void deleteAuth(String token){
        tokens.remove(token);
    }

    @Override
    public synchronized void clear(){
        tokens.clear();
    }


}
