package service;
import dataaccess.*;
import model.*;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(RegisterRequest request){
        //TODO
    }

    public AuthData login(LoginRequest request){
        //TODO
    }

    public void logout(String token){
        //TODO
    }
}
