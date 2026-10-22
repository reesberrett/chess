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

    public AuthData register(RegisterRequest request) throws ServiceException, DataAccessException{
        //if any field is empty or null, throw exception
        if (request == null || request.username() == null || request.password() == null || request.email() == null || request.username().isBlank() || request.password().isBlank() || request.email().isBlank())
            throw new BadRequestException("Error: bad request");

        //if username is already in userDAO, throw exception
        if (userDAO.getUser(request.username()) != null)
            throw new AlreadyTakenException("Error: already taken");

        //make new user with given data and add it to authDAO
        userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());
        authDAO.createAuth(auth);
        return auth;
    }

    public AuthData login(LoginRequest request) throws ServiceException, DataAccessException{
        //if user fields are null, throw exception
        if (request == null || request.username() == null || request.password() == null)
            throw new BadRequestException("Error: bad request");

        //if user fields are incorrect, throw exception
        UserData user = userDAO.getUser(request.username());
        if (user == null || !user.password().equals(request.password()))
            throw new UnauthorizedException("Error: unauthorized");

        //create new token and authorization to add to authDAO
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, request.username());

        authDAO.createAuth(auth);
        return auth;
    }

    public void logout(String token) throws ServiceException, DataAccessException{
        //if no token given, throw exception
        if (token == null || authDAO.getAuth(token) == null)
            throw new UnauthorizedException("Error: unauthorized");

        //remove authorization from authDAO
        authDAO.deleteAuth(token);
    }
}
