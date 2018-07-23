package at.silberfischen.seed.auth.user;

import lombok.Builder;

import javax.validation.constraints.Email;

@Builder
public class UserInformation {

    private Long id;

    @Email
    private String email;
}
