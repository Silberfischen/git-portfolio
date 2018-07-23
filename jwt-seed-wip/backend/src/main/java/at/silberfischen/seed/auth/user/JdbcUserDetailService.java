package at.silberfischen.seed.auth.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JdbcUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public JdbcUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserWithRoles> optUser = userRepository.fetchWithRolesByUsername(username);

        if (!optUser.isPresent()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        final UserWithRoles user = optUser.get();

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

