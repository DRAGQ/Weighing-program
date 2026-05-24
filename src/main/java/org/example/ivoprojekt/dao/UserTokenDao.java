package org.example.ivoprojekt.dao;


import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.domain.UserToken;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(UserToken.class)
@RegisterBeanMapper(User.class)
public interface UserTokenDao {

    @SqlQuery("SELECT * FROM user_token")
    UserToken findAll();

    @SqlQuery("SELECT token FROM user_token WHERE user_id = ?")
    String findUserTokenByUserId(Integer userId);

    @SqlQuery("SELECT * FROM user_token WHERE token = ?")
    UserToken findByToken(String token);

    @SqlQuery("SELECT u.id, u.login, u.name, u.password, is_active AS isActive, u.is_admin AS isAdmin, u.is_protected AS isProtected, u.partner_id FROM user u INNER JOIN user_token ut ON u.id = ut.user_id WHERE ut.token = ?")
    User findUserByToken(String token);

    @SqlUpdate("INSERT INTO user_token (token, user_id) VALUES (?, ?)")
    void save(String token, Integer userId);

    @SqlUpdate("DELETE FROM user_token WHERE token = ?")
    void deleteToken(String token);
}
