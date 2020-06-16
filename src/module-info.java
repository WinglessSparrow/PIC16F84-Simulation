module PIC16F84S.Simulation {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens GUI;
    opens GUI.TableView;
    opens GUI.FXML;
}