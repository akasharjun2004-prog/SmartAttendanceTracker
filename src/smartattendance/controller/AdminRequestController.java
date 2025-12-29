package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import smartattendance.model.AdminRequest;

import java.time.LocalDate;

public class AdminRequestController {

   
    @FXML private TabPane tabPaneRequest;

    @FXML private ChoiceBox<String> cbLeaveType;
    @FXML private DatePicker dpFromDate;
    @FXML private DatePicker dpToDate;
    @FXML private TextField txtLeaveReason;
    @FXML private Button btnSubmitLeave;

    
    @FXML private DatePicker dpPermissionDate;
    @FXML private TextField txtFromTime;
    @FXML private TextField txtToTime;
    @FXML private TextField txtPermissionReason;
    @FXML private Button btnSubmitPermission;

    @FXML private TextField txtGeneralSubject;
    @FXML private TextArea txtGeneralMessage;
    @FXML private Button btnSubmitGeneral;

    @FXML private TableView<AdminRequest> tblPreviousRequests;
    @FXML private TableColumn<AdminRequest, Integer> colRequestId;
    @FXML private TableColumn<AdminRequest, String> colRequestType;
    @FXML private TableColumn<AdminRequest, LocalDate> colRequestDate;
    @FXML private TableColumn<AdminRequest, String> colRequestDetails;
    @FXML private TableColumn<AdminRequest, String> colRequestStatus;

    private ObservableList<AdminRequest> requestList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
       
        cbLeaveType.getItems().addAll("Casual Leave", "Sick Leave", "Earned Leave", "Other");

        
        colRequestId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRequestType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRequestDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colRequestDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
        colRequestStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        
        loadPreviousRequests();
    }

    @FXML
    private void handleSubmitLeave() {
        String leaveType = cbLeaveType.getValue();
        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        String reason = txtLeaveReason.getText();

        if (leaveType == null || fromDate == null || toDate == null || reason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all fields for Leave Request");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Leave Request Submitted Successfully!");
        clearLeaveForm();
        loadPreviousRequests();
    }

    @FXML
    private void handleSubmitPermission() {
        LocalDate date = dpPermissionDate.getValue();
        String fromTime = txtFromTime.getText();
        String toTime = txtToTime.getText();
        String reason = txtPermissionReason.getText();

        if (date == null || fromTime.isEmpty() || toTime.isEmpty() || reason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all fields for Permission Request");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Permission Request Submitted Successfully!");
        clearPermissionForm();
        loadPreviousRequests();
    }

    @FXML
    private void handleSubmitGeneral() {
        String subject = txtGeneralSubject.getText();
        String message = txtGeneralMessage.getText();

        if (subject.isEmpty() || message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all fields for General Request");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "General Request Submitted Successfully!");
        clearGeneralForm();
        loadPreviousRequests();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Admin Request");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearLeaveForm() {
        cbLeaveType.setValue(null);
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        txtLeaveReason.clear();
    }

    private void clearPermissionForm() {
        dpPermissionDate.setValue(null);
        txtFromTime.clear();
        txtToTime.clear();
        txtPermissionReason.clear();
    }

    private void clearGeneralForm() {
        txtGeneralSubject.clear();
        txtGeneralMessage.clear();
    }

    private void loadPreviousRequests() {
      
        requestList.clear();

       
        requestList.add(new AdminRequest(1, "Leave", LocalDate.now().minusDays(2), "Sick Leave", "Approved"));
        requestList.add(new AdminRequest(2, "Permission", LocalDate.now().minusDays(1), "Permission for 2 hours", "Pending"));

        tblPreviousRequests.setItems(requestList);
    }
}

