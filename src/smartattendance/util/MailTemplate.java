package smartattendance.util;

import smartattendance.model.User;
import smartattendance.service.UserService;

public class MailTemplate {

    public static String welcomeTemplate(User user) {
        return "Hello " + user.getFullName() + ",\n\n" +
               "Welcome to Smart Attendance Tracker!\n\n" +
               "Your login credentials are:\n" +
               "Username: " + user.getUsername() + "\n" +
               "Password: " + UserService.plainPassword + "\n\n" +
               "Your personal QR code is attached with this email.\n" +
               "Please change your password after first login.\n\n" +
               "Regards,\nSmart Attendance Team";
    }

    public static String hrAlertTemplate(String employeeName, double hoursWorked) {
        return "Hello HR/Admin,\n\n" +
               "Employee " + employeeName + " has worked only " + hoursWorked + " hours today.\n" +
               "Please review and take necessary action.\n\n" +
               "Regards,\nSmart Attendance System";
    }

    public static String leaveApprovalTemplate(User employee, String leaveDetails, boolean approved) {
        String status = approved ? "approved" : "rejected";
        return "Hello " + employee.getFullName() + ",\n\n" +
               "Your leave request has been " + status + ".\n\n" +
               "Details:\n" + leaveDetails + "\n\n" +
               "Regards,\nSmart Attendance Team";
    }

    public static String permissionApprovalTemplate(User employee, String permissionDetails, boolean approved) {
        String status = approved ? "approved" : "rejected";
        return "Hello " + employee.getFullName() + ",\n\n" +
               "Your permission request has been " + status + ".\n\n" +
               "Details:\n" + permissionDetails + "\n\n" +
               "Regards,\nSmart Attendance Team";
    }
}
