package exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String exception) {
        super(exception);
    }
}
