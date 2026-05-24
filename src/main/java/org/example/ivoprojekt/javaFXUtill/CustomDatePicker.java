package org.example.ivoprojekt.javaFXUtill;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.ivoprojekt.controller.weighing.LocalTimeSpinner;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class CustomDatePicker extends DatePicker {
    private enum DatePart { DAY, MONTH, YEAR }
    private DatePart changingDatePart = DatePart.DAY;
    private int digitCount;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustomDatePicker() {
        super();

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (Year.from(item).getValue() < 2000) {
                            setDisable(true);
                        }
//                        if(MonthDay.from(item).equals(MonthDay.of(9, 25))) {
//                            setTooltip(new Tooltip("Happy Birthday!"));
//                            setStyle("-fx-background-color: green;");
//                        }
//                        if (item.equals(LocalDate.now().plusDays(1))) {
//                            setDisable(true);
//                        }
                    }
                };
            }
        };
        this.setDayCellFactory(dayCellFactory);

        this.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    System.out.println("TO STRING!!!" + date);
                    if (date.getYear() < 2000) {
                        return formatter.format(LocalDate.of(2000, date.getMonth(), date.getDayOfMonth()));
                    }
                    return formatter.format(date);
                }
                return "";
            }
            @Override
            public LocalDate fromString(String string) {
                String[] parts = string.split("/");

                int day = parts.length > 0 && !parts[0].isEmpty() ? Integer.parseInt(parts[0]) : 10;
                int month = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) : 10;
                int year = parts.length > 2 && !parts[2].isEmpty() ? Integer.parseInt(parts[2]) : 2000;

                if (day < 1) day = 10;
                if (month < 1) month = 10;

                System.out.println("day: " + day + ", month: " + month + ", year: " + year);
                LocalDate date = LocalDate.of(year, month, day);

                if (year < 2000) {
                    return LocalDate.of(2000, month, day);
                }
                return date;
            }
        });

        createFormatter();
        setupClickListening();

        this.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String typed = event.getCharacter();

            if (!typed.matches("[0-9]")) {
                event.consume();
                return;
            }

            String[] parts = getEditor().getText().split("/");
            //final DatePart partBeforeChange = changingDatePart;
            if (parts.length < 3) return;

            switch (changingDatePart) {
                case DAY -> {
                    String current = parts[0];
                    this.digitCount = current.length();

                    if (this.digitCount == 0 || this.digitCount == 2) {
                        parts[0] = typed;
                        this.digitCount = 1;
                    } else if (this.digitCount == 1) {
                        String combinedValue = current + typed;
                        int value = Integer.parseInt(combinedValue);

                        if (value <= 31) {
                            parts[0] = combinedValue;
                            this.digitCount = 2;
                        } else {
                            parts[0] = typed;
                        }
                    }
                }
                case MONTH -> {
                    String current = parts[1];
                    this.digitCount = current.length();

                    if (this.digitCount == 0 || this.digitCount == 2) {
                        parts[1] = typed;
                        this.digitCount = 1;
                    }
                    else if (this.digitCount == 1) {
                        String combinedValue = current + typed;
                        int value = Integer.parseInt(combinedValue);

                        if (value <= 12) {
                            parts[1] = combinedValue;
                            this.digitCount = 2;
                        } else {
                            parts[1] = typed;
                        }
                    }
                }
                case YEAR -> {
                    String current = parts[2];
                    this.digitCount = current.length();

                    if (this.digitCount == 0 || this.digitCount == 4) {
                        parts[2] = typed;
                        this.digitCount = 1;
                    } else {
                        String combinedValue = current + typed;
                        int value = Integer.parseInt(combinedValue);

                        if (value <= 4000) {
                            parts[2] = combinedValue;
                            this.digitCount = combinedValue.length();
                        } else {
                            parts[2] = typed;
                        }
                    }
                }
            }

            String newTextTime = parts[0] + "/" + parts[1] + "/" + parts[2];

            Platform.runLater(() -> {
                getEditor().setText(newTextTime);
                switch (changingDatePart) {
                    case DAY   -> getEditor().selectRange(0, digitCount);
                    case MONTH -> getEditor().selectRange(3, 3 + digitCount);
                    case YEAR -> getEditor().selectRange(6, 6 + digitCount);
                };
            });
        });

        getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                try {
                    LocalDate localDate = getConverter().fromString(getEditor().getText());
                    getEditor().setText(localDate.format(formatter));
                } catch (Exception e) {
                    getEditor().setText(getValue().format(formatter));
                }
            }
        });

        this.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode pressed = event.getCode();

            if (pressed == KeyCode.BACK_SPACE) {
                event.consume();
            } else if (pressed == KeyCode.RIGHT) {
                switch (changingDatePart) {
                    case DAY -> changingDatePart = DatePart.MONTH;
                    case MONTH -> changingDatePart = DatePart.YEAR;
                    case YEAR -> changingDatePart = DatePart.DAY;
                }
                Platform.runLater(this::setSelectedRange);
                this.commitValue();

                LocalDate localDate = getConverter().fromString(getEditor().getText());
                getEditor().setText(localDate.format(formatter));

            } else if (pressed == KeyCode.LEFT) {
                switch (changingDatePart) {
                    case DAY -> changingDatePart = DatePart.YEAR;
                    case MONTH -> changingDatePart = DatePart.DAY;
                    case YEAR -> changingDatePart = DatePart.MONTH;
                }
                Platform.runLater(this::setSelectedRange);
                this.commitValue();

                LocalDate localDate = getConverter().fromString(getEditor().getText());
                getEditor().setText(localDate.format(formatter));
            }
        });

        this.getEditor().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setPosition();
            }
        });

        this.getEditor().setCursor(Cursor.DEFAULT);

    }

    private void createFormatter() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
           String newText = change.getControlNewText();
           if (newText.matches("(\\d{0,2}/){0,2}\\d{0,4}")) {
               return change;
           }
           return null;
        });

        this.getEditor().setTextFormatter(formatter);
    }

    private void setupClickListening() {
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            this.commitValue();
            LocalDate localDate = getConverter().fromString(getEditor().getText());
            getEditor().setText(localDate.format(formatter));
        });

        this.getEditor().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setPosition();
            }
        });
    }

    /*private String[] getCurrentParts() {d
        String text = getEditor().getText();
        String[] parts = text.split(":");
        if (parts.length < 3) return new String[]{"", "", ""};
        return parts;
    }*/

    private void setPosition() {
        int position = getEditor().getCaretPosition();
        System.out.println("position: " + position);
        if (position < 3) {
            changingDatePart = DatePart.DAY;
            getEditor().selectRange(0, 2);
        } else if (position < 6) {
            System.out.println("LALALALA");
            changingDatePart = DatePart.MONTH;
            getEditor().selectRange(3, 5);
        } else {
            changingDatePart = DatePart.YEAR;
            getEditor().selectRange(6, 10);
        }
    }

    private void setSelectedRange() {
        if (changingDatePart == DatePart.DAY) {
            getEditor().selectRange(0, 2);
        } else if (changingDatePart == DatePart.MONTH) {
            getEditor().selectRange(3, 5);
        } else {
            getEditor().selectRange(6, 10);
        }
    }

}
