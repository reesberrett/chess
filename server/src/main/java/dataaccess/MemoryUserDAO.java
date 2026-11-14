package dataaccess;
import model.*;
import java.util.Map;
import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData user) {
        String hashPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        users.put(user.username(), new UserData(user.username(), hashPassword, user.email()));
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public synchronized void clear(){
        users.clear();
    }
}
