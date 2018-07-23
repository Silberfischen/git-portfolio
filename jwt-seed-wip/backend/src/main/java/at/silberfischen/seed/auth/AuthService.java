package at.silberfischen.seed.auth;

import at.silberfischen.seed.auth.jwt.JwtTokenProvider;
import at.silberfischen.seed.auth.request.SigninRequest;
import at.silberfischen.seed.auth.user.UserRepository;
import at.silberfischen.seed.auth.user.UserWithRoles;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public String signin(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtTokenProvider.createToken(username,
                userRepository
                        .fetchWithRolesByUsername(username)
                        .filter(UserWithRoles::getMailActivated)
                        .orElseThrow(() -> AuthException.builder().build())
                        .getRoles()
        );
    }

    public String signup(UserWithRoles user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //TODO: mail should not be set here..
            user.setMailActivated(true);

            user = userRepository.insertAndFetch(user);

            return jwtTokenProvider.createToken(user.getUsername(), user.getAuthorities());
        } else {
            throw AuthException.builder().errorMessage("auth.error.register.used").httpStatus(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public UserWithRoles search(String username) {
        Optional<UserWithRoles> user = userRepository.fetchWithRolesByUsername(username);
        if (user.isPresent()) {
            throw AuthException.builder().errorMessage("auth.error.login.user").httpStatus(HttpStatus.UNAUTHORIZED).build();
        }
        return user.get();
    }

    public UserWithRoles whoami(HttpServletRequest req) {
        return userRepository.fetchWithRolesByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req))).get();
    }


    public String signin(SigninRequest login) {
        return signin(login.getUsername(), login.getPassword());
    }
}
