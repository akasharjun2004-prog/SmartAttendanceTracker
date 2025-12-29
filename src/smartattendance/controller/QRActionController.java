package smartattendance.controller;

import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import smartattendance.service.BreakService;
import smartattendance.service.BreakService.ToggleResult;
import smartattendance.service.MailService;
import smartattendance.service.AttendanceService;
import smartattendance.util.SceneUtil;
//import smartattendance.util.SessionUtil;

public class QRActionController {

    @FXML
    private BorderPane rootPane;
    private AttendanceService attendanceService;
   


    @FXML
    private Button btnLogin, btnLogout, btnAskPermission, btnAskLeave, btnBreak, btnViewProfile;

    private BreakService breakService;

    // Initialize the service (controller stays lightweight)
    public void initialize() {
        try {
            this.breakService = new BreakService();
            this.attendanceService = new AttendanceService();  // ✅ FIXED
              
            // Ensure button text initial state (Start Break)
            if (btnBreak != null) btnBreak.setText("Start Break");
        } catch (Exception e) {
            // If service fails to initialize, disable break button to avoid NPEs
            e.printStackTrace();
            if (btnBreak != null) btnBreak.setDisable(true);
        }
    }

    // --- LOGIN ---
    
    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            boolean saved = attendanceService.manualLogin(); // 🔥 service handles DB insert + time
            
            if (saved) {
                MailService.sendManualLoginAlert();            // 🔥 notification to HR
                showAlert("Login Successful",
                        "Manual login recorded and HR notified.");
                btnLogin.setDisable(true);                     // disable button
            } else {
                showAlert("Notice", "You have already logged in today.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to record manual login.");
        }
    }

    // --- LOGOUT ---
    @FXML
    private void handleLogout(ActionEvent event) {
    	

    	    try {
    	        boolean updated = attendanceService.logout();

    	        if (updated) {
    	            showAlert("Logout Successful", "Your logout has been recorded.");
    	        }

    	       // // clear logged user
    	        SceneUtil.switchScene(rootPane, "/smartattendance/view/login.fxml");

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        showAlert("Error", "Unable to logout.");
    	    }
    	}

    

    // --- ASK PERMISSION ---
    @FXML
    private void handleAskPermission(ActionEvent event) {
        SceneUtil.switchScene(rootPane, "/smartattendance/view/ask_permission.fxml");
    }

    // --- ASK LEAVE ---
    @FXML
    private void handleAskLeave(ActionEvent event) {
        SceneUtil.switchScene(rootPane, "/smartattendance/view/ask_leave.fxml");
    }

    // --- BREAK (single button toggles start/end) ---
    @FXML
    private void handleBreak(ActionEvent event) {
        if (breakService == null) {
            showAlert("Error", "Break service unavailable.");
            return;
        }

        try {
            ToggleResult result = breakService.toggleBreak();
            // show message from service
            showAlert(result.success ? "Success" : "Notice", result.message);

            // update button label based on returned state
            if (btnBreak != null) {
                btnBreak.setText(result.onBreak ? "End Break" : "Start Break");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong while toggling break. Check logs.");
        }
    }

    // --- VIEW PROFILE (for employee only) ---
    @FXML
    private void handleViewProfile(ActionEvent event) {
        SceneUtil.switchScene(rootPane, "/smartattendance/view/view_profile.fxml");
    }

    // --- Helper Alert ---
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
