package at.silberfischen.seed.core;

import at.silberfischen.seed.auth.AuthException;
import at.silberfischen.seed.core.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<List<String>> authExceptionHandler(AuthException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getErrorMessages());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.getStackTraceAsString(ex));
    }
}
