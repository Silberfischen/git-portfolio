package at.silberfischen.seed.core;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Singular;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class GenericException extends RuntimeException {

    @Singular
    private List<String> errorMessages = Lists.newArrayList();
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

}
