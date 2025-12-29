package smartattendance.service;

import smartattendance.dao.UserDAO;
import smartattendance.model.User;
import smartattendance.util.MailTemplate;
import smartattendance.util.MailUtil;
import smartattendance.util.SessionUtil;

public class MailService {

    // Welcome email with QR
    public static void sendWelcomeEmail(User user) {
        String subject = "Welcome to Smart Attendance Tracker";
        String message = MailTemplate.welcomeTemplate(user);
        boolean sent = MailUtil.sendEmailWithStatus(user.getEmail(), subject, message, user.getQrImagePath());
        MailNotificationService.sendWithStatus(user.getUserId(), subject, message, sent);
    }

    // HR alert if employee works less than 8 hours
    public static void sendHrAlert(String hrEmail, String employeeName, double hoursWorked, int hrUserId) {
        String subject = "Attendance Alert: " + employeeName;
        String message = MailTemplate.hrAlertTemplate(employeeName, hoursWorked);
        boolean sent = MailUtil.sendEmailWithStatus(hrEmail, subject, message, null);
        MailNotificationService.sendWithStatus(hrUserId, subject, message, sent);
    }

    // Leave approval/rejection email
    public static void sendLeaveApprovalEmail(User employee, String leaveDetails, boolean approved) {
        String subject = approved ? "Leave Approved" : "Leave Rejected";
        String message = MailTemplate.leaveApprovalTemplate(employee, leaveDetails, approved);
        boolean sent = MailUtil.sendEmailWithStatus(employee.getEmail(), subject, message, null);
        MailNotificationService.sendWithStatus(employee.getUserId(), subject, message, sent);
    }

    // Permission approval/rejection email
    public static void sendPermissionApprovalEmail(User employee, String permissionDetails, boolean approved) {
        String subject = approved ? "Permission Approved" : "Permission Rejected";
        String message = MailTemplate.permissionApprovalTemplate(employee, permissionDetails, approved);
        boolean sent = MailUtil.sendEmailWithStatus(employee.getEmail(), subject, message, null);
        MailNotificationService.sendWithStatus(employee.getUserId(), subject, message, sent);
    }

    // Manual login alert
    public static void sendManualLoginAlert() {
        User employee = SessionUtil.getLoggedInUser();
        if (employee == null) return;

        // Fetch HR user from DB or hardcode for now
        User hrUser = new UserDAO().getUserByUsername("hr_username"); // or get first active HR
        if (hrUser == null) return;

        String subject = "Manual Login Alert - " + employee.getFullName();
        String message = "Hello HR,\n\n" +
                         "Employee **" + employee.getFullName() + "** (" + employee.getEmail() + ") " +
                         "has logged in manually today.\nTime: " + java.time.LocalDateTime.now() +
                         "\n\nSmart Attendance System";

        boolean sent = MailUtil.sendEmailWithStatus(hrUser.getEmail(), subject, message, null);
        MailNotificationService.sendWithStatus(hrUser.getUserId(), subject, message, sent);
    }


}
