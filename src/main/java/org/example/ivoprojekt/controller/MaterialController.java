package org.example.ivoprojekt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.service.MaterialService;
import org.example.ivoprojekt.service.VehicleService;

public class MaterialController {
    private MaterialService materialService;
    private boolean isEdit;
    private Integer chosenId;
    private String chosenName;
    private Double chosenHumidity;
    private Double chosenCoefficient;
    private Runnable onMaterialSuccess;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldHumidity;

    @FXML
    private TextField textFieldCoefficient;

    @FXML
    private Button exitButton;

    @FXML
    void saveMaterial(ActionEvent event) {
        String inputName = textFieldName.getText();
        Double inputHumidity = checkDoubleValue(textFieldHumidity.getText());
        Double inputCoefficient = checkDoubleValue(textFieldCoefficient.getText());
        if (inputHumidity == null || inputCoefficient == null) {
            System.out.println("not double");
            return;
        }
        //mohol by som urobit ten focus a aj alert a urobit to niekde v inej metode a tiez by som zvlast handloval to ci dal user spravne double cislo

        if (checkEmptyInput(textFieldName) && checkEmptyInput(textFieldHumidity) && checkEmptyInput(textFieldCoefficient)) {
            if (isEdit) {
                if (!inputName.equals(this.chosenName) || !inputHumidity.equals(this.chosenHumidity) || !inputCoefficient.equals(this.chosenCoefficient)) {
                    this.materialService.updateMaterial(new Material(this.chosenId, inputName, inputHumidity, inputCoefficient));
                    if (onMaterialSuccess != null) {
                        onMaterialSuccess.run();
                    }
                } else {
                    System.out.println("Inputs are equal");
                }
            } else {
                this.materialService.saveMaterial(
                        new Material(null, inputName, inputHumidity, inputCoefficient)

                );
                this.chosenName = inputName;
                if (onMaterialSuccess != null) {
                    onMaterialSuccess.run();
                }
            }
            exitMaterialWindow(null);
        }

    }



    @FXML
    void exitMaterialWindow(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    private Double checkDoubleValue(String value) {
        Double result = null;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            //tu moze byt alert ze value ... je nie je float
        }
        return result;
    }

    public String getMaterialName() {
        return this.chosenName;
    }

    private boolean checkEmptyInput(TextField textField) {
        if (textField.getText().isEmpty()) {
            textField.requestFocus();
            return false;
        }
        return true;
    }

    public void setMaterialService(MaterialService materialService) {
        this.materialService = materialService;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setChosenMaterial(Integer id) {
        Material material = materialService.getMaterialById(id);
        System.out.println("id: " + id + "  " + material);
        this.chosenId = id;
        this.chosenName = material.getName();
        this.chosenHumidity = material.getHumidity();
        this.chosenCoefficient = material.getCoefficient();

        this.textFieldName.setText(this.chosenName);
        this.textFieldHumidity.setText(String.valueOf(this.chosenHumidity));
        this.textFieldCoefficient.setText(String.valueOf(this.chosenCoefficient));
    }

    public void setOnMaterialSuccess(Runnable onMaterialSuccess) {
        this.onMaterialSuccess = onMaterialSuccess;
    }
}
