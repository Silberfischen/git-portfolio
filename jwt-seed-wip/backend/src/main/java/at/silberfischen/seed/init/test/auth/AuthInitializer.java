package at.silberfischen.seed.init.test.auth;

import at.silberfischen.seed.auth.AuthService;
import at.silberfischen.seed.auth.user.UserWithRoles;
import at.silberfischen.seed.auth.user.roles.RoleType;
import at.silberfischen.seed.core.Profiles;
import at.silberfischen.seed.init.Initializer;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component
@Profile(Profiles.TEST)
public class AuthInitializer extends Initializer {

    private final AuthService authService;

    public AuthInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void init() {
        persistUser("admin", "admin");
        persistUser("user", "user",
                Sets.newHashSet(RoleType.USER));
        persistUser("retailer", "retailer",
                Sets.newHashSet(RoleType.RETAILER));
    }

    private boolean persistUser(String username, String password) {
        return persistUser(username, password, Sets.newHashSet(RoleType.ADMINISTRATOR));
    }

    private boolean persistUser(String username, String password, Set<RoleType> roles) {
        UserWithRoles user = UserWithRoles.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();

        return StringUtils.hasText(authService.signup(user));
    }

}
