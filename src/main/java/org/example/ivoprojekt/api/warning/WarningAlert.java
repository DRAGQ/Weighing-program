package org.example.ivoprojekt.api.warning;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WarningAlert {

    public static void warningAlert(AlertType alertType, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType.name());
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
