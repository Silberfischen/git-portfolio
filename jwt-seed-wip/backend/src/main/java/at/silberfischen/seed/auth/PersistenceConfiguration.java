package at.silberfischen.seed.auth;

import at.silberfischen.seed.auth.user.UserRepository;
import at.silberfischen.seed.jooq.Tables;
import at.silberfischen.seed.jooq.tables.pojos.UserBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    public UserRepository getUserRepository(org.jooq.Configuration config) {
        return new UserRepository(Tables.USER_BASE, UserBase.class, config);
    }
}
