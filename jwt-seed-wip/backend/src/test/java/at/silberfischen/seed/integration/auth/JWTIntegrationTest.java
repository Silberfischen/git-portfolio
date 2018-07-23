package at.silberfischen.seed.integration.auth;

import at.silberfischen.seed.auth.request.AuthType;
import at.silberfischen.seed.auth.request.SigninRequest;
import at.silberfischen.seed.core.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static at.silberfischen.seed.utils.MapperUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles(Profiles.TEST)
@SpringBootTest
public class JWTIntegrationTest {

    private final String SIGNIN = "/user/signin";
    private final String SIGNOUT = "/user/signout";
    private final String SIGNUP = "/user/signup";
    private final String CURRENT_USER = "/user";

    private static final MediaType APPLICATION_JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Autowired
    protected WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void singin_shouldFail() throws Exception {

        SigninRequest signinRequest = SigninRequest.builder()
                .username("notavalidusername")
                .password("notavalidpassword")
                .authType(AuthType.EMAIL)
                .build();

        String requestJson = toJson(signinRequest);

        mvc.perform(post(SIGNIN).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void signin_shouldSucceed() throws Exception {

        SigninRequest signinRequest = SigninRequest.builder()
                .username("avalidusername")
                .password("avalidpassword")
                .authType(AuthType.EMAIL)
                .build();

        String requestJson = toJson(signinRequest);

        mvc.perform(post(SIGNUP).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());

        //set it to invalid
        signinRequest.setPassword("notavalidpassword");
        requestJson = toJson(signinRequest);

        mvc.perform(post(SIGNIN).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isUnauthorized());

        //and valid again
        signinRequest.setPassword("avalidpassword");
        requestJson = toJson(signinRequest);

        mvc.perform(post(SIGNIN).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}