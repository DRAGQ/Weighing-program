package org.example.ivoprojekt.javaFxUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NewWindow {
    private static Stage stage;

    public static <T> T openNewWindow(Stage parentStage, String javaFxmlPath, String titleText) throws IOException {
        FXMLLoader loader = new FXMLLoader(NewWindow.class.getResource(javaFxmlPath));
        Parent root = loader.load();
        stage = new Stage();
        Scene scene = new Scene(root);
        Image injectionIcon = new Image(Objects.requireNonNull(NewWindow.class.getResource("/images/injection.png")).toExternalForm());
        stage.getIcons().add(injectionIcon);
        stage.setTitle(titleText);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
        stage.setX(parentStage.getX() + parentStage.getWidth() / 2 - stage.getWidth() / 2);
        double yValue = (parentStage.getY() + parentStage.getHeight() / 2 - stage.getHeight() / 2);
        stage.setY(yValue < 0 ? 0 : yValue);
        return loader.getController();
    }

    /* static void centerStage(Stage parentStage) {
        stage.setX(parentStage.getX() + parentStage.getWidth() / 2 - stage.getWidth() / 2);
        stage.setY(parentStage.getY() + parentStage.getHeight() / 2 - stage.getHeight() / 2);
    }*/
}
