package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
import smartattendance.model.PermissionRequest;
import smartattendance.model.User;
import smartattendance.service.PermissionService;
import smartattendance.service.UserService;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PermissionController {

    @FXML private ComboBox<String> cmbPermissionType;
    @FXML private TextField txtStartTime;
    @FXML private TextField txtEndTime;
    @FXML private TextArea txtReason;
    @FXML private Button btnSubmit;
    @FXML private Button btnCancel;
    @FXML private DatePicker dpPermissionDate;
    @FXML
    private BorderPane rootPane;


    private PermissionService permissionService;
    private UserService userService;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // 24-hour format

    @FXML
    public void initialize() {
    	 try {
    	        permissionService = new PermissionService();
    	        userService = new UserService();
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        showAlert("Error", "Failed to initialize services.");
    	    }

        cmbPermissionType.setItems(FXCollections.observableArrayList(
            "Personal Work",
            "Health Reason",
            "Meeting Outside",
            "Other"
        ));
    }

    @FXML
    private void handleSubmitPermission() {
        String type = cmbPermissionType.getValue();
        LocalDate date = dpPermissionDate.getValue();
        String startTimeStr = txtStartTime.getText().trim();
        String endTimeStr = txtEndTime.getText().trim();
        String reason = txtReason.getText().trim();

        // Validation
        if (type == null || type.isEmpty() || date == null ||
            startTimeStr.isEmpty() || endTimeStr.isEmpty() || reason.isEmpty()) {
            showAlert("Error", "Please fill all fields and select a date.");
            return;
        }

        // Parse times
        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.parse(startTimeStr, timeFormatter);
            endTime = LocalTime.parse(endTimeStr, timeFormatter);
        } catch (DateTimeParseException e) {
            showAlert("Error", "Please enter valid start and end times in HH:mm format.");
            return;
        }

        if (endTime.isBefore(startTime)) {
            showAlert("Error", "End time cannot be before start time.");
            return;
        }

        // Calculate duration
        Duration duration = Duration.between(startTime, endTime);
        String durationStr = String.format("%02d:%02d", duration.toHours(), duration.toMinutesPart());

        // Get logged-in user ID
        int userId = SessionUtil.getLoggedUserId();

        // Fetch employee info
        User employee = userService.getUserById(userId); // get the actual employee
        if (employee == null) {
            showAlert("Error", "User not found. Cannot submit permission.");
            return;
        }

        // Create PermissionRequest object
        PermissionRequest pr = new PermissionRequest();
        pr.setUserId(userId);
        pr.setEmployeeName(employee.getName()); // NEW: set employee name
        pr.setPermissionType(type);
        pr.setDate(date);
        pr.setFromTime(startTimeStr);
        pr.setToTime(endTimeStr);
        pr.setDuration(durationStr);
        pr.setReason(reason);
        pr.setStatus("Pending");

        // Submit via service
        boolean success = permissionService.submitRequest(pr);

        if (success) {
            showAlert("Success", "Permission request submitted successfully.\nDuration: " + durationStr);
            clearFields();
        } else {
            showAlert("Error", "Failed to submit permission request. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        ;
        SceneUtil.switchScene(rootPane, "/smartattendance/view/qr_action.fxml");
     

    }

    private void clearFields() {
        cmbPermissionType.setValue(null);
        dpPermissionDate.setValue(null);
        txtStartTime.clear();
        txtEndTime.clear();
        txtReason.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Smart Attendance Tracker");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
