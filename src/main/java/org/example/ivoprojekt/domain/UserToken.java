package org.example.ivoprojekt.domain;

public class UserToken {
    Integer id;
    String token;
    Integer userId;

    public UserToken() {}

    public UserToken(Integer id, String token, Integer userId) {
        this.id = id;
        this.token = token;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}
