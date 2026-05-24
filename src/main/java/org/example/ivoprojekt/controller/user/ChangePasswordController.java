package org.example.ivoprojekt.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.request.SessionUser;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.service.UserService;
import org.example.ivoprojekt.userUtill.UserUtils.PasswordFieldUtils;
import org.example.ivoprojekt.userUtill.security.HashPassword;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;
import java.util.Base64;

public class ChangePasswordController{
    UserService userService;

    @FXML
    private Button changePasswordButton, exitButton;

    @FXML
    private CheckBox actualPasswordCheckBox, confirmationPasswordCheckBox, newPasswordCheckBox;

    @FXML
    private PasswordField actualPasswordField, confirmationPasswordField, newPasswordField;

    @FXML
    private TextField actualPasswordTextField, confirmationPasswordTextField, newPasswordTextField;

    @FXML
    void changePassword(ActionEvent event) throws Exception {
        String actualPassword = actualPasswordTextField.isVisible() ? actualPasswordTextField.getText() : actualPasswordField.getText();
        String newPassword = newPasswordTextField.isVisible() ? newPasswordTextField.getText() : newPasswordField.getText();
        String controlPassword =  confirmationPasswordTextField.isVisible() ? confirmationPasswordTextField.getText() : confirmationPasswordField.getText();

        if (checkCorrectInputs(actualPassword, newPassword, controlPassword)) {
            SessionUser actualUser = UserSessionManager.getActualUser();
            User dbUser = userService.getUserById(actualUser.getId());
            if (dbUser != null) {
                String dbPassword = dbUser.getPassword();
                String inputHashedPassword = HashPassword.hashPassword(actualPassword, Base64.getDecoder().decode(dbUser.getSalt()));
                if (dbPassword.equals(inputHashedPassword)) {
                    byte[] newSalt = HashPassword.generateSalt();
                    String newHashedPassword = HashPassword.hashPassword(newPassword, newSalt);
                    userService.changePassword(newHashedPassword, Base64.getEncoder().encodeToString(newSalt), dbUser.getId());
                    ((Stage) changePasswordButton.getScene().getWindow()).close();
                } else {
                    wrongPassword(actualPasswordField, actualPasswordTextField, newPasswordField, newPasswordTextField, confirmationPasswordField, confirmationPasswordTextField);
                }
            }
        }
    }

    @FXML
    void exitWindow(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    @FXML
    void toggleVisiblePassword(ActionEvent event) {
        if (event.getSource() instanceof CheckBox checkBox) {
            if (checkBox == actualPasswordCheckBox) {
                PasswordFieldUtils.togglePassword(actualPasswordCheckBox, actualPasswordTextField, actualPasswordField);
            } else if (checkBox == newPasswordCheckBox) {
                PasswordFieldUtils.togglePassword(newPasswordCheckBox, newPasswordTextField, newPasswordField);
            } else if (checkBox == confirmationPasswordCheckBox) {
                PasswordFieldUtils.togglePassword(confirmationPasswordCheckBox, confirmationPasswordTextField, confirmationPasswordField);
            }
        }
    }

    private boolean checkCorrectInputs(String actualPassword, String newPassword, String controlPassword) {
        if (actualPassword.isEmpty()) {
            wrongPassword(actualPasswordField, actualPasswordTextField, newPasswordField, newPasswordTextField, confirmationPasswordField, confirmationPasswordTextField);
        }
        else if (newPassword.isEmpty()) {
            wrongPassword(newPasswordField, newPasswordTextField, actualPasswordField, actualPasswordTextField, confirmationPasswordField, confirmationPasswordTextField);
        }
        else if (controlPassword.isEmpty()) {
            wrongPassword(confirmationPasswordField, confirmationPasswordTextField, newPasswordField, newPasswordTextField, actualPasswordField, actualPasswordTextField);
        }
        else if (!newPassword.equals(controlPassword)) {
            wrongPassword(confirmationPasswordField, confirmationPasswordTextField, newPasswordField, newPasswordTextField, actualPasswordField, actualPasswordTextField);
        } else {
            return true;
        }
        return false;
    }

    public void wrongPassword(PasswordField passwordFieldError, TextField textFieldPasswordError, PasswordField passwordField, TextField textFieldPassword, PasswordField passwordField2, TextField textFieldPassword2) {
        textFieldPasswordError.requestFocus();
        passwordFieldError.requestFocus();
        textFieldPasswordError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        passwordFieldError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        passwordField.setStyle("-fx-focus-color: #039ED3;");
        textFieldPassword.setStyle("-fx-focus-color: #039ED3;");
        passwordField2.setStyle("-fx-focus-color: #039ED3;");
        textFieldPassword2.setStyle("-fx-focus-color: #039ED3;");
    }

    public void setServices(UserService userService) {
        this.userService = userService;
    }
}
