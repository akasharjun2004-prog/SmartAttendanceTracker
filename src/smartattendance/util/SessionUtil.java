package smartattendance.util;

import smartattendance.model.User;

public class SessionUtil {

    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static int getLoggedUserId() {
        return (loggedInUser != null) ? loggedInUser.getUserId() : -1;
    }

    public static String getUsername() {
        return (loggedInUser != null) ? loggedInUser.getUsername() : null;
    }

    public static String getUserRole() {
        return (loggedInUser != null) ? loggedInUser.getRole() : null;
    }

    public static void clearSession() {
        loggedInUser = null;
    }

    public static void logout() {
        clearSession();
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}
