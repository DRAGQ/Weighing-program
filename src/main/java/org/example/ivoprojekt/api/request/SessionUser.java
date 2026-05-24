package org.example.ivoprojekt.api.request;

public class SessionUser {
    private Integer id;
    private String login;
    private String name;
    private boolean isAdmin;
    private boolean isProtected;
    private Integer partnerId;

    public SessionUser() {}

    public SessionUser(Integer id, String login, String name, boolean isAdmin, boolean isProtected, Integer partnerId) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.isAdmin = isAdmin;
        this.isProtected = isProtected;
        this.partnerId = partnerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public boolean getIsProtected() {
        return this.isProtected;
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public Integer getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }
}
