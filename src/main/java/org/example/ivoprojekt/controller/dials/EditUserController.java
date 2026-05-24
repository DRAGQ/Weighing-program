package org.example.ivoprojekt.controller.dials;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.api.warning.AlreadyExistsException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.service.UserService;

import java.util.List;

public class EditUserController {
    private UserService userService;
    private DialUserResponse userInformations;
    //private List<DialUserResponse> users;
    private Runnable onEditSuccess;

    @FXML
    private CheckBox activeUserCheckBox, adminCheckBox;

    @FXML
    private Button saveButton, exitButton;

    @FXML
    private TextField loginTextField, nameTextField;

    @FXML
    void exitWindow(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    @FXML
    void save(ActionEvent event) {
        if(!checkCorrectInputs()) {
            return;
        }
        try {
            updateUser();
            if (this.onEditSuccess != null) {
                this.onEditSuccess.run();
            }
        } catch (AlreadyExistsException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nie je možné použiť toto prihlasovacie meno!", e.getMessage());
            wrongTextField(this.loginTextField, this.nameTextField);
        } catch (Exception e) {
            System.out.println("something wrong");
        } finally {
            this.exitWindow(null);
        }
    }

    private boolean checkCorrectInputs() {
        if (this.loginTextField.getText().isEmpty()) {
            wrongTextField(this.loginTextField,  this.nameTextField);
        } else if (this.nameTextField.getText().isEmpty()) {
            wrongTextField(this.nameTextField,  this.loginTextField);
        } else if (this.loginTextField.getText().equals(userInformations.getLogin()) && this.nameTextField.getText().equals(userInformations.getName())
        && this.adminCheckBox.isSelected() == userInformations.getIsAdmin() && this.activeUserCheckBox.isSelected() == userInformations.getIsActive()) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Žiadna zmena!", "Prihlasovacie meno a meno sú nezmenené.");
        } else  {
            return true;
        }
        return false;
    }

    /*private boolean checkUsedLoginName() {
        for (DialUserResponse user : this.users) {
            if (user.getLogin().equals(this.loginTextField.getText()) && !user.getLogin().equals(this.userInformations.getLogin())) {
                WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nie je možné použiť toto prihlasovacie meno!", "Prihlasovacie meno je už použité.");
                wrongTextField(this.loginTextField, this.nameTextField);
                return false;
            }
        }
        return true;
    }*/

    private void wrongTextField(TextField textFieldError, TextField nameTextField) {
        textFieldError.requestFocus();
        textFieldError.setStyle("-fx-focus-color:rgba(255,0,0,1);");
        nameTextField.setStyle("-fx-focus-color: #039ED3;");
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private void updateUser() {

        userService.updateLoginAndName(
                new DialUserResponse(userInformations.getId(), this.loginTextField.getText(), this.nameTextField.getText(), this.userInformations.getIsProtected(), this.adminCheckBox.isSelected(), this.activeUserCheckBox.isSelected())
        );
    }

    public void setOnEditSuccess(Runnable onEditSuccess) {
        this.onEditSuccess = onEditSuccess;
    }

    /*public void setAllUsers(List<DialUserResponse> users) {
        this.users = users;
    }*/

    public void setChosenUser(DialUserResponse userInformations) {
        this.userInformations = userInformations;
        this.loginTextField.setText(userInformations.getLogin());
        this.nameTextField.setText(userInformations.getName());
        this.adminCheckBox.setSelected(userInformations.getIsAdmin());
        this.activeUserCheckBox.setSelected(userInformations.getIsActive());
    }
}
