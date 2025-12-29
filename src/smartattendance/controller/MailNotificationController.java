package smartattendance.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smartattendance.model.MailNotification;
import smartattendance.model.User;
import smartattendance.service.MailNotificationService;
import smartattendance.service.MailService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MailNotificationController {

    @FXML private TableView<MailNotification> tblMailNotifications;
    @FXML private TableColumn<MailNotification, String> colRecipient;
    @FXML private TableColumn<MailNotification, String> colSubject;
    @FXML private TableColumn<MailNotification, String> colDate;
    @FXML private TableColumn<MailNotification, String> colStatus;

    private final MailNotificationService service = new MailNotificationService();
    private final ObservableList<MailNotification> notifications = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map table columns
        colRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format date column
        colDate.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue().getSentTime() != null) {
                property.set(cellData.getValue().getSentTime().format(formatter));
            }
            return property;
        });

        loadNotifications();
    }

    private void loadNotifications() {
        List<MailNotification> list = service.getAllNotifications();
        notifications.setAll(list);
        tblMailNotifications.setItems(notifications);
    }

    @FXML
    private void handleRefresh() {
        loadNotifications();
    }

    @FXML
    private void handleViewDetails() {
        MailNotification selected = tblMailNotifications.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mail Details");
            alert.setHeaderText(selected.getSubject());
            alert.setContentText("To: " + selected.getRecipient() + "\n\nMessage:\n" + selected.getMessage());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a notification first!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteAll() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to clear all notifications?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            service.deleteAllNotifications();
            loadNotifications();
        }
    }

    // ---------------------------------------------------
    // Send leave approval email + log
    // ---------------------------------------------------
    public void sendLeaveApproval(User employee, String leaveDetails, boolean approved) {
        MailService.sendLeaveApprovalEmail(employee, leaveDetails, approved);
        loadNotifications();
    }

    // ---------------------------------------------------
    // Send permission approval email + log
    // ---------------------------------------------------
    public void sendPermissionApproval(User employee, String permissionDetails, boolean approved) {
        MailService.sendPermissionApprovalEmail(employee, permissionDetails, approved);
        loadNotifications();
    }

    // ---------------------------------------------------
    // Send manual login alert to HR
    // ---------------------------------------------------
    public void sendManualLoginAlert(User hrUser, User employee) {
        MailService.sendManualLoginAlert();
        loadNotifications();
    }

    // ---------------------------------------------------
    // Send HR attendance alert
    // ---------------------------------------------------
    public void sendHrAttendanceAlert(User hrUser, String employeeName, double hoursWorked) {
        MailService.sendHrAlert(hrUser.getEmail(), employeeName, hoursWorked, hrUser.getUserId());
        loadNotifications();
    }
}
