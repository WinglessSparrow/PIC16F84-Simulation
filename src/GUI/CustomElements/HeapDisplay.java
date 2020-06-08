package GUI.CustomElements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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

            Cell temp = new Cell("0x", (count - (width * (row - 1))) + 1, row, maxWidth, Integer.toHexString(data[count]), true);
            getChildren().add(temp);

            count++;
        }
    }

    private void startNewRow(int row, int width) {
        Cell temp = new Cell("", 0, row, minWidth, Integer.toHexString(width * (row - 1)), false);
        temp.setStyle("-fx-background-color: #BFDBF7");
        getChildren().add(temp);
    }

    private void createFirstRow(int width) {
        //very first cell is unique
        Cell temp = new Cell("", 0, 0, minWidth, "0x", false);
        temp.setStyle("-fx-background-color: #BFDBF7");
        getChildren().add(temp);

        //completing the first row to look good
        for (int i = 0; i < width; i++) {
            temp = new Cell("+", i + 1, 0, maxWidth, i + "", false);
            temp.setStyle("-fx-background-color: #BFDBF7");
            getChildren().add(temp);
        }
    }

    public void update() {
        //Count goes through the whole list, and idx is only for the cell which should be changed
        int count = 0, idx = 0;

        while (idx < data.length) {
            //update returns true if the Cell should be updated
            if (((Cell) getChildren().get(count)).update(Integer.toHexString(data[idx]))) {
                idx++;
            }
            count++;
        }
    }
}

class Cell extends Label {

    private String prefix;
    private boolean change;

    public Cell(String prefix, int x, int y, double width, String value, boolean change) {
        this.prefix = prefix;
        this.change = change;

        setText(prefix + value);

        setPrefWidth(width);
        setPrefHeight(25);

        GridPane.setColumnIndex(this, x);
        GridPane.setRowIndex(this, y);

        setAlignment(Pos.CENTER);
        setStyle("-fx-border-color: white; -fx-background-color: #E1E5F2");

    }

    public boolean update(String value) {
        //the boolean flag is here because everything is a Cell, and it should't update describing cells
        if (change) {
            if (getText().equals(prefix + value)) {
                setStyle("-fx-background-color: #E1E5F2; -fx-border-color: white");
            } else {
                setText(prefix + value);
                setStyle("-fx-background-color: #c4c7f2; -fx-border-color: white");
            }

        }
        return change;
    }
}
