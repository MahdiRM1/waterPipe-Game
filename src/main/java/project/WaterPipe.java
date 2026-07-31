package project;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class WaterPipe extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")))
        );
        stage.setTitle("WaterPipe");
        new LevelSelection(stage).show();
    }
}
