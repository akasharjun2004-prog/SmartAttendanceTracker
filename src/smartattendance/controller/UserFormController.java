package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import smartattendance.model.User;
import smartattendance.service.UserService;
import smartattendance.util.SessionUtil;

public class UserFormController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextField txtPassword;    // auto-generated, non-editable
    @FXML private ComboBox<String> cbRole;
    @FXML private ComboBox<User> cbManager;
    @FXML private TextField txtQrCode;
    @FXML private Label lblStatus;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {

        // Role list
        cbRole.setItems(FXCollections.observableArrayList(
                "Admin", "HR", "Manager", "Employee"
        ));

        txtQrCode.setText("AUTO"); // Placeholder
        txtPassword.setText("AUTO"); // Placeholder
        txtPassword.setEditable(false); // cannot edit manually

        loadManagers();
    }

    private void loadManagers() {
        try {
            cbManager.setItems(
                    FXCollections.observableArrayList(userService.getAllManagers())
            );

            cbManager.setConverter(new javafx.util.StringConverter<User>() {
                @Override
                public String toString(User user) {
                    return user != null ? user.getFullName() : "";
                }

                @Override
                public User fromString(String s) { return null; }
            });

        } catch (Exception e) {
            lblStatus.setText("Failed to load managers.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        lblStatus.setText("");

        try {
            User user = new User();
            user.setFullName(txtName.getText());
            user.setEmail(txtEmail.getText());
            user.setPhone(txtPhone.getText());
            user.setAddress(txtAddress.getText());
            user.setRole(cbRole.getValue());

            if (cbManager.getValue() != null)
                user.setManagerId(cbManager.getValue().getUserId());

            // 🔹 Get current logged-in user ID to set createdBy
            User currentUser = SessionUtil.getLoggedInUser();
            Integer createdBy = currentUser != null ? currentUser.getUserId() : null;

            // 🔹 Create user (handles username, password, QR, email)
            boolean success = userService.createUser(user, createdBy);

            if (success) {
                lblStatus.setStyle("-fx-text-fill:green;");
                lblStatus.setText("User saved successfully!");
                closeWindow();
            } else {
                lblStatus.setText("Failed to save user.");
            }

        } catch (Exception e) {
            lblStatus.setText("Error occurred while saving.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
