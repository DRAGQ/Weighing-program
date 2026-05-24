package org.example.ivoprojekt.dao;

import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.domain.Partner;
import org.example.ivoprojekt.domain.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(User.class)
@RegisterBeanMapper(Partner.class)
@RegisterBeanMapper(DialUserResponse.class)
public interface UserDao {

    @SqlQuery("SELECT * FROM user")
    List<User> findAll();

    @SqlQuery("SELECT * FROM user WHERE id = ?")
        User getUserById(Integer id);

    @SqlQuery("SELECT * FROM user WHERE login = ?")
        Optional<User> getUserByLogin(String login);

    @SqlUpdate("INSERT INTO user (login, name, password, salt, is_active, is_admin, is_protected, partner_id) VALUES (:login, :name, :password, :salt, :isActive, :isAdmin, :isProtected, :partnerId)")
    @GetGeneratedKeys
        Integer save(@BindBean User user);

    @SqlUpdate("UPDATE user SET login = :login, name = :name, is_admin = :isAdmin, is_active = :isActive WHERE id = :id")
        void updateLoginAndName(@BindBean DialUserResponse userInformations);

    @SqlUpdate("UPDATE user SET password = ?, salt = ? WHERE id = ?")
    void changePassword(String password, String salt, Integer id);

    @SqlUpdate("DELETE FROM user WHERE id = ?")
    void deleteUser(Integer userId);

    @SqlUpdate("DELETE FROM partner WHERE id = (SELECT partner_id FROM user WHERE id = ?)")
    void deletePartnerById(Integer id);

//    @SqlUpdate("DELETE FROM partner AS p INNER JOIN user AS u ON u.partner_id = p.id WHERE u.id = ?")
//    void deletePartnerById(Integer id);

    /*private final Jdbi jdbi;

    public UserRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void saveUser(User user) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO user (login, name) VALUES (:login, :name)")
                    .bind("login", user.getLogin())
                    .bind("name", user.getName())
                    .execute();
        });
    }

    public List<User> findAll() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, login, name FROM user")
                        .mapToBean(User.class)
                        .list()
        );
    }*/

}
