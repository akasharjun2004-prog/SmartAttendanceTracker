package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import smartattendance.model.User;
import smartattendance.service.ActivityLogService;
import smartattendance.service.LoginService;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

public class LoginController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ImageView eyeOpen;

    @FXML
    private ImageView eyeClosed;

    private final LoginService loginService;
    private  ActivityLogService activityLogService;
    
    public LoginController() {
        this.loginService = new LoginService();
        this.activityLogService = new ActivityLogService();// avoids inline initialization issues
    }

    @FXML
    private void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            eyeOpen.setVisible(true);
            eyeOpen.setManaged(true);
            eyeClosed.setVisible(false);
            eyeClosed.setManaged(false);
        } else {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);

            eyeOpen.setVisible(false);
            eyeOpen.setManaged(false);
            eyeClosed.setVisible(true);
            eyeClosed.setManaged(true);
        }
    }

   
  
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        User user = loginService.login(username, password);

        if (user != null) {

            String role = user.getRole().toLowerCase();
           

            switch (role) {

                case "admin":
                    SceneUtil.switchScene(rootPane, "/smartattendance/view/admin_dashboard.fxml");
                    break;

                case "hr":
                    SceneUtil.switchScene(rootPane, "/smartattendance/view/hr_dashboard.fxml");
                    break;

                case "manager":
                    SceneUtil.switchScene(rootPane, "/smartattendance/view/manager_dashboard.fxml");
                    break;

                case "employee":
                  
                    SceneUtil.switchScene(rootPane, "/smartattendance/view/qr_action.fxml");
                    break;

                default:
                    showAlert("Error", "Unknown role: " + role);
                    break;
            }

        } else {
            showAlert("Login Failed", "Invalid username or password!");
        }
    }


  
    @FXML
    private void handleQRLogin() {
        SceneUtil.switchScene(rootPane, "/smartattendance/view/qr_screen.fxml");
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
