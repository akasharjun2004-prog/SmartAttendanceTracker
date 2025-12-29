package smartattendance.dao;

import smartattendance.model.MailNotification;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MailNotificationDAO {

    // -----------------------------------------
    // INSERT ONLY REAL MAILS SENT BY THE SYSTEM
    // -----------------------------------------
    public void insertNotification(int userId, String subject, String message, String status) {

        String query = """
            INSERT INTO mail_notifications (user_id, subject, message, sent_time, status)
            VALUES (?, ?, ?, NOW(), ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, userId);
            pst.setString(2, subject);
            pst.setString(3, message);
            pst.setString(4, status);  // "SENT" or "FAILED"

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------
    // FETCH ALL
    // -----------------------------------------
    public List<MailNotification> getAllNotifications() {
        List<MailNotification> list = new ArrayList<>();
        String query = "SELECT * FROM mail_notifications ORDER BY sent_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(new MailNotification(
                        rs.getInt("mail_id"),
                        rs.getInt("user_id"),
                        rs.getString("subject"),
                        rs.getString("message"),
                        rs.getTimestamp("sent_time").toLocalDateTime(),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // -----------------------------------------
    // CLEAR ALL
    // -----------------------------------------
    public void deleteAllNotifications() {
        String query = "DELETE FROM mail_notifications";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
