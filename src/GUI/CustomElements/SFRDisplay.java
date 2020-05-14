package GUI.CustomElements;

import Helpers.BitManipulator;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SFRDisplay extends VBox {

    int maxWidth, minWidth;

    public SFRDisplay(String[] names, int[] values, int maxWidth, int minWidth) {
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;

        setSpacing(5);

        //first row
        Register temp = new Register(maxWidth, minWidth, false);
        temp.setStyle("-fx-background-color: #9c9c9c");
        getChildren().add(temp);

        //all the other rows
        for (int i = 0; i < names.length; i++) {
            temp = new Register(names[i], values[i], maxWidth, minWidth, true);

            getChildren().add(temp);
        }
    }

    public void update(int[] data) {

        for (int i = 0; i < data.length; i++) {
            Register temp = (Register) getChildren().get(i);
            temp.update(data[i]);
        }
    }
}

class Register extends HBox {

    private Label lbl_value;
    private Label[] lbls_bin;

    private boolean write;

    public Register(int maxWidth, int minWidth, boolean write) {
        this.write = write;

        setSpacing(5);

        Label temp = new Label("Register");

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);

        for (int i = 7; i > -1; i--) {
            lbls_bin[i] = new Label("+" + i);

            lbls_bin[i].setStyle("-fx-background-color: #9c9c9c");

            lbls_bin[i].setAlignment(Pos.CENTER);
            lbls_bin[i].setPrefWidth(minWidth);
            lbls_bin[i].setPrefHeight(25);

            getChildren().add(lbls_bin[i]);
        }

        temp = new Label("Value");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);
    }

    public Register(String name, int value, int maxWidth, int minWidth, boolean write) {
        setSpacing(5);

        Label temp = new Label(name);

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);

        //setting bits in reverse order
        for (int i = 7; i > -1; i--) {
            lbls_bin[i] = new Label(BitManipulator.getBit(i, value) + "");

            lbls_bin[i].setStyle("-fx-background-color: #b4b4b4");

            lbls_bin[i].setAlignment(Pos.CENTER);
            lbls_bin[i].setPrefWidth(minWidth);
            lbls_bin[i].setPrefHeight(25);

            getChildren().add(lbls_bin[i]);
        }

        temp = new Label(value + "");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);
    }

    private void setBinary(int value) {
        for (int i = 7; i > -1; i--) {
            lbls_bin[i].setText(BitManipulator.getBit(i, value) + "");
        }
    }

    public boolean isWrite() {
        return write;
    }

    public void update(int value) {
        lbl_value.setText(value + "");
        setBinary(value);
    }
}
