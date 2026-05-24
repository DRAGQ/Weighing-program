package org.example.ivoprojekt.controller.weighing;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSpinner extends Spinner<LocalTime> {
    private enum TimePart { HOUR, MINUTE, SECOND }
    private TimePart changingTimePart = TimePart.HOUR;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int digitCount = 0;

    public LocalTimeSpinner() {
        super();
        SpinnerValueFactory<LocalTime> value = new SpinnerValueFactory<>() {


            {
                setConverter(new StringConverter<>() {
                    @Override
                    public String toString(LocalTime localTime) {
                        System.out.println("TO STRING!!! " + localTime);
                        if (localTime != null) {
                            return  localTime.format(formatter);
                        }
                        return LocalTime.now().format(formatter);
                    }

                    @Override
                    public LocalTime fromString(String localTimeString) {
                        if (localTimeString != null) {
                            String[] parts = localTimeString.split(":");

                            int h = parts.length > 0 && !parts[0].isEmpty() ? Integer.parseInt(parts[0]) : 0;
                            int m = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) : 0;
                            int sec = parts.length > 2 && !parts[2].isEmpty() ? Integer.parseInt(parts[2]) : 0;

                            return LocalTime.of(h, m, sec);
                        }
                        return LocalTime.now();
                    }
                });
            }

            @Override
            public void decrement(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime value = getValue();
                    if (changingTimePart == TimePart.HOUR) {
                        setValue(value.minusHours(steps));
                    } else if (changingTimePart == TimePart.MINUTE) {
                        setValue(value.minusMinutes(steps));
                    } else {
                        setValue(value.minusSeconds(steps));
                    }
                    setSelectedRange();
                }
            }

            @Override
            public void increment(int steps) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime value = getValue();
                    if (changingTimePart == TimePart.HOUR) {
                        setValue(value.plusHours(steps));
                    } else if (changingTimePart == TimePart.MINUTE) {
                        setValue(value.plusMinutes(steps));
                    } else {
                        setValue(value.plusSeconds(steps));
                    }
                    setSelectedRange();
                }
            }
        };

        createFormatter();
        setValueFactory(value);
        setEditable(true);
        this.getEditor().setCursor(Cursor.DEFAULT);

        setupClickListening();

        this.getValueFactory().setValue(LocalTime.now());


        getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String typed = event.getCharacter();

            if (!typed.matches("[0-9]")) {
                event.consume();
                return;
            }
            event.consume();

            String[] parts = getCurrentParts();
            final TimePart partBeforeChange = changingTimePart;

            switch(changingTimePart) {
                case HOUR -> {
                    String current = parts[0].trim();

                    if (digitCount == 0 || digitCount == 2) {
                        parts[0] = typed;
                        digitCount = 1;

                    } else if (digitCount == 1) {
                        String combined = current + typed;
                        int val = Integer.parseInt(combined);

                        if (val <= 23) {
                            parts[0] = combined;
                            digitCount = 2;
                        } else {
                            parts[0] = typed;
                        }

                    }
                } case MINUTE -> {
                    String current = parts[1].trim();

                    if (digitCount == 0 || digitCount == 2) {
                        parts[1] = typed;
                        digitCount = 1;

                    } else if (digitCount == 1) {
                        String combined = current + typed;
                        int val = Integer.parseInt(combined);

                        if (val <= 59) {
                            parts[1] = combined;
                            digitCount = 2;
                        } else {
                            parts[1] = typed;
                        }

                    }
                } case SECOND -> {
                    String current = parts[2].trim();

                    if (digitCount == 0 || digitCount == 2) {
                        parts[2] = typed;
                        digitCount = 1;

                    } else if (digitCount == 1) {
                        String combined = current + typed;
                        int val = Integer.parseInt(combined);

                        if (val <= 59) {
                            parts[2] = combined;
                            digitCount = 2;
                        } else {
                            parts[2] = typed;
                        }

                    }
                }
            }

            String newTextTime = parts[0] + ":" + parts[1] + ":" + parts[2];

            Platform.runLater(() -> {
                getEditor().setText(newTextTime);
                switch (partBeforeChange) {
                    case HOUR   -> getEditor().selectRange(0, digitCount);
                    case MINUTE -> getEditor().selectRange(3, 3 + digitCount);
                    case SECOND -> getEditor().selectRange(6, 6 + digitCount);
                };
            });
        });

        getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode pressed = event.getCode();

            if (pressed == KeyCode.BACK_SPACE) {
                event.consume();
            } else if (pressed == KeyCode.RIGHT) {
                switch (changingTimePart) {
                    case HOUR -> changingTimePart = TimePart.MINUTE;
                    case MINUTE -> changingTimePart = TimePart.SECOND;
                    case SECOND -> changingTimePart = TimePart.HOUR;
                }
                Platform.runLater(this::setSelectedRange);
                this.commitValue();
                getEditor().setText(getValueFactory().getConverter().toString(getValue()));

            } else if (pressed == KeyCode.LEFT) {
                switch (changingTimePart) {
                    case HOUR -> changingTimePart = TimePart.SECOND;
                    case MINUTE -> changingTimePart = TimePart.HOUR;
                    case SECOND -> changingTimePart = TimePart.MINUTE;
                }
                Platform.runLater(this::setSelectedRange);
                this.commitValue();
                getEditor().setText(getValueFactory().getConverter().toString(getValue()));
            }
        });

        getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                try {
                    // Normalizuj pri strate focusu
                    LocalTime localTime = getValueFactory().getConverter().fromString(getEditor().getText());
                    getEditor().setText(localTime.format(formatter));
                } catch (Exception e) {
                    getEditor().setText(getValueFactory().getValue().format(formatter));
                }
            }
        });

        getEditor().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            this.commitValue();
            getEditor().setText(getValueFactory().getConverter().toString(getValue()));
        });
    }



    private String[] getCurrentParts() {
        String text = getEditor().getText();
        String[] parts = text.split(":");
        if (parts.length < 3) return new String[]{"", "", ""};
        return parts;
    }

    public void createFormatter() {

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d{0,2}(:\\d{0,2}){0,2}")) {
                return change;
            }
            return null;

        });

        this.getEditor().setTextFormatter(formatter);
    }

    private void setupClickListening() {
        this.getEditor().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setPosition();
            }
        });
    }

    private void setPosition() {
        int position = getEditor().getCaretPosition();
        digitCount = 0;
        if (position < 3) {
            changingTimePart = TimePart.HOUR;
            getEditor().selectRange(0, 2);
        } else if (position < 6) {
            changingTimePart = TimePart.MINUTE;
            getEditor().selectRange(3, 5);
        } else {
            changingTimePart = TimePart.SECOND;
            getEditor().selectRange(6, 8);
        }
    }

    private void setSelectedRange() {
        digitCount = 0;
        if (changingTimePart == TimePart.HOUR) {
            getEditor().selectRange(0, 2);
        } else if (changingTimePart == TimePart.MINUTE) {
            getEditor().selectRange(3, 5);
        } else {
            getEditor().selectRange(6, 8);
        }
    }
}
