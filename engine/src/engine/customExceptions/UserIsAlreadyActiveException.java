package engine.customExceptions;

public class UserIsAlreadyActiveException extends Exception{
    public UserIsAlreadyActiveException (String message){
        super(message);
    }
}
