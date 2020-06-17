package GUI.CustomElements;

import Elements.RAM;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Optional;

public class HeapDisplay extends GridPane {

    //the width of the cells, just to make it more readable
    private int maxWidth, minWidth;
    private int[] data;

    private RAM ram;

    public HeapDisplay(int[] data, RAM ram, int width, int maxWidth, int minWidth) {
        this.data = data;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
        this.ram = ram;

        int count = 0, row = 0;

        createFirstRow(width);

        while (count < data.length) {

            if (count % width == 0) {
                row++;
                startNewRow(row, width);
            }

            Cell temp = new Cell("0x", (count - (width * (row - 1))) + 1, row, maxWidth, Integer.toHexString(data[count]), true, count, this);
            getChildren().add(temp);

            count++;
        }
    }

    private void startNewRow(int row, int width) {
        Cell temp = new Cell("", 0, row, minWidth, Integer.toHexString(width * (row - 1)), false, -1, this);
        temp.setStyle("-fx-background-color: #BFDBF7");
        getChildren().add(temp);
    }

    private void createFirstRow(int width) {
        //very first cell is unique
        Cell temp = new Cell("", 0, 0, minWidth, "0x", false, -1, this);
        temp.setStyle("-fx-background-color: #BFDBF7");
        getChildren().add(temp);

        //completing the first row to look good
        for (int i = 0; i < width; i++) {
            temp = new Cell("+", i + 1, 0, maxWidth, i + "", false, -1, this);
            temp.setStyle("-fx-background-color: #BFDBF7");
            getChildren().add(temp);
        }
    }

    public void update() {
        //Count goes through the whole list, and idx is only for the cell which should be changed
        int count = 0, idx = 0;

        while (idx < data.length) {
            //update returns true if the Cell should be updated
            Cell temp = (Cell) getChildren().get(count);
            if (temp.isChangeable()) {
                //check if the value within the Cell was changed from the cell, if so then we need to retrieve the value, not change it
                temp.update(data[idx]);
                idx++;
            }
            count++;
        }
    }

    public RAM getRam() {
        return ram;
    }
}

class Cell extends Label {
    /*
     *not styling these in css, since they are my custom elements
     */
    private String prefix;
    private boolean flagChange;
    private int valueHeld;
    private String currStyle;

    public Cell(String prefix, int x, int y, double width, String value, boolean change, int nr, HeapDisplay hp) {
        this.prefix = prefix;
        this.flagChange = change;

        setText(prefix + value);

        setPrefWidth(width);
        setPrefHeight(25);

        GridPane.setColumnIndex(this, x);
        GridPane.setRowIndex(this, y);

        setAlignment(Pos.CENTER);
        setStyle("-fx-border-color: white; -fx-background-color: #E1E5F2");
        currStyle = "-fx-background-color: #E1E5F2; -fx-border-color: white";

        if (change) {
            TextInputDialog dialog = new TextInputDialog("new value");
            dialog.setTitle("Change value of a cell 0x" + (Integer.toHexString(nr)) + "; old value " + (prefix + Integer.toHexString(valueHeld)));
            dialog.setHeaderText("Format: \n HEX: \t '0xXX' \n BIN: \t '0bXX' \n DEC: \t 'XX' \n all values will be masked to 8 bit length");

            var tooltip = new Tooltip("Nr: 0x" + Integer.toHexString(nr));
            tooltip.setShowDelay(Duration.seconds(0.5));

            setTooltip(tooltip);

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        var enteredString = result.get();
                        var parsedValue = 0;
                        var wrongValue = false;

                        //determining if teh value is HEX, BIN or DEC
                        //and if it's even parsable
                        if (enteredString.matches("^0x[a-zA-Z0-9]+")) {
                            try {
                                parsedValue = Integer.decode(enteredString);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        } else if (enteredString.matches("^0b[01]+")) {
                            try {
                                parsedValue = Integer.parseInt(enteredString.split("b")[1], 2);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        } else {
                            try {
                                parsedValue = Integer.parseInt(enteredString);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        }

                        if (wrongValue) {
                            var alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Cannot recognise a HEX, BIN or DEC number in a given String.");
                            alert.setContentText("Format: \n HEX: \t '0xXX' \n BIN: \t '0bXX' \n DEC: \t 'XX'");
                            alert.showAndWait();
                        } else {
                            //mask the value;
                            valueHeld = parsedValue & 255;
                            hp.getRam().setData(nr, valueHeld);
                            hp.update();
                        }
                    }
                }
            });

            //style
            hoverProperty().addListener((obs, oldVal, newValue) -> {
                if (newValue) {
                    setStyle("-fx-background-color: #E1E5F2; -fx-border-color: #9599ff");
                } else {
                    setStyle(currStyle);
                }
            });
        }
    }

    public boolean isChangeable() {
        return flagChange;
    }

    public void update(int value) {
        //the boolean flag is here because everything is a Cell, and it should't update describing cells
        if (flagChange) {
            valueHeld = value;
            String hexValue = Integer.toHexString(value);

            if (getText().equals(prefix + hexValue)) {
                currStyle = "-fx-background-color: #E1E5F2; -fx-border-color: white";
            } else {
                setText(prefix + hexValue);
                currStyle = "-fx-background-color: #c4c7f2; -fx-border-color: white";
            }

            setStyle(currStyle);
        }

//        flagHeldValueChanged = false;
    }
}
