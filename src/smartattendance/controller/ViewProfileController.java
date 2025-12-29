package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import smartattendance.model.PermissionRequest;
import smartattendance.model.User;
import smartattendance.service.AttendanceService;
import smartattendance.service.PermissionService;
import smartattendance.service.UserService;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sun.javafx.binding.Logging.ErrorLogger;

public class ViewProfileController {

    @FXML private BorderPane rootPane;
    @FXML private ImageView imgQR;
    @FXML private Label lblName, lblId, lblEmail, lblPhone, lblRole, lblJoinDate;
    @FXML private Label lblPresentDays, lblTotalHours;
    @FXML private TableView<PermissionRequest> tblRequests;
    @FXML private TableColumn<PermissionRequest, String> colType, colReason, colStatus;

    private final UserService userService = new UserService();
    private  PermissionService permissionService ;
    private AttendanceService attendanceService;
  

    @FXML
    private void initialize() {
    	 try {
             this.attendanceService = new AttendanceService();
             this.permissionService = new PermissionService();
          
         } catch (SQLException e) {
             e.printStackTrace();
             showAlert("Error", "Cannot initialize services: " + e.getMessage());
         }
        int userId = smartattendance.util.SessionUtil.getLoggedUserId();
        

        // 1️⃣ Load user info
        User employee = userService.getUserById(userId);
        if(employee != null) {
            lblName.setText("Name: " + SessionUtil.getUsername());
            lblId.setText("Employee ID: " + employee.getUserId());
            lblEmail.setText("Email: " + employee.getEmail());
            lblPhone.setText("Phone: " + employee.getPhone());
            lblRole.setText("Role: " + employee.getRole());
            lblJoinDate.setText("Joined: " + employee.getCreatedAt());
        }
        
        
        

        // 2️⃣ Load pending requests
        List<PermissionRequest> pendingRequests = permissionService.getPendingRequests(userId);
        if(pendingRequests != null) {
            tblRequests.setItems(FXCollections.observableArrayList(pendingRequests));
        }

        // Setup table columns
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPermissionType()));
        colReason.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getReason()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        // 3️⃣ Load attendance summary
        int presentDays = attendanceService.getPresentDaysThisMonth(userId);
        double totalHours = attendanceService.getTotalHoursThisMonth(userId); // decimal hours

        lblPresentDays.setText(String.valueOf(presentDays));
        lblTotalHours.setText(String.format("%.2f hrs", totalHours));

        // 4️⃣ Load QR image from file
        File qrFile = new File(employee.getQrImagePath()); // adjust path
        if(qrFile.exists()) {
            imgQR.setImage(new Image(qrFile.toURI().toString()));
        } else {
            System.out.println("QR Image not found for " +employee.getQrImagePath());
        }
    }
    
    @FXML
    private void handleChangePassword() throws IOException {
       
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/smartattendance/view/change_password.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Change Password");

       
    }


    @FXML
    private void handleBack() {
        SceneUtil.switchScene(rootPane, "/smartattendance/view/qr_action.fxml");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Smart Attendance Tracker");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
