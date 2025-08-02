package project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ScoreBoard{
    private final VBox panel;
    private final Label moveLabel;
    private final Label timeLabel;
    private final Label undoLabel;
    private int time;
    private int move;
    private int undo;
    private Timeline timer;
    private final GameUI gameUI;

    public ScoreBoard(int level, GameUI gameUI){
        this.gameUI = gameUI;
        undo = 2;
        Font font = new Font("Arial Bold", 30);
        timeLabel = new Label();
        initializeTime(level, font);
        move = 5 + 10*level;
        Label levelLabel = new Label("Level = " + level); moveLabel = new Label("Moves = " + move);
        levelLabel.setFont(font); moveLabel.setFont(font);
        levelLabel.setTextFill(Color.WHITE); moveLabel.setTextFill(Color.WHITE);
        undoLabel = new Label("Undo: " + undo);
        undoLabel.setFont(font); undoLabel.setTextFill(Color.WHITE);
        panel = new VBox(10, levelLabel, moveLabel, timeLabel, undoLabel);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setStyle(
        "-fx-background-color:rgb(0, 154, 201); " +
        "-fx-max-width: 300px; " +    
        "-fx-max-height: 300px; " +
        "-fx-background-radius: 15px; " +
        "-fx-padding: 20px; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);"
        );
    }

    //----------------------------------------------------------
    //---------------------Initializers-------------------------
    //----------------------------------------------------------

    private void initializeTime(int level, Font font){
        time = 1200 + level * 1000;
        timeLabel.setTextFill(Color.WHITE);
        timer = new Timeline(
            new KeyFrame(Duration.millis(10), 
                e -> {
                    time--;
                    int sec = time / 100;
                    if(time <= 500) timeLabel.setTextFill(Color.RED);
                    String timeFormatted = String.format("Time %02d':%02d\".%02d", sec / 60, sec % 60, time % 100);
                    timeLabel.setText(timeFormatted);
                    if (time <= 0) {
                        Platform.runLater(() -> gameUI.showLoseMessage("Your time is up!"));
                    }
                })
            );
        timer.setCycleCount(1200 + level * 1000);
        timeLabel.setFont(font);
        timer.play();
    }

    //----------------------------------------------------------
    //-----------------------logic------------------------------
    //----------------------------------------------------------

    public void move(){
        move--;
        if(move < 5) moveLabel.setTextFill(Color.RED);
        moveLabel.setText("Moves = " + move);
    }

    public void undo(){
        undo--;
        move += 2;
        if(move >= 5) moveLabel.setTextFill(Color.WHITE);
        moveLabel.setText("Moves = " + move);
        if(undo == 0) undoLabel.setTextFill(Color.RED);
        undoLabel.setText("Undo: " + undo);
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public void stoptime(){
        timer.stop();
    }

    public VBox getpanel(){
        return panel;
    }

    public int getmove(){
        return move;
    }

    public int remainUndo(){
        return undo;
    }
}