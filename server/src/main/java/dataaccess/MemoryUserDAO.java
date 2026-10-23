package dataaccess;
import model.*;
import java.util.Map;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData user){
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username){
        return users.get(username);
    }

    @Override
    public void deleteUser(String username){
        users.remove(username);
    }

    @Override
    public synchronized void clear(){
        users.clear();
    }
}
