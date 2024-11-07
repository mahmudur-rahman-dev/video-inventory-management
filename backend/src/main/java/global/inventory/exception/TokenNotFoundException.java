package global.inventory.exception;


public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(String.format("Failed: %s", message));
    }

    public TokenNotFoundException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
