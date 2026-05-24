package org.example.ivoprojekt.userUtill.session;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.example.ivoprojekt.api.request.SessionUser;
import org.example.ivoprojekt.domain.User;

import java.util.UUID;
import java.util.prefs.Preferences;

public class UserSessionManager {
    private static Preferences prefs = Preferences.userRoot().node(UserSessionManager.class.getName());;
    private static final String KEY = "userToken";
    private static SessionUser actualUser;
    private static BooleanProperty isloggedIn =  new SimpleBooleanProperty(false);
    //private static Boolean isAdmin;


    public static void saveUserToken(String token) {
        prefs.put(KEY, token);
    }

    public static String loadUserToken() {
        return prefs.get(KEY, null);
    }

    public static String generateUserToken() {
        return UUID.randomUUID().toString();
    }

    public static void setActualUser(SessionUser sessionUser) {
        actualUser = sessionUser;
        isloggedIn.set(true);
        //isAdmin = sessionUser.isAdmin();
    }

    public static SessionUser getActualUser() {
        return actualUser;
    }

    public static BooleanProperty getIsLoggedIn() {
        return isloggedIn;
    }

    //public static void setIsAdmin(boolean value) {
      //  isAdmin = value;
    //}

    //public static Boolean getIsAdmin() {
        //return isAdmin;
    //}

    public static void clearUserSession() {
        actualUser = null;
        prefs.remove(KEY);
        isloggedIn.set(false);
    }
}
