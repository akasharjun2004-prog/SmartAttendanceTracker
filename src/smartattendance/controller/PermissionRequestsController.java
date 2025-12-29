package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smartattendance.model.PermissionRequest;
import smartattendance.service.PermissionService;
import smartattendance.util.SessionUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PermissionRequestsController implements Initializable {

    @FXML
    private TableView<PermissionRequest> tblPermissionRequests;

    @FXML
    private TableColumn<PermissionRequest, String> colEmployeeName;  // matches FXML


    @FXML
    private TableColumn<PermissionRequest, LocalDate> colDate;

    @FXML
    private TableColumn<PermissionRequest, String> colReason;

    @FXML
    private TableColumn<PermissionRequest, String> colStatus;

    @FXML
  //  private TableColumn<PermissionRequest, String> colType;

    private final ObservableList<PermissionRequest> permissionList = FXCollections.observableArrayList();
    private final PermissionService permissionService = new PermissionService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName")); 
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
       // colType.setCellValueFactory(new PropertyValueFactory<>("permissionType"));

        loadPermissionRequests();
    }

    /** Load all requests from DB */
    private void loadPermissionRequests() {
        List<PermissionRequest> requests = permissionService.getAllRequests();
        permissionList.setAll(requests);
        tblPermissionRequests.setItems(permissionList);
    }

    @FXML
    private void handleApprove() {
        PermissionRequest selected = tblPermissionRequests.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int adminId = SessionUtil.getLoggedUserId();
            permissionService.approveRequest(selected.getId(), adminId); // service handles logic
            loadPermissionRequests();
        }
    }

    @FXML
    private void handleReject() {
        PermissionRequest selected = tblPermissionRequests.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int adminId = SessionUtil.getLoggedUserId();
            permissionService.rejectRequest(selected.getId(), adminId); // service handles logic
            loadPermissionRequests();
        }
    }

    @FXML
    private void handleViewDetails() {
        PermissionRequest selected = tblPermissionRequests.getSelectionModel().getSelectedItem();
        if (selected != null) {
            PermissionRequest request = permissionService.getRequestById(selected.getId());
            // UI code to display details (e.g., new dialog)
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Smart Attendance Tracker");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
