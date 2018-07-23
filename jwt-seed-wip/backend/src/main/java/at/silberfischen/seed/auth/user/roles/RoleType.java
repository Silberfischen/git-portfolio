package at.silberfischen.seed.auth.user.roles;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {

    ADMINISTRATOR, RETAILER, USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
