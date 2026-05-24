package org.example.ivoprojekt.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.mapper.DtoMapper;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.service.UserService;
import org.example.ivoprojekt.userUtill.UserUtils.PasswordFieldUtils;
import org.example.ivoprojekt.userUtill.security.HashPassword;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Optional;

public class LoginController {
    private UserService userService;
    private Runnable onRegisterSuccess;

    @FXML
    private Button loginButton, exitButton;

    @FXML
    private CheckBox passwordCheckBox, stayLogInCheckBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameTextField, passwordTextField;


    @FXML
    public void toggleVisiblePassword(ActionEvent event) {
        PasswordFieldUtils.togglePassword(this.passwordCheckBox, this.passwordTextField, this.passwordField);
    }

    @FXML
    public void loginUser(ActionEvent event) throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String inputPassword = this.passwordTextField.isVisible() ? this.passwordTextField.getText() : passwordField.getText();
        if (!checkInputs(inputPassword)) {
            return;
        }
        try {
            User user = this.userService.getUserByLogin(this.nameTextField.getText());
            byte[] salt = Base64.getDecoder().decode(user.getSalt());
            String hashedInputPassword = HashPassword.hashPassword(inputPassword, salt);

            if (!hashedInputPassword.equals(user.getPassword())) {
                wrongPassword();
                return;
            }

            if (!user.getIsActive()) {
                WarningAlert.warningAlert(Alert.AlertType.INFORMATION, "Použivateľ nie je viac aktívny.", "Pre aktiváciu musí byť jeho stav zmenený iným administrátorom.");
                return;
            }
            setupUser(user);
            exitWindow(null);

        } catch (ValidationException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba počas prihlásenia použivateľa!", e.getMessage());
        } catch (NotFoundException e) {
            wrongLogin();
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Použivateľ nebol nájdený!", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nastala neočakávaná chyba počas prihlásenia použivateľa",e.getMessage());
        }

    }

    @FXML
    public void exitWindow(ActionEvent event) {
        ((Stage) this.exitButton.getScene().getWindow()).close();
    }

    public void setupUser(User user) {
        UserSessionManager.setActualUser(DtoMapper.toSessionUser(user));
        if (this.stayLogInCheckBox.isSelected()) {
            String token = UserSessionManager.generateUserToken();
            UserSessionManager.saveUserToken(token);
            this.userService.saveUserToken(token, user.getId());
        }
        this.onRegisterSuccess.run();
    }

    public boolean checkInputs(String inputPassword) {
        if (this.nameTextField.getText().isEmpty()) {
            wrongLogin();
        } else if (inputPassword.isEmpty()) {
            wrongPassword();
        } else {
            return true;
        }
        return false;
    }

    public void setUserService(UserService service) {
        this.userService = service;
    }

    private void wrongLogin() {
        this.nameTextField.requestFocus();
        this.nameTextField.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        this.passwordTextField.setStyle("-fx-focus-color: #039ED3;");
        this.passwordField.setStyle("-fx-focus-color: #039ED3;");
    }

    private void wrongPassword() {
        this.passwordTextField.requestFocus();
        this.passwordField.requestFocus();
        this.passwordTextField.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        this.passwordField.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        this.nameTextField.setStyle("-fx-focus-color: #039ED3;");
    }

    public void setOnLoginSuccess(Runnable onRegisterSuccess) {
        this.onRegisterSuccess = onRegisterSuccess;
    }
}
