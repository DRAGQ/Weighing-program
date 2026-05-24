package org.example.ivoprojekt.controller.dials;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;

public class DialsTemplateController {
    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> firstColumn, secondColumn, thirdColumn;

    @FXML
    private FlowPane flowPane;

    @FXML
    private Button addButton, updateButton, deleteButton;

    public TableView<?> getTable() {
        return this.table;
    }

    public TableColumn<?, ?> getFirstColumn() {
        return firstColumn;
    }

    public TableColumn<?, ?> getSecondColumn() {
        return secondColumn;
    }

    public TableColumn<?, ?> getThirdColumn() {
        return thirdColumn;
    }

    public FlowPane getFlowPane() {return flowPane;}

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}
