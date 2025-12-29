package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import smartattendance.service.LeaveRequestService;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

import java.time.LocalDate;

public class LeaveController {

    @FXML private ComboBox<String> cmbType;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TextArea txtReason;

    private final LeaveRequestService leaveService = new LeaveRequestService();

    @FXML
    private void initialize() {
        // Populate leave types
        if (cmbType != null) {
            cmbType.getItems().addAll(
                "Sick Leave",
                "Casual Leave",
                "Emergency Leave",
                "Other"
            );
        }
    }

    @FXML
    private void handleSubmitLeave() {
        // Collect inputs
        String type = cmbType.getValue();
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();
        String reason = txtReason.getText().trim();
        int userId = SessionUtil.getLoggedUserId();

        // Validation
        if (type == null || start == null || end == null || reason.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }
        if (end.isBefore(start)) {
            showAlert("Error", "End date cannot be before start date.");
            return;
        }

        // Call service to submit leave
        boolean success = leaveService.submitLeaveRequest(type, start, end, reason, userId);

        if (success) {
            showAlert("Success", "Leave request submitted successfully.");
            // Navigate back to QRAction or main screen
            SceneUtil.switchScene(dpStart, "/smartattendance/view/qraction.fxml");
        } else {
            showAlert("Error", "A leave request already exists for these dates.");
        }
    }

    @FXML
    private void handleCancel() {
        SceneUtil.switchScene(dpStart, "/smartattendance/view/qraction.fxml");
    }

    // Helper method to show alerts
    private void showAlert(String header, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Smart Attendance Tracker");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
