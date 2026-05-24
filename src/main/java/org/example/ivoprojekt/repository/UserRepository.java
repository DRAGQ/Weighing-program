package org.example.ivoprojekt.repository;

import org.example.ivoprojekt.api.mapper.DtoMapper;
import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.dao.UserDao;
import org.example.ivoprojekt.dao.UserTokenDao;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.domain.UserToken;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
    }

    public List<User> getAllUsers() {
        try {
            return userDao.findAll();
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to fetch users", e);
        }
    }

    public User getUserById(Integer id) {
        try {
            return userDao.getUserById(id);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to fetch user by id", e);
        }
    }

    public Optional<User> getUserByLogin(String login) {
        try {
            return userDao.getUserByLogin(login);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to fetch user by login", e);
        }
    }

    public Integer save(User user) {
        try {
            return userDao.save(user);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to save user", e);
        }
    }

    public void updateLoginAndName(DialUserResponse userInformations) {
        try {
            System.out.println("ukladam: " +  userInformations);
            userDao.updateLoginAndName(userInformations);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to update login and name", e);
        }
    }

    public void changePassword(String password, String salt, Integer id) {
        try {
            userDao.changePassword(password, salt, id);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to change password", e);
        }
    }

    public void delete(Integer userId) {
        try {
            userDao.deleteUser(userId);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to delete user", e);
        }
    }

    public void deletePartnerById(Integer userId) {
        try {
            userDao.deletePartnerById(userId);
        } catch (JdbiException e) {
            throw  new DatabaseException("Failed to delete partner", e);
        }
    }
}
