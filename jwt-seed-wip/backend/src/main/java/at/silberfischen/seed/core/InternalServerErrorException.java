package at.silberfischen.seed.core;

import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InternalServerErrorException extends GenericException {

    @Builder
    public InternalServerErrorException(@Singular List<String> errorMessages, HttpStatus httpStatus) {
        super(errorMessages, httpStatus);
    }
}
