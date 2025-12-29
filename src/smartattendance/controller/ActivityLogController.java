package smartattendance.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import smartattendance.model.ActivityLog;
import smartattendance.service.ActivityLogService;

public class ActivityLogController {

    @FXML
    private TableView<ActivityLog> tblActivityLog;
    @FXML
    private TableColumn<ActivityLog, String> colTimestamp;
    @FXML
    private TableColumn<ActivityLog, String> colUser;
    @FXML
    private TableColumn<ActivityLog, String> colAction;

    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClearLogs;

    private ActivityLogService service;

    @FXML
    public void initialize() {
       
        service = new ActivityLogService();

   
        colTimestamp.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTimestamp()));
        colUser.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUser()));
        colAction.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getAction()));

        tblActivityLog.setPlaceholder(new Label("No activity logs found."));

        loadLogs();
    }

    private void loadLogs() {
        ObservableList<ActivityLog> list =
                FXCollections.observableArrayList(service.getAllLogs());
        tblActivityLog.setItems(list);
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        loadLogs();
    }

    @FXML
    private void handleClearLogs() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to clear all logs? This action cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Log Deletion");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean success = service.clearAllLogs();
            Alert info = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "All logs cleared successfully!" : "Failed to clear logs. Try again!",
                    ButtonType.OK);
            info.showAndWait();
            if (success) loadLogs();
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadLogs();
        } else {
            ObservableList<ActivityLog> list =
                    FXCollections.observableArrayList(service.searchLogs(keyword));
            tblActivityLog.setItems(list);
        }
    }
}
