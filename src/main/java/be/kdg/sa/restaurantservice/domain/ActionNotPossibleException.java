package be.kdg.sa.restaurantservice.domain;

public class ActionNotPossibleException extends RuntimeException {
    public ActionNotPossibleException(String message) {
        super(message);
    }
}
