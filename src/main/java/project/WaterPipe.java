package project;

import javafx.application.Application;
import javafx.stage.Stage;

public class WaterPipe extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        new LevelSelection(stage).show();
    }
}
