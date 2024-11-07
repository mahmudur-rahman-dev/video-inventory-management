package global.inventory.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message("Validation Error")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(CustomSecurityException.class)
    public ResponseEntity<Object> handleCustomSecurityException(CustomSecurityException ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());

    }

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException ex, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .createdAt(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);

        ApiError apiError = ApiError.builder()
                .message("Some Error Occurred")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());

    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        log.error(ex.getMessage(), ex);

        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.FORBIDDEN)
                .createdAt(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        /*List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ApiError apiError = ApiError.builder()
                .message(errors.toString())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());*/

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));


        ApiError apiError = ApiError.builder()
                .errors(errors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .createdAt(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(value = ParamNotFoundException.class)
    public ResponseEntity<Object> handleParamNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .createdAt(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(value = ResourceNotModifiedException.class)
    public ResponseEntity<ApiError> handleResourceNotModifiedException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_MODIFIED)
                .createdAt(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        ApiError apiError = ApiError.builder()
                .errors(errors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .createdAt(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
