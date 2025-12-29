package smartattendance.controller;

import javafx.application.Platform;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import smartattendance.model.AttendanceRecord;
import smartattendance.model.User;
//import smartattendance.service.ActivityLogService;
import smartattendance.service.AttendanceService;
import smartattendance.service.UserService;
import smartattendance.util.QRUtil;
import smartattendance.util.SceneUtil;
import smartattendance.util.SessionUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class QRScreenController implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private ImageView camView;

    private VideoCapture camera;
    private boolean scanning = true;
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startCameraFeed();
    }

    /** Starts camera feed and continuously scans for QR codes */
    private void startCameraFeed() {
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            showAlert("Camera Error", "Unable to open camera!");
            return;
        }

        Thread cameraThread = new Thread(() -> {
            Mat frame = new Mat();

            while (scanning) {
                if (camera.read(frame)) {
                    Image image = QRUtil.mat2Image(frame);
                    Platform.runLater(() -> camView.setImage(image));

                    String qrText = QRUtil.detectQRCode(image);

                    if (qrText != null && !qrText.isEmpty()) {
                        scanning = false;
                        stopCamera();

                        Platform.runLater(() -> handleQRSuccess(qrText));
                        return;
                    }
                }
            }
        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    /** Stops the camera safely */
    private void stopCamera() {
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }

    /** Handles successful QR scan and redirects based on user role */
    private void handleQRSuccess(String scannedCode) {
        try {
            User user = userService.getUserByQRCode(scannedCode);

            if (user == null) {
                showAlert("Invalid QR", "This QR code is not registered!");
                SceneUtil.switchScene(rootPane, "/smartattendance/view/login.fxml");
                return;
            }

            SessionUtil.setLoggedInUser(user);

            AttendanceRecord todayRecord = null;
            AttendanceService attendanceService = new AttendanceService();

            todayRecord = attendanceService.getTodayAttendance(user.getUserId());

            if (todayRecord == null) {
                attendanceService.markLogin(user.getUserId());
                showAlert("Login recorded", "Welcome! Your login is marked ✅");
                SceneUtil.switchScene(rootPane, "/smartattendance/view/login.fxml");
               

                return; 
            }

            // Already logged in → redirect based on role
            Stage stage = (Stage) rootPane.getScene().getWindow();
            String role = user.getRole().toLowerCase();

            switch (role) {
                case "admin":
                    SceneUtil.switchScene(stage, "/smartattendance/view/admin_dashboard.fxml");
                    break;
                case "hr":
                    SceneUtil.switchScene(stage, "/smartattendance/view/hr_dashboard.fxml");
                    break;
                case "employee":
                    SceneUtil.switchScene(stage, "/smartattendance/view/qr_action.fxml");
                    break;
                default:
                    showAlert("Access Denied", "Unknown role detected. Contact admin.");
                    SceneUtil.switchScene(stage, "/smartattendance/view/login.fxml");
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong while verifying QR.");
        }
    }


    /** Utility method for showing information alerts */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
