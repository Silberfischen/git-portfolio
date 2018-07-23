package at.silberfischen.seed.auth.user;

import at.silberfischen.seed.auth.user.roles.RoleType;
import at.silberfischen.seed.jooq.tables.pojos.UserBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserWithRoles {

    private Long id;

    private String username;
    private String password;

    private Boolean mailActivated;

    private Set<RoleType> roles;


    @JsonIgnore
    public boolean isAdmin() {
        return roles.contains(RoleType.ADMINISTRATOR);
    }

    @JsonIgnore
    public boolean isRetailer() {
        return roles.contains(RoleType.RETAILER);
    }


    public Set<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public static UserWithRoles from(UserBase user) {
        return UserWithRoles.builder()
                .id(user.getId())
                .mailActivated(user.getMailActivated())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
