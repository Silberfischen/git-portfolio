package at.silberfischen.seed.auth;

import at.silberfischen.seed.core.GenericException;
import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;

public class AuthException extends GenericException {

    @Builder
    public AuthException(@Singular List<String> errorMessages, HttpStatus httpStatus) {
        super(errorMessages, httpStatus);
    }
}
