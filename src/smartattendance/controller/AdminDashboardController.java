package smartattendance.controller;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import smartattendance.service.AttendanceService;
import smartattendance.util.SceneUtil;


public class AdminDashboardController {
	private AttendanceService attendanceService;

    @FXML
    private StackPane contentArea; 
    
    
    
    //@FXML
   // private StackPane contentArea; // already in your FXML

    @FXML
    private void handleChangePassword() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/change_password.fxml");

    }

    

    @FXML
    public void initialize() {
        
        SceneUtil.loadContent(contentArea, "/smartattendance/view/admin_dashboard_home.fxml");
        try {
            attendanceService = new AttendanceService();
        } catch (Exception e) {
            e.printStackTrace();
        }

       
    }
    

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            boolean saved = attendanceService.manualLogin();

            if (saved) {
                showAlert("Login Successful", "Manual login recorded and HR notified.");
            } else {
                showAlert("Notice", "You have already logged in today.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to record manual login.");
        }
    }


    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            boolean updated = attendanceService.logout();

            if (updated) {
                showAlert("Logout Successful", "Your logout has been recorded.");
            }

            
            SceneUtil.switchScene(contentArea, "/smartattendance/view/login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to logout.");
        }
    }


    
    @FXML
    private void handleUserManagement() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/user_management.fxml");
    }

    @FXML
    private void handleAttendance() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/view_attendance.fxml");
    }

    @FXML
    private void handleLeaveRequests() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/leave_requests.fxml");
    }

    @FXML
    private void handlePermissionRequests() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/permission_requests.fxml");
    }

    @FXML
    private void handleActivityLog() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/activity_log.fxml");
    }

    @FXML
    private void handleMailNotifications() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/mail_notification.fxml");
    }

    @FXML
    private void handleProfileSettings() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/profile_settings.fxml");
    }
    
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
