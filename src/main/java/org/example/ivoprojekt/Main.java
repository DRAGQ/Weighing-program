package org.example.ivoprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.ivoprojekt.repository.*;
import org.example.ivoprojekt.config.DatabaseConfig;
import org.example.ivoprojekt.controller.MainController;
import org.example.ivoprojekt.service.*;
import org.jdbi.v3.core.Jdbi;

public class Main extends Application {
    private static UserService userService;
    private static PartnerService partnerService;
    private static VehicleService vehicleService;
    private static MaterialService materialService;
    private static WeighingService weighingService;

    public static void main(String[] args) {
        Jdbi jdbi = DatabaseConfig.getJdbi();

        UserRepository userRepository = new UserRepository(jdbi);
        UserTokenRepository userTokenRepository = new UserTokenRepository(jdbi);
        PartnerRepository partnerRepository = new PartnerRepository(jdbi);
        VehicleRepository vehicleRepository = new VehicleRepository(jdbi);
        MaterialRepository materialRepository = new MaterialRepository(jdbi);
        WeighingRepository weighingRepository = new WeighingRepository(jdbi);

        userService = new UserService(userRepository, userTokenRepository);
        partnerService = new PartnerService(partnerRepository);
        vehicleService = new VehicleService(vehicleRepository);
        materialService = new MaterialService(materialRepository);
        weighingService = new WeighingService(weighingRepository);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setServices(userService, partnerService, vehicleService, materialService, weighingService);
            controller.setActualUserToSessionManager();

            Image  injectionIcon = new Image(getClass().getResource("/images/injection.png").toExternalForm());

            stage.getIcons().add(injectionIcon);
            stage.setTitle("Kozlovina");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(e -> {
                e.consume();
                closeProgram(stage);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeProgram(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zatvoriť");
        alert.setHeaderText("Chceš zatvoriť program!");
        alert.setContentText("Máš uložené zmeny v programe?\n Pre zatvorenie stlač OK");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage.close();
        }
    }
}