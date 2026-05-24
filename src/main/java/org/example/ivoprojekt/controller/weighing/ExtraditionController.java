package org.example.ivoprojekt.controller.weighing;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ExtraditionController implements Initializable {
    @FXML
    private WeighingTemplateController templateController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        templateController.setLabelTypeOfPartner("Odoberateľ");
        templateController.setActualDatePicker();
        templateController.setLabelEntryVehicle("Vstup prázdneho vozidla");
        templateController.setLabelExtradictionVehicle("Výstup naloženého vozidla");
    }
}
