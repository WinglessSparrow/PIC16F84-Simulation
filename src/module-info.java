module PIC16F84.Simulation {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens GUI;
    opens GUI.TableView;
    opens GUI.FXML;
}