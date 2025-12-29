package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import smartattendance.model.User;
import smartattendance.service.UserService;
import smartattendance.util.SessionUtil;

public class ProfileSettingsController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private TextField txtRole;
    @FXML private Button btnEdit;
    @FXML private Button btnSave;
    @FXML private Label lblStatus;

    private final UserService userService = new UserService();
    private User currentUser;

    @FXML
    public void initialize() {
        // Load current logged-in user
        currentUser = SessionUtil.getLoggedInUser();
        if (currentUser != null) {
            loadUserDetails();
            setEditable(false);
        }
    }

    private void loadUserDetails() {
        txtFullName.setText(currentUser.getFullName());
        txtEmail.setText(currentUser.getEmail());
        txtPhone.setText(currentUser.getPhone());
        txtAddress.setText(currentUser.getAddress());
        txtRole.setText(currentUser.getRole());
    }

    private void setEditable(boolean editable) {
        txtFullName.setEditable(editable);
        txtEmail.setEditable(editable);
        txtPhone.setEditable(editable);
        txtAddress.setEditable(editable);
        // Role remains non-editable
        btnSave.setDisable(!editable);
    }

    @FXML
    private void handleEditProfile() {
        setEditable(true);
        lblStatus.setText("");
    }

    @FXML
    private void handleSaveProfile() {
        lblStatus.setText("");

        // Update currentUser object
        currentUser.setFullName(txtFullName.getText().trim());
        currentUser.setEmail(txtEmail.getText().trim());
        currentUser.setPhone(txtPhone.getText().trim());
        currentUser.setAddress(txtAddress.getText().trim());

        // Basic validation
        if (currentUser.getFullName().isEmpty() || currentUser.getEmail().isEmpty()) {
            lblStatus.setText("Full Name and Email cannot be empty!");
            return;
        }

        String result = userService.updateUser(currentUser);
        lblStatus.setText(result);

        if ("Updated successfully!".equals(result)) {
            setEditable(false);
        }
    }
}
