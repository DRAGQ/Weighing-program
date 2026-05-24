package org.example.ivoprojekt.controller.weighing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.ivoprojekt.api.request.WeighingOverviewRequest;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.api.response.DialUserResponse;
import org.example.ivoprojekt.api.response.WeighingTableOverviewResponse;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.weighing.PDF.TableOverview;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PrintOverviewController {
    private PartnerService partnerService;
    private VehicleService vehicleService;
    private UserService userService;
    private MaterialService materialService;
    private WeighingService weighingService;

    @FXML
    private CheckBox checkBoxDescription;

    @FXML
    private CheckBox checkBoxIssued;

    @FXML
    private CheckBox checkBoxMaterial;

    @FXML
    private CheckBox checkBoxPartner;

    @FXML
    private CheckBox checkBoxTypeOfWeighing;

    @FXML
    private CheckBox checkBoxVehicle;

    @FXML
    private ChoiceBox<DialUserResponse> choiceBoxIssued;

    @FXML
    private ChoiceBox<Material> choiceBoxMaterial;

    @FXML
    private ChoiceBox<DialPartnerResponse> choiceBoxPartner;

    @FXML
    private ChoiceBox<String> choiceBoxTypeOfWeighing;

    @FXML
    private ChoiceBox<Vehicle> choiceBoxVehicle;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private TextArea descriptionText;

    @FXML
    private Button exitButton, printButton;

    @FXML
    private LocalTimeSpinner timeStart, timeEnd;

    @FXML
    void toggleCheckBox(ActionEvent event) {
        CheckBox toggledCheckBox = (CheckBox) event.getSource();
        ChoiceBox<?> chosenChoiceBox = null;

        if (toggledCheckBox.equals(checkBoxTypeOfWeighing)) {
            chosenChoiceBox = choiceBoxTypeOfWeighing;
        } else if (toggledCheckBox.equals(checkBoxPartner)) {
            chosenChoiceBox = choiceBoxPartner;
        } else if (toggledCheckBox.equals(checkBoxVehicle)) {
            chosenChoiceBox = choiceBoxVehicle;
        } else if (toggledCheckBox.equals(checkBoxIssued)) {
            chosenChoiceBox = choiceBoxIssued;
        } else if (toggledCheckBox.equals(checkBoxMaterial)) {
            chosenChoiceBox = choiceBoxMaterial;
        }

        if (chosenChoiceBox != null) {
            choiceBoxGeneric(chosenChoiceBox, toggledCheckBox);
        } else {
            descriptionText.setDisable(!toggledCheckBox.isSelected());
        }
    }

    @FXML
    void showPDF(ActionEvent event) {
        //zistit co sa stane ked je date null
        //Local Date a Time nemozem vymazat, ked nastavim na 0 tak sa to automaticky nastavy na nejaku najnizsiu hodnotu
        //napr na rok 2000.01.01
        //mohol by som aj dat aby datum a cas nemohli byt zaporne
        ArrayList<String> filters = new ArrayList<>();
        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();
        LocalTime localTimeStart = timeStart.getValue();
        LocalTime localTimeEnd = timeEnd.getValue();

        String typeOfWeighing = genericSelectedValues(choiceBoxTypeOfWeighing.getValue() ,checkBoxTypeOfWeighing);
        DialPartnerResponse dialPartnerResponse = genericSelectedValues(choiceBoxPartner.getValue(), checkBoxPartner);
        Vehicle vehicle = genericSelectedValues(choiceBoxVehicle.getValue(), checkBoxVehicle);
        DialUserResponse dialUserResponse = genericSelectedValues(choiceBoxIssued.getValue(), checkBoxIssued);
        Material material = genericSelectedValues(choiceBoxMaterial.getValue(), checkBoxMaterial);
        String text = genericSelectedValues(descriptionText.getText(), checkBoxDescription);



        Integer type = null;
        if (typeOfWeighing != null) {
            type = typeOfWeighing.equals("Príjem") ? 1 : 0;
            filters.add("Typ pohybu: " + typeOfWeighing);
        }
        String partner = null;
        if (dialPartnerResponse != null) {
            partner = dialPartnerResponse.getName();
            filters.add("Partner: " + dialPartnerResponse.getName());
        }
        String vehicleNumber = null;
        if (vehicle != null) {
            vehicleNumber = vehicle.getIdentificationNumber();
            filters.add("Vozidlo: " + vehicleNumber);
        }

        String issued = null;
        if (dialUserResponse != null) {
            issued = dialUserResponse.getName();
            filters.add("Vystavil: " + issued);
        }

        String  materialName = null;
        if (material != null) {
            materialName = material.getName();
            filters.add("Tovar: " + materialName);
        }

        if (text != null && !text.isEmpty()) {
            filters.add("Poznámka obsahuje: " + text);
        }

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(localTimeStart);
        System.out.println(localTimeEnd);
        System.out.println("typeOfWeighing: " + typeOfWeighing);
        System.out.println("typeOfWeighing: " + dialPartnerResponse);
        System.out.println("typeOfWeighing: " + vehicle);
        System.out.println("typeOfWeighing: " + dialUserResponse);
        System.out.println("typeOfWeighing: " + material);
        System.out.println("typeOfWeighing: " + text);

        //ArrayList<String> filters = new ArrayList<>();
//        addNoNullValue(typeOfWeighing, filters);
//        addNoNullValue(dialPartnerResponse.getName(), filters);
//        addNoNullValue(vehicle.getIdentificationNumber(), filters);
//        addNoNullValue(dialUserResponse.getName(), filters);
//        addNoNullValue(material.getName(), filters);
//        addNoNullValue(text, filters);

        List<WeighingTableOverviewResponse> rows = getAllRows(startDate, endDate, localTimeStart, localTimeEnd, type,
                partner, vehicleNumber, issued,  materialName);

        if (!rows.isEmpty()) {
            String timePeriod = startDate +  " - " + endDate;

            TableOverview table = new TableOverview(rows, timePeriod, filters);
            //table.addRowsToTable(rows);
        } else {
            WarningAlert.warningAlert(Alert.AlertType.INFORMATION, "Žiadne váženie nebolo nájdené!", "Týmto podmienkam nevyhovuje žiadne váženie.");
            System.out.println("rows are null");
        }
    }

    /*private void addNoNullValue(typeOfWeighing, dialPartnerResponse, vehicle, dialUserResponse, material, text) {
        ArrayList<String> filters = new ArrayList<>();

        addNoNullValue(typeOfWeighing, filters);
        addNoNullValue(dialPartnerResponse.getName(), filters);
        addNoNullValue(vehicle.getIdentificationNumber(), filters);
        addNoNullValue(dialUserResponse.getName(), filters);
        addNoNullValue(material.getName(), filters);
        addNoNullValue(text, filters);
    }*/

    /*private void addNoNullValue(String value, ArrayList<String> filters) {
        if (value != null) {
            filters.add(value);
        }
    }*/

    private <T> T genericSelectedValues(T value, CheckBox checkBox) {
        if (checkBox.isSelected()) {
            return value;
        }
        return null;
    }

    @FXML
    void exitWindow(ActionEvent event) {

    }

    //DESCRIPTION MAX 200 ZNAKOV

    public void setServices(PartnerService partnerService, VehicleService vehicleService, UserService userService, MaterialService materialService, WeighingService weighingService) {
        this.partnerService = partnerService;
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.materialService = materialService;
        this.weighingService = weighingService;
    }

    public void init() {
        clearChoiceBoxes();
        getAllPartners();
        getAllVehicles();
        getAllMaterials();
        getAllTypeOfWeighing();
        getAllIssued();

        disableAllComponents();

        /*
        TOTO JE DOBRA METODA11111111111111111111111111111111111111111111111111111111111111111111111111111111111111
        timeStart.setTime(LocalTime.parse("00:00"));
        timeEnd.setTime(LocalTime.parse("23:59"));
         */
        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.now());
    }

    private void clearChoiceBoxes() {
        this.choiceBoxPartner.getItems().clear();
        this.choiceBoxVehicle.getItems().clear();
        this.choiceBoxMaterial.getItems().clear();
        this.choiceBoxTypeOfWeighing.getItems().clear();
        this.choiceBoxIssued.getItems().clear();
    }

    private void disableAllComponents() {
        this.choiceBoxPartner.setDisable(true);
        this.choiceBoxVehicle.setDisable(true);
        this.choiceBoxMaterial.setDisable(true);
        this.choiceBoxTypeOfWeighing.setDisable(true);
        this.choiceBoxIssued.setDisable(true);
        this.descriptionText.setDisable(true);
    }

    private <T> void choiceBoxGeneric(ChoiceBox<T>  choiceBox, CheckBox checkBox) {
        choiceBox.setDisable(!checkBox.isSelected());
    }

    private void getAllPartners() {
        List<DialPartnerResponse> partners = partnerService.getAllPartners();
        choiceBoxSetter(choiceBoxPartner, partners, DialPartnerResponse::getName);
    }

    private void getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        choiceBoxSetter(choiceBoxVehicle, vehicles, Vehicle::getIdentificationNumber);
    }

    private void getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        choiceBoxSetter(choiceBoxMaterial, materials, Material::getName);
    }

    private void getAllTypeOfWeighing() {
        choiceBoxTypeOfWeighing.getItems().add("Príjem");
        choiceBoxTypeOfWeighing.getItems().add("Výdaj");
    }

    private void getAllIssued() {
        List<DialUserResponse> users = userService.getAllUsers();
        choiceBoxSetter(choiceBoxIssued, users, DialUserResponse::getName);
    }

   private List<WeighingTableOverviewResponse> getAllRows(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, Integer type, String partnerName,
                                                          String vehicleName, String userName, String materialName) {
       WeighingOverviewRequest request = new WeighingOverviewRequest(dateStart, dateEnd, timeStart, timeEnd, type, partnerName, vehicleName, userName, materialName);
        return this.weighingService.getWeighingTableRows(request);
    }

    private <T> void choiceBoxSetter(ChoiceBox<T> choiceBox, List<T> items, Function<T, String> displayFunction) {
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T object) {
                return object != null ? displayFunction.apply(object) : "";
            }

            @Override
            public T fromString(String s) {
                return null;
            }
        });
        choiceBox.getItems().addAll(items);
    }
}
