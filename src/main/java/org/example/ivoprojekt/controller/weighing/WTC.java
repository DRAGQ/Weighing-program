package org.example.ivoprojekt.controller.weighing;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.api.response.WeighingTableResponse;
import org.example.ivoprojekt.api.response.WeighingUpdateResponse;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.MaterialController;
import org.example.ivoprojekt.controller.user.PartnerController;
import org.example.ivoprojekt.controller.VehicleController;
import org.example.ivoprojekt.controller.utill.WeighingActionType;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.domain.Weighing;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.service.MaterialService;
import org.example.ivoprojekt.service.PartnerService;
import org.example.ivoprojekt.service.VehicleService;
import org.example.ivoprojekt.service.WeighingService;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class WTC implements Initializable {
    private Stage stage;
    private PartnerService partnerService;
    private VehicleService vehicleService;
    private MaterialService materialService;
    private WeighingService weighingService;
    private boolean isSupplier;
    private boolean isUpdate;
    private int updateNumberWeighing;
    private PartnerController createdPartnerController;
    private VehicleController createdVehicleController;
    private MaterialController createdMaterialController;
    private WeighingTableResponse weighingTableResponse;
    private Runnable onSaveRefreshTable;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelTypeOfPartner, labelEntryVehicle, labelExtradictionVehicle;

    @FXML
    private HBox boxTara, boxGrossWeighing;

    @FXML
    private ChoiceBox<DialPartnerResponse> choiceBoxTypeOfPartner;

    @FXML
    private ChoiceBox<Vehicle> choiceBoxVehicle;

    @FXML
    private ChoiceBox<Material> choiceBoxMaterial;

    @FXML
    private Button addTaraButton, setEntryTimeButton, addWeightGrossButton, setDepartureTimeButton, saveWeighingButton, closeButton;

    @FXML
    private TextField textFieldTara, textFieldWeightGross, textFieldWeightNet;

    @FXML
    private LocalTimeSpinner vehicleEntryTime, vehicleDepartureTime;

    @FXML
    private TextArea textAreaNote;

    @FXML
    private CheckBox checkBoxPrintWeighingTicket;

    @FXML
    void closeWindow(ActionEvent event) {
        this.stage.close();
    }

    @FXML
    public void addPartner(ActionEvent event) throws IOException {
        this.createdPartnerController = NewWindow.openNewWindow(this.stage, "/fxml/user/Partner.fxml", "Zoznam partnerov");
        this.createdPartnerController.setPartnerService(this.partnerService);
        this.createdPartnerController.setIsEdit(false);
        this.createdPartnerController.setSaveToDatabase(true);
        this.createdPartnerController.setOnPartnerSuccess(this::initPreviousValues);

    }

    private void initPreviousValues() {
        DialPartnerResponse temporaryPartner = this.choiceBoxTypeOfPartner.getValue();
        Vehicle temporaryVehicle = this.choiceBoxVehicle.getValue();
        Material temporaryMaterial =  this.choiceBoxMaterial.getValue();

        if (this.createdPartnerController != null) {
            String partnerName = this.createdPartnerController.getChosenPartnerName();
            temporaryPartner = new DialPartnerResponse(null, partnerName, null);
            this.createdPartnerController = null;
        } else if (this.createdVehicleController != null) {
            temporaryVehicle = this.createdVehicleController.getVehicleForChoiceBox();
            this.createdVehicleController = null;
        } else if (this.createdMaterialController != null) {
            temporaryMaterial = new Material(null, this.createdMaterialController.getMaterialName(), null, null);
            this.createdMaterialController = null;
        }

        setTempForChoiceBoxUpdate(this.choiceBoxTypeOfPartner, temporaryPartner);
        setTempForChoiceBoxUpdate(this.choiceBoxVehicle, temporaryVehicle);
        setTempForChoiceBoxUpdate(this.choiceBoxMaterial, temporaryMaterial);

    }

    @FXML
    public void addVehicle(ActionEvent event) throws IOException {
        this.createdVehicleController = NewWindow.openNewWindow(this.stage, "/fxml/user/Vehicle.fxml", "Údaje o vozidle");
        this.createdVehicleController.setVehicleService(this.vehicleService);
        this.createdVehicleController.setIsEdit(false);
        this.createdVehicleController.setOnVehicleSuccess(this::initPreviousValues);
    }

    @FXML
    public void addMaterial(ActionEvent event) throws IOException {
        this.createdMaterialController = NewWindow.openNewWindow(this.stage, "/fxml/user/Material.fxml", "Údaje o druhu materiálu");
        this.createdMaterialController.setMaterialService(this.materialService);
        this.createdMaterialController.setIsEdit(false);
        this.createdMaterialController.setOnMaterialSuccess(this::initPreviousValues);
    }

    @FXML
    public void setCurrentTime(ActionEvent event) throws IOException {
        if (event.getSource() == this.setEntryTimeButton) {
            this.vehicleEntryTime.getValueFactory().setValue(LocalTime.now());
        } else if (event.getSource() == this.setDepartureTimeButton) {
            this.vehicleDepartureTime.getValueFactory().setValue(LocalTime.now());
        }
    }

    public void switchBoxes() {
        HBox taraParent = (HBox) this.boxTara.getParent();
        HBox grossParent = (HBox) this.boxGrossWeighing.getParent();

        taraParent.getChildren().remove(this.boxTara);
        grossParent.getChildren().remove(this.boxGrossWeighing);

        taraParent.getChildren().add(1,this.boxGrossWeighing);
        grossParent.getChildren().add(1, this.boxTara);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isUpdate = false;
        checkBoxPrintWeighingTicket.setSelected(true);
        Platform.runLater(() -> {
            if (this.closeButton.getScene() != null) {
                this.stage = (Stage) this.closeButton.getScene().getWindow();
            }
        });
        //ked dam hmotnost buto nieco a potom az vyberiem vozidlo tak to brutto nie je zohladnene
        //spravim nejaku metodu kde nastavim spravne nazvy, switchnem  boxy ak bude treba pod, spravim to cez enum zrejme
        //setActualDatePicker();
         choiceBoxVehicleListener();
        //vehicleEntryTime = new LocalTimeSpinner();
        //vehicleDepartureTime = new LocalTimeSpinner();
        //entryTimeTextField = new TextFieldSpinner();


        //ked kliknem pridat napr. noveho dodavatela tak nech je aj zaskrtnuty automaticky podla toho kde som ho vybral
        //tiez len 2 desatinne miesta nech mi ukazuje
        //budem musiet nejako vyriesit to ked dam do textfieldu dlhe cislo tak mi tam da pismena
        this.textFieldWeightGross.setOnKeyReleased(keyEvent -> {
            setTextFieldWeightNet();
        });
        this.textFieldTara.setOnKeyReleased(keyEvent -> {
            setTextFieldWeightNet();
        });
    }

    public void setType(WeighingActionType type) {
        switch (type) {
            case SUPPLIER -> setSupplier();
            case BUYER -> setBuyer();
            //case UPDATE -> System.out.println("UPDATE");
        }
    }

    private void setSupplier() {
        setUpWindow("Dodávateľ", null, "Vstup naloženého vozidla", "Výstup prázdneho vozidla");
        this.isSupplier = true;
        this.switchBoxes();
    }

    private void setBuyer() {
        setUpWindow("Odberateľ", null, "Vstup prázdneho vozidla", "Výstup naloženého vozidla");
        this.isSupplier = false;
    }

    private void setUpWindow(String KindOfPartner, LocalDate date, String entryLabel, String extradictionLabel) {
        this.setLabelTypeOfPartner(KindOfPartner);
        this.setDatePicker(date);
        this.setLabelEntryVehicle(entryLabel);
        this.setLabelExtradictionVehicle(extradictionLabel);
    }

    public void init() {
        clearChoiceBoxes();
        getAllPartners();
        getAllVehicles();
        getAllMaterials();
    }

    public void setServices(PartnerService partnerService, VehicleService vehicleService, MaterialService materialService, WeighingService weighingService) {
        this.partnerService = partnerService;
        this.vehicleService  = vehicleService;
        this.materialService = materialService;
        this.weighingService = weighingService;
    }

    public void setUpdate(Integer number) {
        this.isUpdate = true;
        this.updateNumberWeighing = number;

        WeighingUpdateResponse weighingUpdateResponse = weighingService.getWeighingByNumber(number);

        LocalDate weighingDate = LocalDate.parse(weighingUpdateResponse.getLocalDate());
        LocalTime weighingEntryTime = LocalTime.parse(weighingUpdateResponse.getLocalTimeEntry());
        LocalTime weighingDepartureTime = LocalTime.parse(weighingUpdateResponse.getLocalTimeDeparture());

        this.datePicker.setValue(weighingDate);
        this.datePicker.setDisable(true);

        this.vehicleEntryTime.getValueFactory().setValue(weighingEntryTime);
        this.vehicleDepartureTime.getValueFactory().setValue(weighingDepartureTime);


        DialPartnerResponse temporaryPartner = new DialPartnerResponse(null, weighingUpdateResponse.getPartnerName(), null);
        Vehicle temporaryVehicle = new Vehicle(null, weighingUpdateResponse.getVehicleIdentificationNumber(), null, weighingUpdateResponse.getTara().doubleValue());
        Material temporaryMaterial = new Material(null, weighingUpdateResponse.getMaterial(),null, null);

        //MOZNO MI BUDE BLBNUT NETT HODNOTA KED BUDEM UPDATOVAT VYDAJKU. A POTOM KED BUDEM ROBIT ZASE PRIJEMKU KVOLI PORADIU. LEBO SA MI NETO ZMENI LEN PRI CBOXE
        //MAL BY SOM SA POZRIET NA FORMATOVANIE HODNOT CISEL ABY SOM NEMOHOL NAPISAT NAPR. 553.00000000 RESP. ASPON PRI UKLADANI NECH SA TO ZMENI AKO MA NA JEDNU 0 NAPR ALEBO ZIADNU 0.
        this.getTextFieldWeightGross().setText(weighingUpdateResponse.getGross().toString());

        setTempForChoiceBoxUpdate(this.choiceBoxTypeOfPartner, temporaryPartner);
        setTempForChoiceBoxUpdate(this.choiceBoxVehicle, temporaryVehicle);
        setTempForChoiceBoxUpdate(this.choiceBoxMaterial, temporaryMaterial);

        /*
        TOTO JE DOBRA METODA11111111111111111111111111111111111111111111111111111111111111111111111111111111111111
        this.vehicleEntryTime.setTime(weighingEntryTime);
        this.vehicleDepartureTime.setTime(weighingDepartureTime);

         */
        getVehicleEntryTime().setEditable(false);

        this.getTextAreaNote().setText(weighingUpdateResponse.getDescription());

    }

    private <T> void setTempForChoiceBoxUpdate(ChoiceBox<T> choiceBox, T tempObject) {
        if (tempObject == null) {return;}
        if (!choiceBox.getItems().contains(tempObject)) {
            choiceBox.getItems().add(tempObject);
        }
        choiceBox.setValue(tempObject);
    }

    public void setTextFieldWeightNet() {
            try {
                String weighingGrossString = this.textFieldWeightGross.getText();
                String weighingTaraString = this.textFieldTara.getText();

                if (weighingGrossString.isEmpty()) { weighingGrossString = "0";}
                if  (weighingTaraString.isEmpty()) { weighingTaraString = "0";}


                //DecimalFormat df = new DecimalFormat("#.##");

                //String grossFormated = df.format(Double.parseDouble(weighingGrossString));
                //String taraFormated = df.format(Double.parseDouble(weighingTaraString));


                BigDecimal weighingGross = new BigDecimal(weighingGrossString);
                BigDecimal weighingTara = new BigDecimal(weighingTaraString);



                if (weighingGross.doubleValue() < 0) {
                    weighingGross = new BigDecimal(0);
                }
                if  (weighingTara.doubleValue() < 0) {
                    weighingTara = new BigDecimal(0);
                }




                //BigDecimal netValue = BigDecimal.valueOf(weighingGross.doubleValue() - weighingTara.doubleValue()).setScale(2, RoundingMode.HALF_UP);
                String netValue = formatDoubleValue().format(weighingGross.doubleValue() - weighingTara.doubleValue());
                System.out.println("NASTAVUJEM NETT: " + netValue);
                //String netValue = formatDoubleValue(weighingGross.doubleValue() - weighingTara.doubleValue());
                this.textFieldWeightNet.setText(netValue.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
    }

    private void clearChoiceBoxes() {
        this.choiceBoxTypeOfPartner.getItems().clear();
        this.choiceBoxVehicle.getItems().clear();
        this.choiceBoxMaterial.getItems().clear();
    }

    private void getAllPartners() {
        List<DialPartnerResponse> partners = partnerService.getAllPartners();
        choiceBoxSetter(choiceBoxTypeOfPartner, partners, DialPartnerResponse::getName);
    }

    private void getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        choiceBoxSetter(choiceBoxVehicle, vehicles, Vehicle::getIdentificationNumber);
    }

    private void getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        choiceBoxSetter(choiceBoxMaterial, materials, Material::getName);
    }

    @FXML
    public void setActualTime(ActionEvent event) {
        /*
        TOTO JE DOBRA METODA11111111111111111111111111111111111111111111111111111111111111111111111111111111111111
        if (event.getSource() == setActualEntryTimeButton) {
            vehicleEntryTime.setActualTime();
        } else if (event.getSource() == setActualDepartureTimeButton) {
            vehicleDepartureTime.setActualTime();
        }
         */
    }

    @FXML
    public void saveWeighing(ActionEvent event) throws IOException {
        if (!checkInputs()) {
            return;
        }
        /*
        - potom check box aby bol ihned zaskrtnuty

        */

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalDate localDate = this.datePicker.getValue();
        LocalTime localTimeEntry = LocalTime.parse(this.vehicleEntryTime.getValue().format(timeFormatter));
        LocalTime localTimeDeparture = LocalTime.parse(this.vehicleDepartureTime.getValue().format(timeFormatter));

        Integer userId = UserSessionManager.getActualUser().getId();

        String grossFormated = formatDoubleValue().format(Double.parseDouble(this.textFieldWeightGross.getText())).replace(",", ".");
        //String taraFormated = this.textFieldTara.getText().replace(",", ".");
        String taraFormated = formatDoubleValue().format(Double.parseDouble(this.textFieldTara.getText())).replace(",", ".");
        String nettFormated = formatDoubleValue().format(Double.parseDouble(this.textFieldWeightNet.getText())).replace(",", ".");

        //MAM TO NEJAKO MOC PREKOMBINOVANE TU DOLE A MAM AJ ERROR ZREJME TAM NIEKDE PRIDA CIARKU KED DAVAM DOUBLE PARSE ALEBO NIECO TAKE
        BigDecimal gross = new BigDecimal(grossFormated);
        BigDecimal tara =  new BigDecimal(taraFormated);
        BigDecimal nett = new BigDecimal(nettFormated);

        if (this.isUpdate) {

            //V UPDATE TU MAM ISSUED NAME KTORE BERIEM Z AKTUALNE PRIHLASENEHO USERA A TO POTOM UKLADAM. MOZE SA STAT ZE INY ADMIN MENI HODSNOTY CO VYTVORIL NIEKTO INY. MOZNO TO TAK MA BYT ZATIAL TO NECHAM TAK AJ PRI KONTROLE TO TAK NECHAM
            //MOZNO AJ WeighingUpdateResponse 'TRIEDU' SKRATIM LEBO MI PRIDE ZE type, nett NEPOTREBUJEM
            //SKUSIM PRI UKLADANI WEIGHING SPRAVIT TO KED JE ZA CELYM CISLOM , A UZ LEN 0 NAPR 66,0 TAK NECH TAM TA NULA UZ NIE JE. SPRAVIM TO KED OTVORIM TOTO OKNO NECH NETO JE VZDY CELE AK JE NIECO ,0 A ZVYSNE 2 AK AJ DAM ,0 TAK SA TO NEULOZI TAK AJ INDE TO PORIESIT PRE ISTOTU, SERVICE



            WeighingUpdateResponse weighingUpdateResponseOld = weighingService.getWeighingByNumber(this.updateNumberWeighing);

            System.out.println("NETTTOOOOOOOOOOOOOOOOOOOOOOO: " + nett);

            WeighingUpdateResponse weighingUpdateResponseNew = new WeighingUpdateResponse(this.updateNumberWeighing, localDate.toString(), localTimeEntry.toString(), localTimeDeparture.toString(),
                    this.choiceBoxTypeOfPartner.getValue().getName(), this.choiceBoxVehicle.getValue().getIdentificationNumber(), weighingUpdateResponseOld.getIssuedName(), this.choiceBoxMaterial.getValue().getName(),
                    gross, tara, nett, this.textAreaNote.getText());

            if (weighingUpdateResponseOld.equals(weighingUpdateResponseNew)) {
                System.out.println("EQUAL");
                closeWindow(null);
                return;
            }

            //mozem skontrolovat ci bola urobena nejaka zmena porovnam napr. objekty toho co som tam poslal na vyplnenie s novymi co tu ukladam
            //ak ukladam nove hodnoty tak ulozim do db a dam tlacit vazny listok aj je zaskrtnuty inak ak su rovnake len zavriem okno
            Weighing weighing = new Weighing(null, this.updateNumberWeighing, this.isSupplier, localDate.toString(), localTimeEntry.toString(), localTimeDeparture.toString(), gross, tara, nett, this.textAreaNote.getText(), null,
                    this.choiceBoxTypeOfPartner.getValue().getId(), this.choiceBoxVehicle.getValue().getId(), this.choiceBoxMaterial.getValue().getId());
            this.weighingService.updateWeighing(weighing);
            //this.weighingService.updateWeighing(this.updateNumber, this.isSupplier, localDate, localTimeEntry, localTimeDeparture, gross, tara, nett,
            //this.textAreaNote.getText(), userId, this.choiceBoxTypeOfPartner.getValue().getId(), this.choiceBoxVehicle.getValue().getId(), this.choiceBoxMaterial.getValue().getId());

        } else {
            int generatedNumber = weighingService.generateWeighingNumber(localDate);

            Weighing weighing = new Weighing(null, generatedNumber, this.isSupplier, localDate.toString(), localTimeEntry.toString(), localTimeDeparture.toString(), gross, tara, nett, this.textAreaNote.getText(), userId,
                    this.choiceBoxTypeOfPartner.getValue().getId(), this.choiceBoxVehicle.getValue().getId(), this.choiceBoxMaterial.getValue().getId());
             weighingService.saveWeighing(weighing);
        }
        PrinterJob job = PrinterJob.createPrinterJob();

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/weighing/Extradition.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/weighing/WeighingTemplate.fxml"));
        Parent document = loader.load();

        if (checkBoxPrintWeighingTicket.isSelected()) {
            if (job != null && job.showPrintDialog(this.stage)) {
                job.printPage(document);
                job.endJob();
            }
        }

        //refresh table callback
        if (this.onSaveRefreshTable != null) {
            this.onSaveRefreshTable.run();
        }
    }

    private boolean checkInputs() {
        if (this.datePicker.getValue() == null) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Dátum nesmie byť prázdny!", "Musíš vyplniť dátum.");
        } else if (this.choiceBoxTypeOfPartner.getValue() == null) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Partner nie je vybraný!", "Musíš vybrať partnera.");
        } else if (this.choiceBoxVehicle.getValue() == null) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Vozidlo nie je vybrané!", "Musíš vybrať vozidlo.");
        } else if (this.choiceBoxMaterial.getValue() == null) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Materiál nie je vybraný!", "Musíš vybrať materiál.");
        } else if (this.textFieldWeightGross.getText().isEmpty()) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Brutto je prázdne!", "Hodnota brutto musí byť vyplnená.");
        } else if (this.textFieldTara.getText().isEmpty()) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Tara je prázdna!", "Hodnota tara musí byť vyplnená.");
        } else if (this.textFieldWeightNet.getText().isEmpty()) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Netto je prázdne!", "Hodnota netto musí byť vyplnená.");
        } else if (this.vehicleEntryTime.getValue().isAfter(this.vehicleDepartureTime.getValue())) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Čas vstupu je po čase výstupu váženia!", "Čas vstupu vozidla musí byť pred časom výstupu.");
            //TEXT AREA ASI MOZE BYT PRAZDNY LEN POZRIEM CI TO BUDE VADIT DALEJ
        //} else if (this.textAreaNote.getText().isEmpty()) {
           // WarningAlert.warningAlert(Alert.AlertType.WARNING, "Partner nie je vybraný!", "Musíš vybrať partnera.");
            //System.out.println("EMPTY TEXT note");
        } else if (Double.parseDouble(this.textFieldWeightNet.getText().replace(",", ".")) < 0) {
            WarningAlert.warningAlert(Alert.AlertType.WARNING, "Netto nemôže byť menej ako 0!", "Zvýš hodnotu netto.");
        } else {
            return true;
        }
        return false;
    }

    public void setOnSaveRefreshTable(Runnable onSaveRefreshTable) {
        this.onSaveRefreshTable = onSaveRefreshTable;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public HBox getBoxTara() {
        return boxTara;
    }

    public HBox getBoxGrossWeighing() {
        return boxGrossWeighing;
    }

    public ChoiceBox<?> getChoiceBoxTypeOfPartner() {
        return choiceBoxTypeOfPartner;
    }

    public ChoiceBox<?> getChoiceBoxVehicle() {
        return choiceBoxVehicle;
    }

    public ChoiceBox<?> getChoiceBoxMaterial() {
        return choiceBoxMaterial;
    }

    public Button getAddTaraButton() {
        return addTaraButton;
    }

    public Button getSetEntryTimeButton() {
        return setEntryTimeButton;
    }

    public Button getAddWeightGrossButton() {
        return addWeightGrossButton;
    }

    public Button getSetDepartureTimeButton() {
        return setDepartureTimeButton;
    }

    public Button getSaveWeighingButton() {
        return saveWeighingButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public TextField getTextFieldTara() {
        return textFieldTara;
    }

    public Spinner<?> getVehicleEntryTime() {
        return vehicleEntryTime;
    }

    public TextField getTextFieldWeightGross() {
        return textFieldWeightGross;
    }

    public Spinner<?> getVehicleDepartureTime() {
        return vehicleDepartureTime;
    }

    public TextField getTextFieldWeightNet() {
        return textFieldWeightNet;
    }

    public TextArea getTextAreaNote() {
        return textAreaNote;
    }

    public CheckBox getCheckBoxPrintWeighingTicket() {
        return checkBoxPrintWeighingTicket;
    }

    public void setDatePicker(LocalDate date) {
        this.datePicker.setValue(date == null ? LocalDate.now() : date);
    }

    public void setLabelTypeOfPartner(String text) {
        this.labelTypeOfPartner.setText(text);
    }

    public void setLabelEntryVehicle(String text) {
        this.labelEntryVehicle.setText(text);
    }

    public void setLabelExtradictionVehicle(String text) {
        this.labelExtradictionVehicle.setText(text);
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

    private void choiceBoxVehicleListener() {
        this.choiceBoxVehicle.setOnAction(actionEvent -> {
            if (choiceBoxVehicle.getValue() != null) {
                this.textFieldTara.setText(formatDoubleValue().format(choiceBoxVehicle.getValue().getTara()));
                setTextFieldWeightNet();
            }
        });

    }

    private DecimalFormat formatDoubleValue() {
        return new DecimalFormat("#.##");
    }

}
