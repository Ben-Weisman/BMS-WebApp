package engine.customExceptions;

public class InvalidBoatNameException extends Exception {
    public InvalidBoatNameException(){
        super("Invalid boat type entered.");
    }
}
