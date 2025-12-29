package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import smartattendance.model.User;
import smartattendance.service.UserService;

public class UserManagementController {

    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colAddress;

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private ComboBox<String> cmbRole;

    @FXML private Button btnEdit;
    @FXML private Button btnUpdate;
    @FXML private Label lblStatus;

    private final UserService userService = new UserService();
    private User selectedUser;

    @FXML
    public void initialize() {

        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        colRole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole()));
        colAddress.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));

        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Manager", "Employee"));

        loadUsers();

        tblUsers.setOnMouseClicked(event -> {
            selectedUser = tblUsers.getSelectionModel().getSelectedItem();
            if (selectedUser != null) btnEdit.setDisable(false);
        });
    }

    private void loadUsers() {
        ObservableList<User> list = FXCollections.observableArrayList(
                userService.getAllUsers()
        );
        tblUsers.setItems(list);
    }

    @FXML
    private void handleEditUser() {
        if (selectedUser == null) return;

        txtFullName.setText(selectedUser.getFullName());
        txtEmail.setText(selectedUser.getEmail());
        txtPhone.setText(selectedUser.getPhone());
        txtAddress.setText(selectedUser.getAddress());
        cmbRole.setValue(selectedUser.getRole());
    }

    @FXML
    private void handleUpdateUser() {
        if (selectedUser == null) return;

        selectedUser.setFullName(txtFullName.getText());
        selectedUser.setEmail(txtEmail.getText());
        selectedUser.setPhone(txtPhone.getText());
        selectedUser.setAddress(txtAddress.getText());
        selectedUser.setRole(cmbRole.getValue());

        String result = userService.updateUser(selectedUser);

        lblStatus.setText(result);
        loadUsers();
    }

    @FXML
    private void handleDeleteUser() {
        if (selectedUser == null) return;

        String result = userService.deleteUser(selectedUser.getUserId());

        lblStatus.setText(result);
        loadUsers();
    }

    @FXML
    private void handleAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartattendance/view/user_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add User");
            stage.show();

        } catch (Exception e) {
        	 e.printStackTrace(); 
            lblStatus.setText("Failed to open Add User form!");
        }
    }
}
