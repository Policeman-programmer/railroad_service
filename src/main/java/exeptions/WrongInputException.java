package exeptions;

public class WrongInputException extends Throwable {
    public WrongInputException(String message) {
        super(message);
    }
}
