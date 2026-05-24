package org.example.ivoprojekt.controller.weighing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.controller.MaterialController;
import org.example.ivoprojekt.controller.user.PartnerController;
import org.example.ivoprojekt.controller.VehicleController;
import org.example.ivoprojekt.controller.utill.ActionHandler;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.MaterialService;
import org.example.ivoprojekt.service.PartnerService;
import org.example.ivoprojekt.service.VehicleService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AcceptanceController implements Initializable {
    private Stage stage;
    private PartnerService partnerService;
    private VehicleService vehicleService;
    private MaterialService materialService;
    private double tara;


    @FXML
    private WeighingTemplateController templateController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            this.stage = (Stage) templateController.getAddMaterialButton().getScene().getWindow();
        });

        templateController.setLabelTypeOfPartner("Dodávateľ");
        templateController.switchBoxes();
        templateController.setActualDatePicker();
        templateController.setLabelEntryVehicle("Vstup naloženého vozidla");
        templateController.setLabelExtradictionVehicle("Výstup prázdneho vozidla");

        setButtonsListening();

        templateController.getChoiceBoxVehicle().setOnAction(actionEvent -> {
            templateController.setTextFieldTara();
            this.tara = Double.parseDouble(templateController.getTextFieldTara().getText());
            setWeightNet(0);
        });


        templateController.getTextFieldWeightGross().setOnKeyReleased(keyEvent -> {
            try {
                String weighingGross = templateController.getTextFieldWeightGross().getText();
                if (weighingGross.isEmpty()) {
                    setWeightNet(0);
                }
                setWeightNet(Double.parseDouble(weighingGross));
                //budem musiet nejako vyriesit to ked dam do textfieldu dlhe cislo tak mi tam da pismena
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }

        });
    }

    private void setWeightNet(double weighingGross) {
        templateController.setTextFieldWeightNet(weighingGross, this.tara);
    }

    private void setButtonsListening() {
        setButtonAction(templateController.getAddPartnerButton(), this::addPartnerButton);
        setButtonAction(templateController.getAddVehicleButton(), this::addVehicleButton);
        setButtonAction(templateController.getAddMaterialButton(), this::addMaterialButton);
    }

    private void setButtonAction(Button button, ActionHandler handler) {
        button.setOnAction(event -> {
            try {
                handler.handle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void addPartnerButton() throws IOException {
        PartnerController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Partner.fxml", "Zoznam partnerov");
        controller.setPartnerService(this.partnerService);
        controller.setIsEdit(false);
        controller.setSaveToDatabase(true);
        controller.setOnPartnerSuccess(this::init);
    }

    private void addVehicleButton() throws IOException {
        VehicleController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        controller.setVehicleService(this.vehicleService);
        controller.setIsEdit(false);
        controller.setOnVehicleSuccess(this::init);
    }

    private void addMaterialButton() throws IOException {
        MaterialController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Material.fxml", "Údaje o druhu materiálu");
        controller.setMaterialService(this.materialService);
        controller.setIsEdit(false);
        controller.setOnMaterialSuccess(this::init);
    }


    public void init() {
        clearChoiceBoxes();
        getAllPartners();
        getAllVehicles();
        getAllMaterials();
    }

    public void setServices(PartnerService partnerService, VehicleService vehicleService, MaterialService materialService) {
        this.partnerService = partnerService;
        this.vehicleService  = vehicleService;
        this.materialService = materialService;
    }

    private void clearChoiceBoxes() {
        templateController.clearChoiceBoxTypeOfPartner();
        templateController.clearChoiceBoxVehicle();
        templateController.clearChoiceBoxMaterial();
    }

    private void getAllPartners() {
        List<DialPartnerResponse> partners = partnerService.getAllPartners();
        templateController.setChoiceBoxTypeOfPartner(partners);
    }

    private void getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        templateController.setChoiceBoxVehicle(vehicles);
    }

    private void getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        templateController.setChoiceBoxMaterial(materials);
    }
}
