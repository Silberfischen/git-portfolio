package at.silberfischen.seed.auth;


import at.silberfischen.seed.auth.request.SigninRequest;
import at.silberfischen.seed.auth.response.LoginResponse;
import at.silberfischen.seed.auth.response.RegisterResponse;
import at.silberfischen.seed.auth.user.UserWithRoles;
import at.silberfischen.seed.auth.user.roles.RoleType;
import at.silberfischen.seed.jooq.tables.pojos.UserBase;
import com.google.common.collect.Sets;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public UserWithRoles getCurrentUser(HttpServletRequest req) {
        return authService.whoami(req);
    }

    @PostMapping("/signup")
    public RegisterResponse register(@RequestBody @Valid UserBase user) {
        UserWithRoles userWithRoles = UserWithRoles.from(user);

        userWithRoles.setRoles(Sets.newHashSet(RoleType.USER));

        authService.signup(userWithRoles);
        return RegisterResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid SigninRequest login) {
        return ResponseEntity.ok(LoginResponse.builder()
                .token(authService.signin(login))
                .build());
    }

    @PostMapping("/signout")
    public boolean logout(@RequestBody @Valid UserWithRoles logout) {
//        return authService.signout(logout);
        System.out.println("hi");
        return false;
    }
}
