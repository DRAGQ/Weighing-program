package org.example.ivoprojekt.repository;

import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.dao.UserTokenDao;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.domain.UserToken;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;

public class UserTokenRepository {
    private final UserTokenDao userTokenDao;

    public UserTokenRepository(Jdbi jdbi) {
        this.userTokenDao = jdbi.onDemand(UserTokenDao.class);
    }

    public String findUserTokenByUserId(Integer userId) {
        try {
            return userTokenDao.findUserTokenByUserId(userId);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch user token by user id", e);
        }
    }

    public UserToken getUserToken(String token) {
        try {
            return userTokenDao.findByToken(token);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch user token", e);
        }
    }


    public User findUserByToken(String token) {
        try {
            return userTokenDao.findUserByToken(token);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch user by token", e);
        }
    }

    public void save(String token, Integer userId) {
        try {
            userTokenDao.save(token, userId);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save user token", e);
        }
    }

    public void delete(String token) {
        try {
            userTokenDao.deleteToken(token);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to delete user token", e);
        }
    }
}
