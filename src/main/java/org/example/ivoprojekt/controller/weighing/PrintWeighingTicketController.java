package org.example.ivoprojekt.controller.weighing;

import javafx.fxml.FXML;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;

public class PrintWeighingTicketController {
    @FXML
    PrintTemplateController templateController;

    @FXML
    PrintTemplateController secondTemplateController;

    public void setValuesToPrint(WeighingPrintResponse weighingPrintResponse, String issued) {
        templateController.setValuesToPrint(weighingPrintResponse, issued);
        secondTemplateController.setValuesToPrint(weighingPrintResponse, issued);
    }
}
