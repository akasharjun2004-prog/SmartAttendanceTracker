package smartattendance.util;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import smartattendance.model.User;

public class SceneUtil {
	
    /** Switch entire scene using a Pane (when controller has rootPane reference) */
    public static void switchScene(Pane pane, String fxmlPath) {
        try {
            Stage stage = (Stage) pane.getScene().getWindow();
            Parent root = FXMLLoader.load(SceneUtil.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            applyFadeTransition(root);
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.switchScene(Pane)", e);
            e.printStackTrace();
        }
    }

    /** Switch entire scene using ActionEvent (like button click) */
    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            applyFadeTransition(root);
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.switchScene(ActionEvent)", e);
            e.printStackTrace();
        }
    }

    /** Switch scene using any Node (flexible for buttons, labels, etc.) */
    public static void switchScene(Node source, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            applyFadeTransition(root);
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.switchScene(Node)", e);
            e.printStackTrace();
        }
    }

    /** Load an FXML and return its Parent (for embedding) */
    public static Parent loadFXML(String fxmlPath) {
        try {
            return FXMLLoader.load(SceneUtil.class.getResource(fxmlPath));
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.loadFXML", e);
            e.printStackTrace();
            return null;
        }
    }

    /** Load an FXML into a target pane (used in dashboards for center content) and return its controller */
    public static <T> T loadContent(Pane targetPane, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(fxmlPath));
            Parent content = loader.load();
            targetPane.getChildren().clear();
            targetPane.getChildren().add(content);
            applyFadeTransition(content);
            return loader.getController(); // <-- Return the controller
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.loadContent", e);
            e.printStackTrace();
            return null;
        }
    }

    /** Switch entire stage directly */
    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(SceneUtil.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            applyFadeTransition(root);
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.switchScene(Stage)", e);
            e.printStackTrace();
        }
    }

    /** ✅ Auto-switch to correct dashboard based on logged-in user's role */
    public static void switchToDashboard(Stage stage) {
        try {
            User currentUser = SessionUtil.getLoggedInUser();

            if (currentUser == null) {
                switchScene(stage, "/smartattendance/view/login.fxml");
                return;
            }

            String role = currentUser.getRole().toLowerCase();

            if (role.equals("admin")) {
                switchScene(stage, "/smartattendance/view/admin_dashboard.fxml");
            } else if (role.equals("hr")) {
                switchScene(stage, "/smartattendance/view/hr_dashboard.fxml");
            } else {
                switchScene(stage, "/smartattendance/view/login.fxml");
            }
        } catch (Exception e) {
            ErrorLogUtil.logError("SceneUtil.switchToDashboard", e);
            e.printStackTrace();
        }
    }

    /** Apply smooth fade animation */
    private static void applyFadeTransition(Parent root) {
        FadeTransition fade = new FadeTransition(Duration.millis(400), root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
}
