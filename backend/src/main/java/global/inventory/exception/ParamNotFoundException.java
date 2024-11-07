package global.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParamNotFoundException extends RuntimeException {
    public ParamNotFoundException(String value) {
        super(String.format("Missing request param: %s", value));
    }
}
