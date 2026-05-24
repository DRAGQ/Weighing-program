package org.example.ivoprojekt.controller.dials;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.VehicleController;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.javaFxUtil.TableUtil;
import org.example.ivoprojekt.service.VehicleService;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VehiclesController implements Initializable {
    private Stage stage;
    private VehicleService vehicleService;
    private TableView<Vehicle> table;
    private TableColumn<Vehicle, Integer> id;
    private TableColumn<Vehicle, String> identificationNumber;
    private TableColumn<Vehicle, String> description;
    private Integer selectedId;
    private String selectedNumber;
    private String selectedDescription;

    @FXML
    DialsTemplateController templateController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.stage = (Stage) templateController.getAddButton().getScene().getWindow());

        templateController.getAddButton().setOnAction(event -> {
            try {
                addVehicleButton();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        templateController.getUpdateButton().setOnAction(event -> {
            try {
                updateVehicleButton();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        templateController.getDeleteButton().setOnAction(event -> deleteVehicleButton());

        table = (TableView<Vehicle>) templateController.getTable();
        id = (TableColumn<Vehicle, Integer>) templateController.getFirstColumn();
        identificationNumber = (TableColumn<Vehicle, String>) templateController.getSecondColumn();
        description = (TableColumn<Vehicle, String>) templateController.getThirdColumn();

        id.setText("ID");
        identificationNumber.setText("Číslo");
        description.setText("Popis");

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedId = newSelection.getId();
                selectedNumber = newSelection.getIdentificationNumber();
                selectedDescription = newSelection.getDescription();
            }
        });

        setTableBehavior();
    }

    private void addVehicleButton() throws IOException {
        VehicleController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        controller.setVehicleService(this.vehicleService);
        controller.setIsEdit(false);
        controller.setOnVehicleSuccess(this::initTable);
    }

    private void updateVehicleButton() throws IOException {
        VehicleController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        controller.setVehicleService(this.vehicleService);
        controller.setIsEdit(true);

        controller.setChosenVehicle(this.selectedId ,this.selectedNumber, this.selectedDescription);
        controller.setOnVehicleSuccess(this::initTable);
    }

    private void deleteVehicleButton() {
        try {
            vehicleService.deleteVehicle(selectedId);
            initTable();
        } catch (UnableToExecuteStatementException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Vozidlo nie je možné vymazať!", "Vozidlo sa používa v tabulke váženia.");
        } catch (Exception e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Vozidlo nie je možné vymazať!", "Neočakávaná chyba.");
        }
    }

    public void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        identificationNumber.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        setupTable();
    }

    private void setupTable() {
        this.table.getItems().clear();
        List<Vehicle> vehicles = this.vehicleService.getAllVehicles();

        if (!vehicles.isEmpty()) {
            this.table.getItems().addAll(vehicles);
        }
    }

    private void setTableBehavior() {
        TableUtil.setLayout(this.table);
        TableUtil.setTableFocusBehavior(this.table);
        TableUtil.setButtonBindings(this.templateController.getUpdateButton(), this.templateController.getDeleteButton());
    }

    public void setVehicleService(VehicleService service) {
        this.vehicleService = service;
    }
}
