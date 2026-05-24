package org.example.ivoprojekt.controller.dials;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.user.PartnerController;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.PartnerService;
import org.example.ivoprojekt.javaFxUtil.TableUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PartnersController implements Initializable {
    private Stage stage;
    private PartnerService partnerService;
    private TableView<DialPartnerResponse> table;
    private TableColumn<DialPartnerResponse, Integer> id;
    private TableColumn<DialPartnerResponse, String> name;
    private TableColumn<DialPartnerResponse, String> address;
    private Integer inputId;

    @FXML
    DialsTemplateController templateController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.stage = (Stage) templateController.getAddButton().getScene().getWindow());

        templateController.getAddButton().setOnAction(event -> {
            try {
                addPartnerButton();
            } catch (IOException e) {
                WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
            }
        });
        templateController.getUpdateButton().setOnAction(event -> {
            try {
                updatePartnerButton();
            } catch (IOException e) {
                WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
            }
        });
        templateController.getDeleteButton().setOnAction (event -> deletePartnerButton());


        table = (TableView<DialPartnerResponse>) templateController.getTable();
        id = (TableColumn<DialPartnerResponse, Integer>) templateController.getFirstColumn();
        name = (TableColumn<DialPartnerResponse, String>) templateController.getSecondColumn();
        address = (TableColumn<DialPartnerResponse, String>) templateController.getThirdColumn();

        id.setText("ID");
        name.setText("Názov");
        address.setText("Adresa");

        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.inputId = newSelection.getId();
            }
        });

        setTableBehavior();
    }

    private void addPartnerButton() throws IOException {
        PartnerController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Partner.fxml", "Zoznam partnerov");
        controller.setPartnerService(this.partnerService);
        controller.setIsEdit(false);
        controller.setSaveToDatabase(true);
        controller.setOnPartnerSuccess(this::initTable);
    }

    private void updatePartnerButton() throws IOException {
        if (this.inputId != null) {
            PartnerController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Partner.fxml", "Zoznam partnerov");
            controller.setPartnerService(this.partnerService);
            controller.setIsEdit(true);
            controller.setChosenPartner(inputId, null);
            controller.setOnPartnerSuccess(this::initTable);
        }
    }

    private void deletePartnerButton() {
        try {
            partnerService.deletePartner(inputId);
            initTable();
        } catch (DatabaseException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Nepodarilo sa odstrániť partnera", e.getMessage());
        }
    }

    public void initTable() {
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.address.setCellValueFactory(new PropertyValueFactory<>("address"));
        setupTable();
    }

    private void setupTable() {
        this.table.getItems().clear();
        List<DialPartnerResponse> partners = this.partnerService.getAllPartners();

        if (!partners.isEmpty()) {
            this.table.getItems().addAll(partners);
        }
    }

    private void setTableBehavior() {
        TableUtil.setLayout(this.table);
        TableUtil.setTableFocusBehavior(this.table);
        TableUtil.setButtonBindings(this.templateController.getUpdateButton(), this.templateController.getDeleteButton());
    }

    public void setPartnerService(PartnerService service) {
        this.partnerService = service;
    }
}
