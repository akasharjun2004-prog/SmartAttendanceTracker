package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import smartattendance.model.User;
import smartattendance.service.UserService;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

public class ChangePasswordController {

    @FXML
    private PasswordField txtOldPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnChangePassword;

    @FXML
    private Button btnCancel;
    @FXML
    private StackPane contentArea; 

    private final UserService userService = new UserService();

    @FXML
    private void handleChangePassword(ActionEvent event) {

        String oldPass = txtOldPassword.getText().trim();
        String newPass = txtNewPassword.getText().trim();
        String confirmPass = txtConfirmPassword.getText().trim();

        // Get logged-in user from session
        User loggedUser = SessionUtil.getLoggedInUser();
        if (loggedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Session Expired", "Please login again.");
            return;
        }

        // Delegate everything to service
        String result = userService.changePassword(loggedUser, oldPass, newPass, confirmPass);

        if ("SUCCESS".equals(result)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.WARNING, "Error", result);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Password change cancelled.");
    }

    private void clearFields() {
        txtOldPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
    }
    @FXML
    private void handleBack(ActionEvent event)
    {
    	
    	 SceneUtil.switchScene(contentArea, "/smartattendance/view/login.fxml");

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
