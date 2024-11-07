package global.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class ResourceNotModifiedException extends RuntimeException {
    public ResourceNotModifiedException(String resource) {
        super(String.format("%s not created/modified", resource));
    }
}
