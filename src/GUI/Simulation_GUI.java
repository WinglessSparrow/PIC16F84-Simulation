package GUI;

import Simulation.Simulation;
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

    private Simulation sim;
    private String path;

    private Controller centralController;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("PIC 16F84 Simulator");

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
                //second one should be sufficient, but to make sure first kill FX thread
                Platform.exit();
                try {
                    sim.cleanUp();
                } catch (NullPointerException ignored) {
                }
                System.out.println("see you space cowboy");
                System.exit(0);
            }
        });

        primaryStage.show();
    }


    //Gives  simGUI to all controllers
    private void initControllerSim(FXMLLoader loader) {
        //StartingWController controller  = loader.getController();
        centralController.setSimGUI(this);
    }

    //Loads a file and restarts the backend
    public void loadFile(String path) {
        this.path = path;
        powerReset();
    }

    //restarts the backend
    public void powerReset() {
        //garbage collection is cool and all, but I don't trust it enough
        if (sim != null) {
            sim.killThread();
        }

        sim = new Simulation(path, (StartingWController) centralController);
        Thread simThread = new Thread(sim);
        sim.updateGUI();
        simThread.start();

    }

    public void update() {
        centralController.update();
    }


    public Simulation getSim() {
        return sim;
    }
}
