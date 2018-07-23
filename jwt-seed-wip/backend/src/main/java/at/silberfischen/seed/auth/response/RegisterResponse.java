package at.silberfischen.seed.auth.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private boolean success;
}
