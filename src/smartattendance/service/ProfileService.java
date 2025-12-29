package smartattendance.service;

import smartattendance.dao.UserDAO;
import smartattendance.model.User;

import java.sql.SQLException;

public class ProfileService {

    private final UserDAO userDAO;

    public ProfileService() throws SQLException {
        this.userDAO = new UserDAO();
    }

    /** Get admin/user details by username */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /** Update admin/user details */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    /** Optional: fetch QR code string if you want to display it */
    public String getUserQrCode(String username) {
        User user = userDAO.getUserByUsername(username);
        return user != null ? user.getQrCode() : null;
    }
}
