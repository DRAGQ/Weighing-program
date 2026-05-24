package org.example.ivoprojekt.userUtill.UserUtils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordFieldUtils {

    public static void togglePassword(CheckBox checkBox, TextField visibleField, PasswordField hiddenField) {
        if (checkBox.isSelected()) {
            visibleField.setText(hiddenField.getText());
            visibleField.setVisible(true);
            hiddenField.setVisible(false);
        } else {
            hiddenField.setText(visibleField.getText());
            hiddenField.setVisible(true);
            visibleField.setVisible(false);
        }
    }
}
