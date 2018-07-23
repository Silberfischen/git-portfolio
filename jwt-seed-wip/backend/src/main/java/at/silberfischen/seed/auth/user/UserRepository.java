package at.silberfischen.seed.auth.user;


import at.silberfischen.seed.auth.user.roles.RoleType;
import at.silberfischen.seed.database.DatabaseException;
import at.silberfischen.seed.jooq.tables.pojos.UserBase;
import at.silberfischen.seed.jooq.tables.records.UserBaseRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jooq.*;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

import static at.silberfischen.seed.jooq.Tables.USER_BASE;
import static at.silberfischen.seed.jooq.Tables.USER_ROLE;

public class UserRepository extends DAOImpl<UserBaseRecord, UserBase, Long> {

    private final DSLContext sql;


    public UserRepository(Table<UserBaseRecord> table, Class<UserBase> type, Configuration configuration) {
        super(table, type, configuration);
        this.sql = DSL.using(configuration);
    }

    public Optional<UserWithRoles> fetchWithRolesByUsername(@NotNull String username) {
        Result<Record> userWithRolesRecord = sql.selectFrom(USER_BASE
                .join(USER_ROLE).onKey())
                .where(USER_BASE.USERNAME.eq(username))
                .fetch();

        return mapToUserWithRoles(userWithRolesRecord);
    }

    public boolean existsByUsername(String username) {
        return sql.select(USER_BASE.ID).from(USER_BASE)
                .where(USER_BASE.USERNAME.eq(username))
                .fetchOne() != null;
    }

    public int deleteByUsername(String username) {
        return sql.deleteFrom(USER_BASE).where(USER_BASE.USERNAME.eq(username)).execute();
    }

    @Override
    protected Long getId(UserBase object) {
        return object.getId();
    }

    public UserWithRoles insertAndFetch(UserWithRoles user) {
        Result<UserBaseRecord> res = sql.insertInto(USER_BASE)
                .columns(USER_BASE.USERNAME, USER_BASE.MAIL_ACTIVATED, USER_BASE.PASSWORD, USER_BASE.USER_INFO_ID)
                .values(user.getUsername(), user.getMailActivated() != null ? user.getMailActivated() : false, user.getPassword(), null).returning().fetch();

        if (res.isEmpty()) {
            throw new DatabaseException(
                    Lists.newArrayList("Could not insert user " + user.getUsername() + " into datbase!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final long userBaseId = res.get(0).getId();

        user.getAuthorities().forEach(role ->
                sql.insertInto(USER_ROLE)
                        .columns(USER_ROLE.USER_BASE_ID, USER_ROLE.ROLE_TYPE)
                        .values(userBaseId, role.getAuthority()).execute()
        );

        user.setId(userBaseId);

        return user;
    }

    private Optional<UserWithRoles> mapToUserWithRoles(Result<Record> userWithRolesRecord) {
        if (userWithRolesRecord.isEmpty()) {
            return Optional.empty();
        }

        boolean baseUserInitialized = false;

        UserWithRoles user = new UserWithRoles();
        Set<RoleType> roles = Sets.newHashSet();

        for (Record r : userWithRolesRecord) {
            if (!baseUserInitialized) {
                user.setId(r.get(USER_BASE.ID));
                user.setMailActivated(r.get(USER_BASE.MAIL_ACTIVATED));
                user.setUsername(r.get(USER_BASE.USERNAME));
                user.setPassword(r.get(USER_BASE.PASSWORD));
            }

            roles.add(RoleType.valueOf(r.get(USER_ROLE.ROLE_TYPE)));
        }

        user.setRoles(roles);
        return Optional.of(user);
    }
}