package smartattendance.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smartattendance.model.AttendanceRecord;
import smartattendance.model.User;
import smartattendance.service.AttendanceService;
import smartattendance.util.SessionUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AttendanceController {

    @FXML private TableView<AttendanceRecord> attendanceTable;
    @FXML private TableColumn<AttendanceRecord, String> colEmployeeId;
    @FXML private TableColumn<AttendanceRecord, String> colName;
    @FXML private TableColumn<AttendanceRecord, String> colDate;
    @FXML private TableColumn<AttendanceRecord, String> colLoginTime;
    @FXML private TableColumn<AttendanceRecord, String> colLogoutTime;
    @FXML private TableColumn<AttendanceRecord, String> colTotalHours;

    @FXML private DatePicker datePicker;
    @FXML private Label lblStatus;

    private AttendanceService attendanceService;

    @FXML
    public void initialize() throws SQLException {
        attendanceService = new AttendanceService();
        setupColumns();
        loadAttendance(null);
    }

    /** Initialize table columns */
    private void setupColumns() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLoginTime.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        colLogoutTime.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));
        colTotalHours.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
    }

    /** Load attendance (Admin / HR / Manager / Employee) */
    private void loadAttendance(LocalDate date) {

        User currentUser = SessionUtil.getLoggedInUser();
        if (currentUser == null) {
            lblStatus.setText("Error: No active session!");
            System.out.println("⚠ SessionUtil returned null user");
            return;
        }

        List<AttendanceRecord> list = attendanceService.getAttendanceForRole(currentUser, date);

        attendanceTable.getItems().setAll(list);

        if (date == null) {
            lblStatus.setText("Showing all records");
        } else {
            lblStatus.setText("Filtered by date: " + date.toString());
        }
    }

    @FXML
    private void handleFilter() {
        loadAttendance(datePicker.getValue());
    }

    @FXML
    private void handleRefresh() {
        datePicker.setValue(null);
        loadAttendance(null);
    }
}
