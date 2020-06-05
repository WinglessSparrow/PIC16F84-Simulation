package GUI.CustomElements;

import Elements.RAM;
import Helpers.BitManipulator;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SFRDisplay extends VBox {

    private RAM ram;

    public SFRDisplay(int maxWidth, int minWidth) {

        String[] names = new String[]{
                "TMR0", "PCL", "STATUS", "FSR", "PORT-A", "PORT-B", "EEDATA", "EEADR", "PCLATH",
                "INTCON", "OPTION", "TRISA", "TRISB", "EECON1", "EECON2"
        };

        setSpacing(5);
        //first row
        Register temp = new Register(maxWidth, minWidth, false);
        temp.setStyle("-fx-background-color: #bfdbf7");
        getChildren().add(temp);

        //all the other rows
        for (String name : names) {
            temp = new Register(name, 0, maxWidth, minWidth, true);

            getChildren().add(temp);
        }
    }

    public void setData(RAM ram) {
        this.ram = ram;
        update();
    }


    public void update() {
        int[] data = ram.getSfrData();

        //iterates through data
        int count = 0;

        for (int i = 0; i < getChildren().size(); i++) {
            //getting the instance
            Register temp = (Register) getChildren().get(i);
            //check if it's the first row
            if (temp.isWrite()) {
                try {
                    temp.update(data[count]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    //no value, by the full operation should happen
                    //but might happen so just to make sure it's ok
                    temp.update(0);
                }
                count++;
            }
        }
    }
}

class Register extends HBox {

    private Label lbl_value;
    private Label[] lbls_bin = new Label[8];

    private boolean write;

    public Register(int maxWidth, int minWidth, boolean write) {
        this.write = write;
        setSpacing(5);

        Label temp = new Label("Register");

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #BFDBF7");

        getChildren().add(temp);

        for (int i = 7; i > -1; i--) {
            lbls_bin[i] = new Label("+" + i);

            lbls_bin[i].setStyle("-fx-background-color: #BFDBF7");

            lbls_bin[i].setAlignment(Pos.CENTER);
            lbls_bin[i].setPrefWidth(minWidth);
            lbls_bin[i].setPrefHeight(25);

            getChildren().add(lbls_bin[i]);
        }

        temp = new Label("Value");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #BFDBF7");

        getChildren().add(temp);
    }

    public Register(String name, int value, int maxWidth, int minWidth, boolean write) {
        this.write = write;
        setSpacing(5);

        Label temp = new Label(name);

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #BFDBF7");

        getChildren().add(temp);

        //setting bits in reverse order
        for (int i = 7; i > -1; i--) {
            lbls_bin[i] = new Label(BitManipulator.getBit(i, value) + "");

            lbls_bin[i].setStyle("-fx-background-color: #E1E5F2");

            lbls_bin[i].setAlignment(Pos.CENTER);
            lbls_bin[i].setPrefWidth(minWidth);
            lbls_bin[i].setPrefHeight(25);

            getChildren().add(lbls_bin[i]);
        }

        temp = new Label(value + "");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #BFDBF7");

        lbl_value = temp;

        getChildren().add(temp);
    }

    private void setBinary(int value) {
        for (int i = 7; i > -1; i--) {
            String valStr = BitManipulator.getBit(i, value) + "";
            if (lbls_bin[i].getText().equals(valStr)) {
                lbls_bin[i].setStyle("-fx-background-color: #E1E5F2");
            } else {
                lbls_bin[i].setText(valStr);

                lbls_bin[i].setStyle("-fx-background-color: #c4c7f2");
            }
        }
    }

    public boolean isWrite() {
        return write;
    }

    public void update(int value) {
        String valStr = value + "";
        if (lbl_value.getText().equals(valStr)) {
            lbl_value.setStyle("-fx-background-color: #E1E5F2");
        } else {
            lbl_value.setStyle("-fx-background-color: #c4c7f2");
            lbl_value.setText(valStr);
        }
        setBinary(value);
    }
}
