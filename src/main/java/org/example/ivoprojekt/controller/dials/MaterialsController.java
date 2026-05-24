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
import org.example.ivoprojekt.controller.MaterialController;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.javaFxUtil.TableUtil;
import org.example.ivoprojekt.service.MaterialService;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MaterialsController implements Initializable {
    private Stage stage;
    private MaterialService materialService;
    private TableView<Material> table;
    private TableColumn<Material, Integer> id;
    private TableColumn<Material, String> name;
    private Integer inputId;

    @FXML
    DialsTemplateController templateController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.stage = (Stage) this.templateController.getAddButton().getScene().getWindow());

            this.templateController.getAddButton().setOnAction(event -> {
                try {
                    addMaterialButton();
                } catch (IOException e) {
                    WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
                }
            });
            this.templateController.getUpdateButton().setOnAction(event -> {
                try {
                    updateMaterialButton();
                } catch (IOException e) {
                    WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní tlačidiel!", e.getMessage());
                }
            });

        this.templateController.getDeleteButton().setOnAction(event -> deleteMaterialButton());

        this.table = (TableView<Material>) this.templateController.getTable();
        this.id = (TableColumn<Material, Integer>) this.templateController.getFirstColumn();
        this.name = (TableColumn<Material, String>) this.templateController.getSecondColumn();

        this.id.setText("ID");
        this.name.setText("Tovar / Plodina");
        this.templateController.getThirdColumn().setText(null);

        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.inputId = newSelection.getId();
            }
        });

        setTableBehavior();
    }

    private void addMaterialButton() throws IOException {
        MaterialController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Material.fxml", "Údaje o druhu materiálu");
        controller.setMaterialService(this.materialService);
        controller.setIsEdit(false);
        controller.setOnMaterialSuccess(this::initTable);
    }

    private void updateMaterialButton() throws IOException {
        MaterialController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Material.fxml", "Údaje o druhu materiálu");
        controller.setMaterialService(this.materialService);
        controller.setIsEdit(true);
        controller.setChosenMaterial(this.inputId);
        controller.setOnMaterialSuccess(this::initTable);
    }

    private void deleteMaterialButton() {
        try {
            this.materialService.deleteMaterial(this.inputId);
            initTable();
            System.out.println("delete button clicked");
        }  catch (UnableToExecuteStatementException e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Materiál nie je možné vymazať!", "Materiál sa používa v tabulke váženia.");
        } catch (Exception e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Materiál nie je možné vymazať!", "Neočakávaná chyba.");
        }
    }

    public void setMaterialService(MaterialService service) {
        this.materialService = service;
    }

    public void initTable() {
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        setupTable();
    }

    private void setupTable() {
        this.table.getItems().clear();

        List<Material> materials = this.materialService.getAllMaterials();

        if (!materials.isEmpty()) {
            this.table.getItems().addAll(materials);
        }
    }

    private void setTableBehavior() {
        TableUtil.setLayout(this.table);
        TableUtil.setTableFocusBehavior(this.table);
        TableUtil.setButtonBindings(this.templateController.getUpdateButton(), this.templateController.getDeleteButton());
    }
}
