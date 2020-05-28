package GUI;

import SimulationMain.Simulation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Simulation_GUI extends Application {

    Simulation sim;
    Thread simThread;

    public static void main(String[] args) {
        launch(args);
    }

    private Controller centralController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        initSim();

        primaryStage.setTitle("What the hell are you trying to find up here?!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/StartingWindow.fxml"));

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
        initControllerSim(loader);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                //Closes the JavaFX application and all the Other running threads
                //second one should be sufficient, but to make sure
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.show();
    }

    private void initSim() {
        sim = new Simulation();
        simThread = new Thread(sim);
        simThread.start();
    }

    private void initControllerSim(FXMLLoader loader) {
        StartingWController controller =
                loader.<StartingWController>getController();
        controller.setSim(sim);

    }


}
