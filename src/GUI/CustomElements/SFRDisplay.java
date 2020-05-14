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

        Register temp = new Register(maxWidth, minWidth);
        temp.setStyle("-fx-background-color: #9c9c9c");
        getChildren().add(temp);

        for (int i = 0; i < names.length; i++) {
            temp = new Register(names[i], values[i], maxWidth, minWidth);

            getChildren().add(temp);
        }
    }

    public void update(int[] data) {
    }
}

class Register extends HBox {

    public Register(int maxWidth, int minWidth) {
        setSpacing(5);

        Label temp = new Label("Register");

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);

        for (int i = 7; i > -1; i--) {
            temp = new Label("+" + i);

            temp.setStyle("-fx-background-color: #9c9c9c");

            temp.setAlignment(Pos.CENTER);
            temp.setPrefWidth(minWidth);
            temp.setPrefHeight(25);

            getChildren().add(temp);
        }

        temp = new Label("Value");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);
    }

    public Register(String name, int value, int maxWidth, int minWidth) {
        setSpacing(5);

        Label temp = new Label(name);

        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);

        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);

        //setting bits in reverse order
        for (int i = 7; i > -1; i--) {
            temp = new Label(BitManipulator.getBit(i, value) + "");

            temp.setStyle("-fx-background-color: #b4b4b4");

            temp.setAlignment(Pos.CENTER);
            temp.setPrefWidth(minWidth);
            temp.setPrefHeight(25);

            getChildren().add(temp);
        }

        temp = new Label(value + "");

        temp.setAlignment(Pos.CENTER);
        temp.setPrefWidth(maxWidth);
        temp.setPrefHeight(25);
        temp.setStyle("-fx-background-color: #9c9c9c");

        getChildren().add(temp);
    }

    public void update(int value, String name) {

    }
}
