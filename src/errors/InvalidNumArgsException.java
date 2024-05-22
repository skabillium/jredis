package errors;

public class InvalidNumArgsException extends Exception {
    public InvalidNumArgsException(String command) {
        super(String.format("Invalid number of arguments for '' command", command));
    }
}
