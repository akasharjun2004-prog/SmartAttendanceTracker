package smartattendance.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import smartattendance.service.AttendanceService;
import smartattendance.util.SceneUtil;
import javafx.scene.control.Alert;
import java.io.IOException;

public class HRDashboardController {

    @FXML
    private StackPane contentArea;
    @FXML
    private BorderPane mainPane;

    /** ========= Utility Method to Load Screens ========= */
    private void loadScreen(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load " + fxmlPath);
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

  

    @FXML
    private void handleViewAttendance() {
        loadScreen("/smartattendance/view/view_attendance.fxml");
    }

    @FXML
    private void handleLeaveRequests() {
        loadScreen("/smartattendance/view/leave_requests.fxml");
    }

    @FXML
    private void handlePermissionRequests() {
        loadScreen("/smartattendance/view/permission_requests.fxml");
    }

    @FXML
    private void handleAskLeave() {
        loadScreen("/smartattendance/view/ask_leave.fxml");
    }

    @FXML
    private void handleAskPermission() {
        loadScreen("/smartattendance/view/ask_permission.fxml");
    }

    @FXML
    private void handleEmployees() {
        loadScreen("/smartattendance/view/employees.fxml");
    }

    @FXML
    private void handleActivityLog() {
        loadScreen("/smartattendance/view/activity_log.fxml");
    }

    @FXML
    private void handleAdminRequest() {
        loadScreen("/smartattendance/view/admin_request.fxml");
    }
    
    @FXML
    private void handleChangePassword() {
        SceneUtil.loadContent(contentArea, "/smartattendance/view/change_password.fxml");

    }

    @FXML
    private void handleLogin() {
        try {
            AttendanceService attendanceService = new AttendanceService();
            boolean saved = attendanceService.manualLogin();

            if (saved) {
                showAlert("Login Successful", "Login recorded successfully.");
            } else {
                showAlert("Notice", "You already logged in today.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to record login.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Close current HR Dashboard window
            Stage currentStage = (Stage) contentArea.getScene().getWindow();
            currentStage.close();

            // Open Login Screen in full window
            Parent root = FXMLLoader.load(getClass().getResource("/smartattendance/view/login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setMaximized(true);  // FULL SCREEN
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
