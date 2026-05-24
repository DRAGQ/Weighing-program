package org.example.ivoprojekt.api.response;

public class DialUserResponse {
    private Integer id;
    private String login;
    private String name;
    private boolean isProtected;
    private boolean isAdmin;
    private boolean isActive;

    public DialUserResponse(Integer id, String login, String name, boolean isProtected, boolean isAdmin, boolean isActive) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.isProtected = isProtected;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public String getLogin() {
        return this.login;
    }

    public Integer getId() {
        return this.id;
    }

    public boolean getIsProtected() {
        return this.isProtected;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }
    public boolean getIsActive() {
        return this.isActive;
    }

    @Override
    public String toString() {
        return "DialUserResponse{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", isProtected=" + isProtected +
                ", isAdmin=" + isAdmin +
                ", isActive=" + isActive +
                '}';
    }
}
