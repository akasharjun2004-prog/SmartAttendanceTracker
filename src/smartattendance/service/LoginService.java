package smartattendance.service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import smartattendance.dao.UserDAO;
import smartattendance.model.User;
import smartattendance.util.DBConnection;
import smartattendance.util.PasswordUtil;
import smartattendance.util.SessionUtil;

public class LoginService {

    private final UserDAO userDAO;
    private ActivityLogService activityLogService;

    public LoginService() {
        UserDAO tempDAO;
        try {
            tempDAO = new UserDAO();
            activityLogService=new ActivityLogService();// DB connection handled in DAO
        } catch (Exception e) {
            e.printStackTrace();
            tempDAO = null;
        }
        this.userDAO = tempDAO;
    }

    /**
     * Normal login (username + password)
     */
    public User login(String username, String plainPassword) {
        if (userDAO == null) return null;

        // Fetch user from DB
        User user = userDAO.getUserByUsername(username);
        if (user == null) return null;

        // Check hashed password
        boolean valid = PasswordUtil.verifyPassword(plainPassword, user.getPassword());
        if (!valid) return null;

        // Set session
        SessionUtil.setLoggedInUser(user);

        return user;
    }
    private boolean autoRecordLogin(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                INSERT INTO attendance (user_id, login_time, attendance_date)
                VALUES (?, CURRENT_TIMESTAMP(0), CURRENT_DATE)
                ON DUPLICATE KEY UPDATE login_time = CURRENT_TIMESTAMP(0)
            """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                activityLogService.log(SessionUtil.getLoggedUserId(), "LOGIN", " login", "LOCAL");

                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * ✔ QR Login: fetch user using QR Code (username stored in QR)
     */
    public User loginWithQR(String userIdFromQR) {
        if (userDAO == null) return null;

        // QR stores format "USER:<userId>"
        String qrValue = "USER:" + userIdFromQR;

        User user = userDAO.getUserByQRCode(qrValue);
        if (user == null) return null;

        SessionUtil.setLoggedInUser(user);
        autoRecordLogin(user.getUserId()); // record login in attendance

        return user;
    }

}
