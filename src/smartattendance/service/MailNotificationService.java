package smartattendance.service;

import smartattendance.dao.MailNotificationDAO;
import smartattendance.model.MailNotification;

import java.util.List;

public class MailNotificationService {

    private static final MailNotificationDAO dao = new MailNotificationDAO();

    // 🔹 Fetch all notifications
    public List<MailNotification> getAllNotifications() {
        return dao.getAllNotifications();
    }

    // 🔹 Delete all notifications
    public void deleteAllNotifications() {
        dao.deleteAllNotifications();
    }

    // 🔹 Record email sent/failed status
    public static void sendWithStatus(int userId, String subject, String message, boolean sent) {
        String status = sent ? "SENT" : "FAILED";
        dao.insertNotification(userId, subject, message, status);
    }
}
