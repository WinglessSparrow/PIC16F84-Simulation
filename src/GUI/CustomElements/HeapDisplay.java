package GUI.CustomElements;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class HeapDisplay extends GridPane {

    //the width of the cells, just to make it more readable
    private int maxWidth, minWidth;
    private int[] data;

    public HeapDisplay(int[] data, int width, int maxWidth, int minWidth) {
        this.data = data;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
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
                if (temp.isChanged()) {
                    data[idx] = temp.getValueHeld();
                }

                temp.update(data[idx]);
                idx++;
            }
            count++;
        }
    }
}

class Cell extends Label {
    /*
     *not styling these in css, since they are my custom elements
     */
    private String prefix;
    private boolean flagChange;
    private boolean flagHeldValueChanged;
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

            setTooltip(new Tooltip("Nr: 0x" + Integer.toHexString(nr)));

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        String enterd = result.get();
                        int parsedValue = 0;
                        boolean wrongValue = false;

                        //determining if teh value is HEX, BIN or DEC
                        //and if it's even parsable
                        if (enterd.matches("^0x[a-zA-Z0-9]+")) {
                            try {
                                parsedValue = Integer.decode(enterd);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        } else if (enterd.matches("^0b[01]+")) {
                            try {
                                parsedValue = Integer.parseInt(enterd, 2);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        } else {
                            try {
                                parsedValue = Integer.parseInt(enterd);
                            } catch (NumberFormatException e) {
                                wrongValue = true;
                            }
                        }

                        if (wrongValue) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Cannot recognise a HEX, BIN or DEC number in a given String.");
                            alert.setContentText("Format: \n HEX: \t '0xXX' \n BIN: \t '0bXX' \n DEC: \t 'XX'");
                            alert.showAndWait();
                        } else {
                            //mask the value;
                            valueHeld = parsedValue & 255;
                            flagHeldValueChanged = true;
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

    public boolean isChanged() {
        return flagHeldValueChanged;
    }

    public int getValueHeld() {
        return valueHeld;
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

        flagHeldValueChanged = false;
    }
}
