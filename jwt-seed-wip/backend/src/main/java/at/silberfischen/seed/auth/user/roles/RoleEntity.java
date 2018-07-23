package at.silberfischen.seed.auth.user.roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RoleEntity {

    private Long id;

    private RoleType type;

}
