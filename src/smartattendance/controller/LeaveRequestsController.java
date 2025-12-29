package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smartattendance.model.LeaveRequest;
import smartattendance.service.LeaveRequestService;

public class LeaveRequestsController {

    @FXML private TableView<LeaveRequest> tblLeaveRequests;
    @FXML private TableColumn<LeaveRequest, String> colEmployeeName;
    @FXML private TableColumn<LeaveRequest, String> colFromDate;
    @FXML private TableColumn<LeaveRequest, String> colToDate;
    @FXML private TableColumn<LeaveRequest, String> colReason;
    @FXML private TableColumn<LeaveRequest, String> colStatus;

    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnRefresh;

    private final ObservableList<LeaveRequest> leaveData = FXCollections.observableArrayList();
    private final LeaveRequestService leaveService = new LeaveRequestService();

    @FXML
    public void initialize() {
        // Table column bindings
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colFromDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colToDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data from DB
        loadLeaveRequests();
    }

    private void loadLeaveRequests() {
        leaveData.clear();
        leaveData.addAll(leaveService.getAllLeaveRequests());
        tblLeaveRequests.setItems(leaveData);
    }

    @FXML
    private void handleApprove() {
        LeaveRequest selected = tblLeaveRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a leave request to approve.");
            return;
        }

        boolean success = leaveService.approveLeave(selected.getLeaveId());
        if (success) {
            selected.setStatus("Approved");
            tblLeaveRequests.refresh();
            showAlert("Success", "Leave request approved successfully.");
        } else {
            showAlert("Error", "Failed to approve leave request.");
        }
    }

    @FXML
    private void handleReject() {
        LeaveRequest selected = tblLeaveRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a leave request to reject.");
            return;
        }

        boolean success = leaveService.rejectLeave(selected.getLeaveId());
        if (success) {
            selected.setStatus("Rejected");
            tblLeaveRequests.refresh();
            showAlert("Success", "Leave request rejected successfully.");
        } else {
            showAlert("Error", "Failed to reject leave request.");
        }
    }

    @FXML
    private void handleRefresh() {
        loadLeaveRequests();
        showAlert("Refreshed", "Leave requests reloaded.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Smart Attendance Tracker");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
