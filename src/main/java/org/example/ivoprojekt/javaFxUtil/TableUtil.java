package org.example.ivoprojekt.javaFxUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class TableUtil {
    //public static Runnable onTableRowClicked;
    //public static boolean isTableClicked;
    public static BooleanProperty isClickedTableRow;

    public static void setLayout(TableView<?> table) {
        table.getColumns().getLast().prefWidthProperty().bind(table.widthProperty().subtract(20));
    }

    public static <T> void setTableFocusBehavior(TableView<T> table) {
        ObjectProperty<TableRow<T>> lastSelectedRow = new SimpleObjectProperty<>();
        TableUtil.isClickedTableRow = new SimpleBooleanProperty(false);

        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();

            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    lastSelectedRow.set(row);
                }
            });
            return row;
        });

        table.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            //boolean isFocused = false;
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (lastSelectedRow.get() != null) {
                    Bounds boundsSelectedRow = lastSelectedRow.get().localToScene(lastSelectedRow.get().getLayoutBounds());
                    if (!boundsSelectedRow.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY())) {
                        //callback
                        //isFocused = false;
                        TableUtil.isClickedTableRow.setValue(false);

                        table.getSelectionModel().clearSelection();
                    } else  {
                        //callback
                       // isFocused = true;
                        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
                        TableUtil.isClickedTableRow.setValue(true);
                    }
                }
            }
        });
       // return table.isFocused();
    }

    public static void setButtonBindings(Button update, Button delete) {
        List<Button> editWeighingButtons = Arrays.asList(update, delete);
        editWeighingButtons.forEach(button -> {
            System.out.println("AAA");
            button.mouseTransparentProperty().bind(TableUtil.isClickedTableRow.not());
            button.opacityProperty().bind(Bindings.when(TableUtil.isClickedTableRow).then(1.0).otherwise(0.4));
        });
    }

    /*public void setOnTableRowClicked(Runnable onTableRowClicked) {
        TableUtill.onTableRowClicked = onTableRowClicked;
    }*/
}
