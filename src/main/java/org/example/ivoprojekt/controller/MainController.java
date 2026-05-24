package org.example.ivoprojekt.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.ivoprojekt.api.mapper.DtoMapper;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;
import org.example.ivoprojekt.api.response.WeighingTableResponse;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.controller.dials.MaterialsController;
import org.example.ivoprojekt.controller.dials.PartnersController;
import org.example.ivoprojekt.controller.dials.UsersController;
import org.example.ivoprojekt.controller.dials.VehiclesController;
import org.example.ivoprojekt.controller.user.ChangePasswordController;
import org.example.ivoprojekt.controller.user.LoginController;
import org.example.ivoprojekt.controller.user.RegisterController;
import org.example.ivoprojekt.controller.utill.ThrowingRunnable;
import org.example.ivoprojekt.controller.utill.WeighingActionType;
import org.example.ivoprojekt.controller.utill.WeighingTicketType;
import org.example.ivoprojekt.controller.utill.WeighingTimePeriods;
import org.example.ivoprojekt.controller.weighing.*;
import org.example.ivoprojekt.domain.User;
import org.example.ivoprojekt.domain.UserToken;
import org.example.ivoprojekt.javaFxUtil.NewWindow;
import org.example.ivoprojekt.javaFxUtil.TableUtil;
import org.example.ivoprojekt.service.*;
import org.example.ivoprojekt.userUtill.session.UserSessionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static org.example.ivoprojekt.controller.utill.WeighingTimePeriods.TODAY;
import static org.example.ivoprojekt.controller.utill.WeighingTimePeriods.WEEK;

