package org.example.ivoprojekt.controller.user;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.mapper.DtoMapper;
import org.example.ivoprojekt.api.warning.AlreadyExistsException;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.domain.Partner;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.PartnerService;
import org.example.ivoprojekt.service.UserService;
import org.example.ivoprojekt.userUtill.UserUtils.PasswordFieldUtils;
import org.example.ivoprojekt.userUtill.security.HashPassword;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RegisterController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    private Stage stage;
    private UserService userService;
    private PartnerService partnerService;
    private Partner partner;
    private Runnable onRegisterSuccess;

    @FXML
    private Button partnerWindowButton, registerButton, exitButton;

    @FXML
    private CheckBox passwordCheckBox, confirmationCheckBox, stayedLoggedInCheckBox;

    @FXML
    private PasswordField confirmationPasswordField, passwordField;

    @FXML
    private TextField confirmationPasswordTextField, passwordTextField, loginNameTextField, nameTextField;

    @FXML
    public void toggleVisiblePassword(ActionEvent event) {
        if (event.getSource() == passwordCheckBox) {
            PasswordFieldUtils.togglePassword(passwordCheckBox, passwordTextField, passwordField);
        } else if (event.getSource() == confirmationCheckBox) {
            PasswordFieldUtils.togglePassword(confirmationCheckBox, confirmationPasswordTextField, confirmationPasswordField);
        }
    }

    @FXML
    public void openAdditionalInfo(ActionEvent event) throws IOException {
        PartnerController partnerController = NewWindow.openNewWindow(this.stage, "/fxml/user/Partner.fxml", "Údaje o firme");
        partnerController.setPartnerService(this.partnerService);
        partnerController.disableCheckBoxes();

        if (this.partner != null) {
            partnerController.setChosenPartner(null, this.partner);
        }

        partnerController.setOnCreateCallback(newPartner -> this.partner = newPartner);
    }

    @FXML
    public void registerUser() {
        String inputPassword = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();
        String inputConfirmPassword = confirmationPasswordField.isVisible() ? confirmationPasswordField.getText() : confirmationPasswordTextField.getText();
        Integer partnerId = null;
        boolean success = false;
        Logger logger = LoggerFactory.getLogger(this.getClass());

        if (!checkCorrectInputs(inputPassword, inputConfirmPassword) || !comparePasswords(inputPassword, inputConfirmPassword)) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Chyba v inpute!", "Jeden z inputov je prázdny alebo nesplňuje dané podmienky.");
            return;
        }
        try {
            partnerId = partnerService.savePartner(this.partner);

            byte[] salt = HashPassword.generateSalt();
            String hashedPassword = HashPassword.hashPassword(inputPassword, salt);

            User user = new User(null, loginNameTextField.getText(), nameTextField.getText(), hashedPassword, Base64.getEncoder().encodeToString(salt), true, false, false, partnerId);
            Integer userId = userService.saveUser(user);
            success = true;
            user.setId(userId);

            setupUser(user);
            exitWindow(null);
            //this.stage.close();

        } catch (ValidationException | NotFoundException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba počas registrácie použivateľa!", e.getMessage());
        } catch (AlreadyExistsException e) {
            wrongLoginName(loginNameTextField, nameTextField);
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Prihlasovacie meno už existuje!", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nastala neočakávaná chyba počas registrácie použivateľa",e.getMessage());
        } finally {
            if (!success) {
                rollbackPartner(partnerId);
            }
        }
    }

    @FXML
    public void exitWindow(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.stage = (Stage) registerButton.getScene().getWindow());
    }

    private void rollbackPartner(Integer partnerId) {
        if (partnerId != null) {
            partnerService.deletePartner(partnerId);
        }
    }

    private void setupUser(User user) {
        UserSessionManager.setActualUser(DtoMapper.toSessionUser(user));
        if (stayedLoggedInCheckBox.isSelected()) {
            String token = UserSessionManager.generateUserToken();
            UserSessionManager.saveUserToken(token);
            userService.saveUserToken(token, UserSessionManager.getActualUser().getId());
        }
        if (this.onRegisterSuccess != null) {
            this.onRegisterSuccess.run();
        }
    }

    public void disableLogCheckBox() {
        this.stayedLoggedInCheckBox.setDisable(true);
    }

    private boolean checkCorrectInputs(String inputPassword, String inputConfirmPassword) {
        if (loginNameTextField.getText().isEmpty()) {
            wrongLoginName(loginNameTextField, nameTextField);
        } else if (nameTextField.getText().isEmpty()) {
            wrongLoginName(nameTextField, loginNameTextField);
        } else if (inputPassword.isEmpty()) {
            wrongPassword(passwordField, passwordTextField, confirmationPasswordField, confirmationPasswordTextField);
        } else if (inputConfirmPassword.isEmpty()) {
            wrongPassword(confirmationPasswordField, confirmationPasswordTextField, passwordField, passwordTextField);
        } else if (this.partner == null) {
            this.partnerWindowButton.requestFocus();
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Chýbajú dodatočné informácie o firme!", "Treba vyplniť dodatočné informácie");
        } else {
            return true;
        }
        return false;
    }

    private boolean comparePasswords(String inputPassword, String inputConfirmPassword) {
        if(!inputPassword.equals(inputConfirmPassword)) {
            wrongPassword(confirmationPasswordField, confirmationPasswordTextField, passwordField, passwordTextField);
            return  false;
        }
        return true;
    }

    public void wrongLoginName(TextField textFieldError, TextField nameTextField) {
        textFieldError.requestFocus();
        textFieldError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        nameTextField.setStyle("-fx-focus-color: #039ED3;");
        this.passwordTextField.setStyle("-fx-focus-color: #039ED3;");
        this.passwordField.setStyle("-fx-focus-color: #039ED3;");
        this.confirmationPasswordTextField.setStyle("-fx-focus-color: #039ED3;");
        this.confirmationPasswordField.setStyle("-fx-focus-color: #039ED3;");
    }

    public void wrongPassword(PasswordField passwordFieldError, TextField textFieldPasswordError, PasswordField passwordField, TextField textFieldPassword) {
        textFieldPasswordError.requestFocus();
        passwordFieldError.requestFocus();
        textFieldPasswordError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        passwordFieldError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        passwordField.setStyle("-fx-focus-color: #039ED3;");
        textFieldPassword.setStyle("-fx-focus-color: #039ED3;");
        this.loginNameTextField.setStyle("-fx-focus-color: #039ED3;");
        this.nameTextField.setStyle("-fx-focus-color: #039ED3;");
    }

    public void setServices(UserService service,  PartnerService partnerService) {
        this.userService = service;
        this.partnerService = partnerService;
    }

    public void setOnRegisterSuccess(Runnable onRegisterSuccess) {
        this.onRegisterSuccess = onRegisterSuccess;
    }
}
