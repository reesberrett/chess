package service;

public class BadRequestException extends ServiceException{

    public BadRequestException(String message){
        super(400, message);
    }
}
