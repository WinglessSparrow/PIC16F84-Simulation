package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Simulation_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Controller centralController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("What the hell are you trying to find up here?!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartingWindow.fxml"));

        AnchorPane root = null;

        try {
            root = loader.load();
            centralController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            //it shouldn't take this path, if it does we got a problem
            root = new AnchorPane();
        }

        Scene scene = new Scene(root, 1400, 800);

        scene.getStylesheets().add("/GUI/Style-Serious.css");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
