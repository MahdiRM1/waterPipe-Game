package project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LevelSelection {
    private final Stage stage;

    public LevelSelection(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        BorderPane pane = new BorderPane();
        ImageView image = ImageFactory.createImageView("levelselection.png",
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        HBox root = new HBox(50);
        root.setAlignment(Pos.CENTER);

        HBox selectionBox = new HBox();

        Label selection = new Label("Select Level");
        selection.setFont(Font.font("Arial", FontWeight.BOLD, 100));
        selection.setTextFill(Color.SKYBLUE);
        selection.setEffect(new DropShadow(10, Color.BLACK));
        selectionBox.getChildren().add(selection);
        selectionBox.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(setButton(1), setButton(2), setButton(3));
        pane.getChildren().add(0, image);
        pane.setCenter(root);
        pane.setTop(selectionBox);

        Scene scene = new Scene(pane, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Select Level");
        stage.show();
    }

    public void startGame(int level) {
        GameUI gameUI = new GameUI(level, stage);
        Scene gameScene = new Scene(gameUI.getPane(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        stage.setTitle("WaterPipe Game");
        stage.setScene(gameScene);
    }

    private Button setButton(int level){
        Button btn = new Button("" + level);
        btn.setOnAction(event -> {
            SoundManager.playClick();
            startGame(level);
        });
        btn.setStyle(
            "-fx-background-radius: 50; " + 
            "-fx-min-width: 150px; " +    
            "-fx-min-height: 150px; " +   
            "-fx-background-color: linear-gradient(to bottom, #0f2fe4,rgb(55, 202, 247)); "  + 
            "-fx-text-fill: white; " +     
            "-fx-font-size: 75px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 0, 1);"
        );
        btn.setOnMouseEntered(e -> {
            SoundManager.playHover();
            btn.setStyle(btn.getStyle() + "-fx-background-color: linear-gradient(to bottom,rgb(55, 202, 247),  #0f2fe4);");
        });
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle() + "-fx-background-color: linear-gradient(to bottom, #0f2fe4,rgb(55, 202, 247));"));
        return btn;
    }
}