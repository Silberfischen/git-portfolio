package at.silberfischen.seed.database;

import at.silberfischen.seed.core.GenericException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class DatabaseException extends GenericException {

    public DatabaseException(List<String> errorMessages, HttpStatus httpStatus) {
        super(errorMessages, httpStatus);
    }
}
