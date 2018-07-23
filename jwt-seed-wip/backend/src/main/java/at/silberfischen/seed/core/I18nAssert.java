package at.silberfischen.seed.core;

import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class I18nAssert {

    public static void notNull(@Nullable Object object, HttpStatus status, String... messages) {
        if (object == null) {
            throw InternalServerErrorException.builder()
                    .httpStatus(status)
                    .errorMessages(Lists.newArrayList(messages))
                    .build();
        }
    }

    public static void notNull(@Nullable Object object, String... messages) {
        if (object == null) {
            throw InternalServerErrorException.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorMessages(Lists.newArrayList(messages))
                    .build();
        }
    }

    public static void notNull(@Nullable Object object) {
        if (object == null) {
            throw InternalServerErrorException.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorMessage("generic.null")
                    .build();
        }
    }

    public static void hasText(@Nullable String text, String... messages) {
        if (!StringUtils.hasText(text)) {
            throw InternalServerErrorException.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorMessage("generic.null")
                    .build();        }
    }


    public static void hasText(@Nullable String text, HttpStatus status, String... messages) {
    }
}
