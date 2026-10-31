package service.exceptions;

public class AlreadyTakenException extends ServiceException {

    public AlreadyTakenException(String message){
        super(403, message);
    }
}
