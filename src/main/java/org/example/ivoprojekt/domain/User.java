package org.example.ivoprojekt.domain;

import java.util.Base64;

public class User {
    private Integer id;
    private String login;
    private String name;
    private String password;
    private String salt;
    private boolean isActive;
    private boolean isAdmin;
    private boolean isProtected;
    private Integer partnerId;

    public User() {}

    public User(Integer id, String login, String name, String password, String salt, boolean isActive, boolean isAdmin, boolean isProtected, Integer partnerId) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.isActive = isActive;
        this.isAdmin = isAdmin;
        this.isProtected = isProtected;
        this.partnerId = partnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {this.password = password;}

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsProtected() {
        return this.isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public Integer getPartnerId() {return partnerId;}
    
    public void setPartnerId(Integer partnerId) {this.partnerId = partnerId;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", active=" + isActive +
                ", administrator=" + isAdmin +
                ", protected=" + isProtected +
                ", partnerId=" + partnerId +
                '}';
    }
}