public class MainController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserService userService;
    private PartnerService partnerService;
    private VehicleService vehicleService;
    private MaterialService materialService;
    private WeighingService weighingService;
    private int previousMinute = -1;
    private String chosenTimePeriodStart;
    private String chosenTimePeriodEnd;

    @FXML
    private DatePicker customPeriodStart, customPeriodEnd;

    @FXML
    private HBox customPeriodHBox;

    @FXML
    private ImageView taraImageView, acceptanceImageView, extradictionImageView, editImageView, deleteImageView, printWeighingImageView, printOverviewImageView;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private MenuItem dialsPartners, dialsTypeOfMaterial, dialsUsers, dialsVehicles;

    @FXML
    private MenuItem userChangePassword, userLogout, userLogin, userRegister;

    @FXML
    private MenuItem weightTaraSetting, weightAcceptance, weightExtradition, weightUpdateWeighing, weightDeleteWeighing, weightPrintWeighingTicket, weightPrintOverview;

    @FXML
    private ListView<String> listView;

    @FXML
    private TableView<WeighingTableResponse> table;

    @FXML
    private TableColumn<WeighingTableResponse, Integer> numberColumn;

    @FXML
    private TableColumn<WeighingTableResponse, String> typeColumn;

    @FXML
    private TableColumn<WeighingTableResponse, String> dateTimeColumn, partnerNameColumn, vehicleINColumn, issuedNameColumn, materialColumn, descriptionColumn;

    @FXML
    private TableColumn<WeighingTableResponse, BigDecimal> grossColumn, taraColumn, nettColumn;

    @FXML
    private VBox taraWrapper, acceptanceWrapper, extradictionWrapper, editWrapper, deleteWrapper, printWeighingWrapper, printOverviewWrapper;

    private List<MenuItem> loggedUserItems;
    //private BooleanProperty isloggedIn;
    private BooleanProperty isEditingWeighing;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox.setVgrow(this.table, Priority.ALWAYS);

        Platform.runLater(() -> this.stage = (Stage) table.getScene().getWindow());

        setBindings();
        setListView();

        createMainImages();
        setImagesListening();

        setImageMouseEvent(this.taraWrapper);
        setImageMouseEvent(this.acceptanceWrapper);
        setImageMouseEvent(this.extradictionWrapper);
        setImageMouseEvent(this.editWrapper);
        setImageMouseEvent(this.deleteWrapper);
        setImageMouseEvent(this.printWeighingWrapper);
        setImageMouseEvent(this.printOverviewWrapper);

        Timeline clock = new Timeline(
                new KeyFrame(Duration.seconds(1), e-> currentTimeGenerate())
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void setUserRights() {
        boolean isAdminPermission = UserSessionManager.getActualUser() != null && UserSessionManager.getActualUser().getIsAdmin();
        dialsPartners.setDisable(!isAdminPermission);
        dialsTypeOfMaterial.setDisable(!isAdminPermission);
        dialsUsers.setDisable(!isAdminPermission);
        dialsVehicles.setDisable(!isAdminPermission);
    }

    private void setBindings() {

        List<VBox> loggedUserButtons = Arrays.asList(taraWrapper, acceptanceWrapper, extradictionWrapper, printOverviewWrapper);
        List<VBox> editWeighingButtons = Arrays.asList(editWrapper, deleteWrapper, printWeighingWrapper);

        BooleanProperty isloggedIn = UserSessionManager.getIsLoggedIn();
        isEditingWeighing = new SimpleBooleanProperty(false);

        loggedUserButtons.forEach((button) -> {
            button.mouseTransparentProperty().bind(isloggedIn.not());
            button.opacityProperty().bind(
                    Bindings.when(isloggedIn).then(1.0).otherwise(0.4)
            );
        });

        editWeighingButtons.forEach((button) -> {
            button.mouseTransparentProperty().bind(isloggedIn.and(isEditingWeighing).not());
            button.opacityProperty().bind(
                    Bindings.when(isloggedIn.and(isEditingWeighing)).then(1.0).otherwise(0.4)
            );
        });

        this.userLogin.disableProperty().bind(isloggedIn);
        this.userRegister.disableProperty().bind(isloggedIn);
        this.userChangePassword.disableProperty().bind(isloggedIn.not());
        this.userLogout.disableProperty().bind(isloggedIn.not());
    }

    private void setImagesListening(){
        this.taraWrapper.setOnMouseClicked(event ->
                safeAction(() -> showWeighingTaraSetting(null)));

        this.acceptanceWrapper.setOnMouseClicked(event ->
                safeAction(() -> showWeightAcceptance(null)));

        this.extradictionWrapper.setOnMouseClicked(event ->
                safeAction(() -> showWeightExtradition(null)));

        this.editWrapper.setOnMouseClicked(event ->
                safeAction(() -> showUpdateWeighing(null)));

        this.deleteWrapper.setOnMouseClicked(event ->
                safeAction(() -> weightDelete(null)));

        this.printWeighingWrapper.setOnMouseClicked(event ->
                safeAction(() -> showWeightPrintWeighingTicket(null)));

        this.printOverviewWrapper.setOnMouseClicked(event ->
                safeAction(() -> showWeightPrintOverview(null)));
    }

    private void safeAction(ThrowingRunnable action) {
        try {
            action.run();
        } catch (Exception e) {
            WarningAlert.warningAlert(Alert.AlertType.ERROR, "Chyba pri renderovaní obrázkov!", e.getMessage());
        }
    }

    private void createMainImages() {
        this.taraImageView.setImage(generatePngImage("tara"));
        this.acceptanceImageView.setImage(generatePngImage("príjemka"));
        this.extradictionImageView.setImage(generatePngImage("výdajka"));
        this.editImageView.setImage(generatePngImage("úprava"));
        this.deleteImageView.setImage(generatePngImage("vymazanie"));
        this.printWeighingImageView.setImage(generatePngImage("tlačiareň"));
        this.printOverviewImageView.setImage(generatePngImage("tlačiareň"));
    }

    private void setImageMouseEvent(VBox vBox) {
        vBox.setOnMouseEntered(event ->
            vBox.setStyle("""
                -fx-background-radius: 10 10 0 0;
                -fx-background-color: rgba(10,150,255,0.2);
                """)
        );
        vBox.setOnMouseExited(event ->
            vBox.setStyle("")
        );
    }

    private Image generatePngImage(String name) {
        return new Image(Objects.requireNonNull(getClass().getResource("/images/main/" + name + ".png")).toExternalForm());
    }

    private void currentTimeGenerate() {
        LocalDateTime now = LocalDateTime.now();
        if (previousMinute != now.getMinute()) {
            previousMinute = now.getMinute();
            currentTimeLabel.setText(now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
    }

    public void setServices(UserService userService,  PartnerService partnerService, VehicleService vehicleService, MaterialService materialService, WeighingService WeighingService) {
        this.userService = userService;
        this.partnerService = partnerService;
        this.vehicleService = vehicleService;
        this.materialService = materialService;
        this.weighingService = WeighingService;
        System.out.println( weighingService.getAllWeighingForTable("2025-03-05", "2026-03-05"));
        initTable();
    }

    public void initTable() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        partnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("partnerName"));
        vehicleINColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleIdentificationNumber"));
        issuedNameColumn.setCellValueFactory(new PropertyValueFactory<>("issuedName"));
        materialColumn.setCellValueFactory(new PropertyValueFactory<>("material"));
        grossColumn.setCellValueFactory(new PropertyValueFactory<>("gross"));
        taraColumn.setCellValueFactory(new PropertyValueFactory<>("tara"));
        nettColumn.setCellValueFactory(new PropertyValueFactory<>("nett"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        changeWeighingByListView(listView.getItems().getFirst());
        setColorsAndFocusForTable();
        setCustomPeriodValues();
    }

    private void setupTable() {
        this.table.getItems().clear();
        List<WeighingTableResponse> weighing = this.weighingService.getAllWeighingForTable(this.chosenTimePeriodStart, this.chosenTimePeriodEnd);
        if (!weighing.isEmpty()) {
            TableUtil.setLayout(table);
            table.getItems().addAll(weighing);
        }
    }

    private void setColorsAndFocusForTable() {
        ObjectProperty<TableRow<WeighingTableResponse>> lastSelectedRow = new SimpleObjectProperty<>();

        this.table.setRowFactory(tv -> {
            TableRow<WeighingTableResponse> row = new  TableRow<>() {
            @Override
            protected void updateItem(WeighingTableResponse item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("row-green", "row-red");

                if (!empty && item != null) {
                    if (WeighingTicketType.ACCEPTANCE.name.equals(item.getType())) {
                        getStyleClass().add("row-green");
                    } else {
                        getStyleClass().add("row-red");
                    }
                }
            }
        };
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    lastSelectedRow.set(row);
                }
            });
            return row;
        });
        table.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (lastSelectedRow.get() != null) {
                Bounds boundsSelectedRow = lastSelectedRow.get().localToScene(lastSelectedRow.get().getLayoutBounds());
                if (!boundsSelectedRow.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY())) {
                    isEditingWeighing.setValue(false);
                    table.getSelectionModel().clearSelection();
                } else {
                    isEditingWeighing.setValue(true);
                }
            }
        });

    }

    private void setCustomPeriodValues() {
        customPeriodStart.setValue(LocalDate.now().minusDays(1));
        customPeriodEnd.setValue(LocalDate.now());
    }

    public void setActualUserToSessionManager() {
        String token = UserSessionManager.loadUserToken();
        if (token != null) {
            try {
            //UserToken dbToken = userService.getUserToken(token);
                //toto vrati usera a je tam ako false admin mozno to nevrati presne co chcem tak to automaticky da false lebo to nie je nastavene
                User loggedUser = userService.findUserByToken(token);
                //user ma byt otional
                UserSessionManager.setActualUser(DtoMapper.toSessionUser(loggedUser));
            } catch (NotFoundException e) {
                UserSessionManager.clearUserSession();
            } catch (Exception e) {
                System.out.println("somethin is wrong");
            }
        }
        setUserRights();
    }

    private void setListView() {
        String[] datesOfWeighting = {TODAY.period,  WEEK.period, WeighingTimePeriods.CUSTOM.period};
        listView.getItems().addAll(datesOfWeighting);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                changeWeighingByListView(listView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void changeWeighingByListView(String chosenPeriod) {
        WeighingTimePeriods period = WeighingTimePeriods.fromString(chosenPeriod);
        switch (period) {
            case TODAY -> {
                this.chosenTimePeriodStart = LocalDate.now().toString();
                this.chosenTimePeriodEnd = LocalDate.now().plusDays(1).toString();
                this.customPeriodHBox.setVisible(false);
                this.customPeriodHBox.setManaged(false);
                setupTable();
            }
            case WEEK -> {
                this.chosenTimePeriodStart = LocalDate.now().minusWeeks(1).toString();
                this.chosenTimePeriodEnd = LocalDate.now().plusDays(1).toString();
                this.customPeriodHBox.setVisible(false);
                this.customPeriodHBox.setManaged(false);
                setupTable();
            }
            case CUSTOM -> {
                this.customPeriodHBox.setVisible(true);
                this.customPeriodHBox.setManaged(true);
            }
        }
    }

    @FXML
    public void customPeriod(ActionEvent event) {
        if (customPeriodStart.getValue() == null || customPeriodStart.getValue().isAfter(customPeriodEnd.getValue())) {
            customPeriodStart.requestFocus();
        } else if(customPeriodEnd.getValue() == null) {
            customPeriodEnd.requestFocus();
        } else {
            this.chosenTimePeriodStart = customPeriodStart.getValue().toString();
            this.chosenTimePeriodEnd = customPeriodEnd.getValue().toString();
            setupTable();
        }
    }

    @FXML
    public void loginUser(ActionEvent event) throws IOException {
        //System.out.println("TOTO SA ZREJME ZAVOLA JA KED BY MA NEMALO PRIHLASIT HNED NA ZACIATKU");
        LoginController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Login.fxml", "Prihlásenie");
        controller.setUserService(userService);
        controller.setOnLoginSuccess(this::setActualUserToSessionManager);
    }

    @FXML
    public void registerUser(ActionEvent event) throws IOException {
        RegisterController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/Register.fxml", "Registrácia");
        controller.setServices(userService, partnerService);
        controller.setOnRegisterSuccess(this::setActualUserToSessionManager);
    }

    @FXML
    public void changePassword(ActionEvent event) throws IOException {
        ChangePasswordController controller = NewWindow.openNewWindow(this.stage, "/fxml/user/ChangePassword.fxml", "Zmena hesla");
        controller.setServices(userService);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        userService.deleteUserToken(UserSessionManager.loadUserToken());
        UserSessionManager.clearUserSession();
    }

    @FXML
    public void showAllUsers(ActionEvent event) throws IOException {
        UsersController controller = NewWindow.openNewWindow(this.stage, "/fxml/dials/Users.fxml", "Použivatelia");
        controller.setUserService(userService, partnerService);
        controller.initTable();
    }

    @FXML
    public void showAllPartners(ActionEvent event) throws IOException {
        PartnersController controller = NewWindow.openNewWindow(this.stage, "/fxml/dials/Partners.fxml", "Zoznam partnerov");
        controller.setPartnerService(partnerService);
        controller.initTable();
    }

    @FXML
    public void showAllVehicles(ActionEvent event) throws IOException {
        VehiclesController controller = NewWindow.openNewWindow(this.stage, "/fxml/dials/Vehicles.fxml", "Zoznam vozidiel");
        controller.setVehicleService(vehicleService);
        controller.initTable();
    }

    @FXML
    public void showAllMaterials(ActionEvent event) throws IOException {
        MaterialsController controller = NewWindow.openNewWindow(this.stage, "/fxml/dials/Materials.fxml", "Zoznam materiálov");
        controller.setMaterialService(materialService);
        controller.initTable();
    }

    @FXML
    public void showWeighingTaraSetting(ActionEvent event) throws IOException {
        TaraController controller = NewWindow.openNewWindow(this.stage, "/fxml/weighing/Tara.fxml", "Nastaviť taru");
        controller.setVehicleService(vehicleService);
        controller.initTara();
    }

    @FXML
    public void showWeightAcceptance(ActionEvent event) throws IOException {
        WTC controller = NewWindow.openNewWindow(this.stage, "/fxml/weighing/WeighingTemplate.fxml", "Príjem tovaru alebo plodiny");
        controller.setServices(partnerService, vehicleService, materialService, weighingService);
        controller.setType(WeighingActionType.SUPPLIER);
        controller.init();
        controller.setOnSaveRefreshTable(this::setupTable);
    }

    @FXML
    public void showWeightExtradition(ActionEvent event) throws IOException {
        WTC controller = NewWindow.openNewWindow(this.stage, "/fxml/weighing/WeighingTemplate.fxml", "Výdaj tovaru alebo plodiny");
        controller.setServices(partnerService, vehicleService, materialService, weighingService);
        controller.setType(WeighingActionType.BUYER);
        controller.init();
        controller.setOnSaveRefreshTable(this::setupTable);
    }

    @FXML
    public void showUpdateWeighing(ActionEvent event) throws IOException {
        int number = table.getSelectionModel().getSelectedItem().getNumber();
        String type = table.getSelectionModel().getSelectedItem().getType();

        WTC controller = NewWindow.openNewWindow(this.stage, "/fxml/weighing/WeighingTemplate.fxml",
        type.equals(WeighingTicketType.ACCEPTANCE.name) ? "Príjem tovaru alebo plodiny" : "Výdaj tovaru alebo plodiny");
        controller.setServices(partnerService, vehicleService, materialService, weighingService);
        controller.setType(type.equals(WeighingTicketType.ACCEPTANCE.name) ? WeighingActionType.SUPPLIER : WeighingActionType.BUYER);
        controller.init();
        controller.setUpdate(number);
        controller.setOnSaveRefreshTable(this::setupTable);
    }

    @FXML
    public void weightDelete(ActionEvent event) throws IOException {
        //before deleting show alert if really delete item
        //after this try behavior when some material or vehicles, .. are empty
        Integer number = table.getSelectionModel().getSelectedItem().getNumber();
        weighingService.deleteById(number);
    }

    @FXML
    public void showWeightPrintWeighingTicket(ActionEvent event) throws IOException {
        PrintWeighingTicketController controller = NewWindow.openNewWindow(this.stage, "/fxml/PrintWeighingTicket.fxml", "Vážny lístok");
        Integer idNumber = this.table.getSelectionModel().getSelectedItem().getNumber();
        String issued = this.table.getSelectionModel().getSelectedItem().getIssuedName();

        WeighingPrintResponse weighingPrintResponse = weighingService.getWeighingPrintResponse(idNumber);
        controller.setValuesToPrint(weighingPrintResponse, issued);
    }

    @FXML
    public void showWeightPrintOverview(ActionEvent event) throws IOException {
        PrintOverviewController controller = NewWindow.openNewWindow(this.stage, "/fxml/weighing/PrintOverview.fxml", "Prehľad");
        controller.setServices(partnerService, vehicleService,userService, materialService, weighingService);
        controller.init();
    }


}
