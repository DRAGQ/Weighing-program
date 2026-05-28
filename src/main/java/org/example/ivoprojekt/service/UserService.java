package org.example.ivoprojekt.service;

import org.example.ivoprojekt.api.mapper.DtoMapper;
import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.api.warning.*;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.domain.UserToken;
import org.example.ivoprojekt.dao.UserDao;
import org.example.ivoprojekt.dao.UserTokenDao;
import org.example.ivoprojekt.repository.UserRepository;
import org.example.ivoprojekt.repository.UserTokenRepository;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    public UserService(UserRepository userRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    public List<DialUserResponse> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        if (users.isEmpty()) {
            throw new NotFoundException("Žiadny použivateľ nebol nájdený");
        }
        return users.stream().map(DtoMapper::toDialUserResponse).toList();
    }

    public User getUserById(Integer id) throws Exception {
        if (id == null) {
            throw new ValidationException("id použivateľa je null");
        }
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Použivateľ nebol nájdený v databáze");
        }
        return user;
    }

    public User getUserByLogin(String login) {
        if (login == null) {
            throw new ValidationException("Prihlasovacie meno je null");
        }
        User user = userRepository.getUserByLogin(login).orElse(null);
        if (user == null) {
            throw new NotFoundException("Použivateľ nebol nájdený v databáze");
        }
        return user;
    }

    public Optional<User> findByLogin(String login) {
        if (login == null) {
            throw new ValidationException("Prihlasovacie meno je null");
        }
        return userRepository.getUserByLogin(login);
    }

    public Integer saveUser(User user) {
        if (user == null) {
            throw new ValidationException("Použivateľ je null");
        }
        Optional<User> checkUserLogin = findByLogin(user.getLogin());
        checkUserLogin.ifPresent(u -> {
            throw new AlreadyExistsException("Prihlasovacie meno: " + u.getLogin() + " už existuje");
        });
        return userRepository.save(user);
    }

    public void updateLoginAndName(DialUserResponse userInformations) {
        if (userInformations.getId() == null || userInformations.getLogin() == null || userInformations.getLogin().isEmpty() || userInformations.getName() == null || userInformations.getName().isEmpty()) {
            throw new ValidationException("Žiadny parameter nemôže byť null alebo prázdny!");
        }
        List<DialUserResponse> allUsers = getAllUsers();
        for (DialUserResponse user : allUsers) {
            if (user.getLogin().equals(userInformations.getLogin()) && !user.getId().equals(userInformations.getId())) {
                throw new AlreadyExistsException("Prihlasovacie meno: " + user.getLogin() + " už existuje");
            }
        }
        userRepository.updateLoginAndName(userInformations);
    }

    public void changePassword(String password, String salt, Integer id) {
        if (password == null || password.isEmpty() || salt == null || salt.isEmpty() || id == null) {
            throw new ValidationException("Heslo a salt nemôže byť null alebo prázdne");
        }
        userRepository.changePassword(password, salt, id);
    }

    public void deleteUser(Integer userId) {
        if (userId == null) {
            throw new ValidationException("Id použivateľa je null");
        }
        try {
            userRepository.delete(userId);
        } catch (DatabaseException e) {
            if (DatabaseTranslationException.foreignKeyException(e)) {
                throw new ForeignKeyException("Použivateľ sa používa v tabuľke váženia");
            }
            throw e;
        }
    }

    public void deletePartnerById(Integer selectedId) {
        if (selectedId == null) {
            throw new ValidationException("Id použivateľa nemôže byť null");
        }
        userRepository.deletePartnerById(selectedId);
    }

    public String findUserTokenByUserId(Integer userId) {
        if (userId == null) {
            throw new ValidationException("Id použivateľa je null");
        }
        return userTokenRepository.findUserTokenByUserId(userId);
    }

    public UserToken getUserToken(String token) {
        if (token == null) {
            throw new ValidationException("Token je null");
        }
        UserToken userToken = userTokenRepository.getUserToken(token);
        if (userToken == null) {
            throw new NotFoundException("Token nebol nájdený");
        }
        return userToken;
    }

    public User findUserByToken(String token) {
        if (token == null) {
            throw new ValidationException("Token je null");
        }
        User user = userTokenRepository.findUserByToken(token);
        if (user == null) {
            throw new NotFoundException("Použivateľ nebol nájdený");
        }
        return user;
    }

    public void saveUserToken(String token, Integer userId) {
        if (token == null || userId == null) {
            throw new ValidationException("Token alebo id je null");
        }
        userTokenRepository.save(token, userId);
    }

    public void deleteUserToken(String token) {
        if (token == null) {
            return;
        }
        getUserToken(token);
        userTokenRepository.delete(token);
    }
}
