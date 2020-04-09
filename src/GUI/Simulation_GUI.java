package GUI;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Simulation_GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("What the hell are you trying to find up here?!");

//        scene.getStylesheets().add("/fx_Interface/style-o-g.css");

        Scene scene = new Scene(new Parent() {
            @Override
            protected ObservableList<Node> getChildren() {
                return super.getChildren();
            }
        }, 900, 900);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
