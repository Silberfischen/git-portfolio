package at.silberfischen.seed.auth.user;

import at.silberfischen.seed.auth.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static at.silberfischen.seed.core.I18nAssert.hasText;
import static at.silberfischen.seed.core.I18nAssert.notNull;

@Component
@Slf4j
public class JdbcAuthManager implements AuthenticationManager {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public JdbcAuthManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        notNull(authentication);

        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";
        hasText(username);
        hasText(password);

        Optional<UserWithRoles> optUser = userRepository.fetchWithRolesByUsername(username);

        UserWithRoles user = optUser.orElseThrow(() -> AuthException.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .errorMessage("auth.error.signin.username")
                .build()
        );

        if (!passwordEncoder.matches(password, optUser.get().getPassword())) {
            throw AuthException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .errorMessage("auth.error.signin.password")
                    .build();
        }


        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}