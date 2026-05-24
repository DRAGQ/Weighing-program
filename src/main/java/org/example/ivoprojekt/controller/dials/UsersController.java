package org.example.ivoprojekt.controller.dials;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.api.warning.ForeignKeyException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.user.RegisterController;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.PartnerService;
import org.example.ivoprojekt.service.UserService;
import org.example.ivoprojekt.javaFxUtil.TableUtil;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//musim urobit response triedu kde dam tieto hodnoty

public class UsersController implements Initializable {
    private Stage stage;
    private UserService userService;
    private PartnerService partnerService;
    private TableView<DialUserResponse> table;
    private TableColumn<DialUserResponse, Integer> id;
    private TableColumn<DialUserResponse, String> login;
    private TableColumn<DialUserResponse, String> name;
    private List<DialUserResponse> users;
    private Button changePassword;
    private Integer selectedId;
    private String selectedLogin;
    private String selectedName;
    private boolean isAdmin;
    private boolean isActive;

    private DialUserResponse selectedUserInformations;

    private Logger logger;

    @FXML
    DialsTemplateController templateController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.logger = LoggerFactory.getLogger(UsersController.class);

        Platform.runLater(() -> this.stage = (Stage) table.getScene().getWindow());

        templateController.getAddButton().setOnAction(event -> {
            try {
                addUserButton();
            } catch (IOException e) {
                WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
            }
        });
        templateController.getUpdateButton().setOnAction(event -> {
            try {
                updateUserButton();
            } catch (IOException e) {
                WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
            }
        });
        templateController.getDeleteButton().setOnAction(event -> deleteUserButton());

        addChangePasswordButton();
        changePassword.setOnAction(event -> {
            System.out.println("Change password button pressed");
        });

        table = (TableView<DialUserResponse>) templateController.getTable();
        id = (TableColumn<DialUserResponse, Integer>) templateController.getFirstColumn();
        login = (TableColumn<DialUserResponse, String>) templateController.getSecondColumn();
        name = (TableColumn<DialUserResponse, String>) templateController.getThirdColumn();

        id.setText("ID");
        login.setText("Login");
        name.setText("Meno");

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            boolean isAdmin = UserSessionManager.getActualUser().getIsAdmin();

            boolean changePasswordPermission = newSelection == null || !(isAdmin && !newSelection.getIsProtected());
            System.out.println("is admin loged in: " + changePasswordPermission);
            setButtonsAvailability(changePassword, !isAdmin, !isAdmin ? 0.4 : 1.0);
            setButtonsAvailability(templateController.getUpdateButton(), changePasswordPermission, changePasswordPermission ? 0.4 : 1.0);
            setButtonsAvailability(templateController.getDeleteButton(), changePasswordPermission, changePasswordPermission ? 0.4 : 1.0);

            if (newSelection != null) {
                this.selectedId = newSelection.getId();
                this.selectedLogin = newSelection.getLogin();
                this.selectedName = newSelection.getName();
                this.isAdmin = newSelection.getIsAdmin();
                this.isActive = newSelection.getIsActive();
                this.selectedUserInformations = newSelection;
            }
        });
        setTableBehavior();
    }

    private void addChangePasswordButton() {
        changePassword = new Button("Zmeniť heslo");
        templateController.getFlowPane().getChildren().add(changePassword);
    }

    private void setButtonsAvailability(Button button, boolean disabled, double value) {
        button.setDisable(disabled);
        button.setOpacity(value);
    }

    private void addUserButton() throws IOException {
        RegisterController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Register.fxml", "Registrácia");
        controller.setServices(userService, partnerService);
        controller.disableLogCheckBox();
        controller.setOnRegisterSuccess(this::initTable);
    }

    private void updateUserButton() throws IOException {
        EditUserController editController = NewWindow.openNewWindow(this.stage, "/fxml/user/Edit.fxml", "Úprava");
        editController.setUserService(userService);
        //TREBA SPRAVIT LOGIKU ACTIVE USER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! CIZE PRI PRIHLASENI TO SKONTROLOVAT
        //ADMIN SI MOZE ZMENIT HESLO SAM ALE NIKTO INY MU HO NEMOZE ZMENIT LEBO JE PROTECTED. TEORETICKY MOZE ADMIN NIEKOHO INEHO DAT PROTECTED
        editController.setChosenUser(this.selectedUserInformations);
        editController.setOnEditSuccess(this::initTable);
    }

    private void deleteUserButton() {
        try {
            //SQLITE_CONSTRAINT_UNIQUE (2067) by malo byt pri inserte
            //OSETRIT ESTE DELETE PARTNERA

            userService.deletePartnerById(selectedId);
            userService.deleteUser(selectedId);
            initTable();
        }  catch (ValidationException | ForeignKeyException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Použivateľa nie je možné vymazať!", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Použivateľa nie je možné vymazať!", "Neočakávaná chyba.");
        }
    }


    public void initTable() {
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.login.setCellValueFactory(new PropertyValueFactory<>("login"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupTable();
    }

    private void setupTable() {
        this.table.getItems().clear();
        this.users = this.userService.getAllUsers();

        if (!this.users.isEmpty()) {
            this.table.getItems().addAll(this.users);
        }
    }

    private void setTableBehavior() {
        TableUtil.setLayout(this.table);
        TableUtil.setTableFocusBehavior(this.table);
        setButtonsAvailability(templateController.getUpdateButton(), true, 0.4);
        setButtonsAvailability(templateController.getDeleteButton(), true, 0.4);
        setButtonsAvailability(changePassword, true, 0.4);
    }

    public void setUserService(UserService service, PartnerService partnerService) {
        this.userService = service;
        this.partnerService = partnerService;
    }
}
