package project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.CellPipe.Rotation;

public class GameUI {

    private final Board board;
    private int[][] boardValue;
    private final int ROWS;
    private final int COLS;
    private final int width = (int) Screen.getPrimary().getBounds().getWidth();
    private final int height = (int) Screen.getPrimary().getBounds().getHeight();
    private final BorderPane gamePane = new BorderPane();;
    private final GridPane gridPane = new GridPane();
    private Stage stage;
    private int level;
    private final Move[] move;
    private final ScoreBoard scoreboard;
    private final VBox buttonsBox;


    public GameUI(int level, Stage stage) {

        move = new Move[2];
        move[0] = new Move();
        move[1] = new Move();
        buttonsBox = new VBox(15);
        this.level = level;
        this.stage = stage;
        scoreboard = new ScoreBoard(level, this);

        switch (level) {
            case 1:{
                ROWS = 5; COLS = 5;
                boardValue = new int[][]{
                {7, 0, 0, 0, 0},
                {3, 2, 2, 5, 0},
                {4, 2, 2, 6, 0},
                {1, 0, 0, 0, 0},
                {3, 2, 2, 2, 8}
                };
                board = new Board(ROWS, COLS, boardValue, this, scoreboard);      
                break; 
            }

            case 2:{
                ROWS = 7; COLS = 7;
                boardValue = new int[][]{
                {7, 0, 0, 0, 0, 0, 0}, 
                {1, 0, 0, 0, 0, 0, 0}, 
                {1, 0, 0, 0, 0, 0, 0},
                {3, 2, 2, 2, 5, 0, 0}, 
                {0, 4, 2, 2, 6, 0, 0},
                {0, 1, 0, 0, 0, 0 ,0},
                {0, 3, 2, 2, 2, 2, 8}
                };
                board = new Board(ROWS, COLS, boardValue, this, scoreboard);
                break;
            }
            default:{
                ROWS = 8; COLS = 8;
                boardValue = new int[][]{
                {0, 7, 0, 3, 2, 2, 3, 0}, 
                {4, 3, 2, 5, 0, 0, 1, 0}, 
                {1, 0, 0, 2, 0, 0, 1, 0}, 
                {3, 2, 2, 9, 3, 2, 3, 0}, 
                {0, 0, 3, 3, 2, 0, 0, 0},
                {0, 0, 2, 0, 2, 0 ,0, 0},
                {0, 0, 2, 0, 3, 1, 1, 8},
                {0, 0, 3, 1, 3, 0, 0, 0}
                };
                board = new Board(ROWS, COLS, boardValue, this, scoreboard);
                break;
            }
        }
        initializeUi(gridPane);
        if(winCheck(board.getFirstPipeRow(), board.getFirstPipeCol(), board.getFirstPipeRow() + 1, board.getFirstPipeCol())) new LevelSelection(stage).startGame(level);;

    }

    //----------------------------------------------------------
    //--------------------Initializers--------------------------
    //----------------------------------------------------------
    
    private void initializeUi(GridPane gridPane) {

        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                gridPane.add(board.getCell(row, col).getButton(), col, row);
            }
        }

        ImageView backGround = new ImageView(new Image("file:pictures/background.png"));
        backGround.setFitWidth(width);
        backGround.setFitHeight(height);
        gamePane.getChildren().add(0, backGround);
        gamePane.setCenter(gridPane);
        gamePane.setRight(scoreboard.getpanel());
        InitializeButtons();
    }


    public void InitializeButtons(){
        buttonsBox.setAlignment(Pos.TOP_CENTER);

        Button homebtn = setButton("home", 100, 100, 0);
        Button settingbtn = setButton("setting", 100, 100, 0);
        Button restartbtn = setButton("restart", 100, 100, 0);
        
        homebtn.setOnAction(event -> new LevelSelection(stage).show());
        restartbtn.setOnAction(event -> {
            scoreboard.stoptime();
            new LevelSelection(stage).startGame(level);
        }
        );

        buttonsBox.getChildren().addAll(homebtn, restartbtn, settingbtn, new Button());
        UndoBtn(false);
        gamePane.setLeft(buttonsBox);
    }


    public void UndoBtn(Boolean bool){
        
        Button undobtn = new Button();
        ImageView image = new ImageView(new Image("file:pictures/undo.png"));
        image.setFitHeight(100);
        image.setFitWidth(100);
        undobtn.setGraphic(image);

        if(!bool)undobtn.setStyle(
            "-fx-background-radius: 100; " + 
            "-fx-min-width: 100px; " +    
            "-fx-min-height: 100px; " +   
            "-fx-background-color: linear-gradient(to bottom,rgb(230, 23, 9),rgb(255, 26, 26)); "  + 
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 0, 1);");
        else undobtn = setButton("undo", 100, 100, 0);

        undobtn.setOnAction(event -> Undo());
        buttonsBox.getChildren().remove(3);
        buttonsBox.getChildren().add(undobtn);
    }

    //----------------------------------------------------------
    //---------------------gamelogic----------------------------
    //----------------------------------------------------------

    private void Undo(){

        if(move[1].Rotation() == null || scoreboard.remainUndo() < 1) return;

        if(move[1].Rotation() == Rotation.Clockwise) board.getCell(move[1].Row(), move[1].Col()).rotate(Rotation.Counterclockwise);
        else board.getCell(move[1].Row(), move[1].Col()).rotate(Rotation.Clockwise);
        scoreboard.undo();
        move[1].set(move[0].Row(), move[0].Col(), move[0].Rotation());
        move[0].set(-1, -1, null);
        if(scoreboard.remainUndo() < 1 || move[1].Rotation() == null) UndoBtn(false);
    }

    private Button setButton(String text, int width, int height, int fontsize){
        Button btn = new Button();
        if(fontsize == 0) {
            ImageView image = new ImageView(new Image("file:pictures/" + text + ".png"));
            image.setFitHeight(height);
            image.setFitWidth(width);
            btn.setGraphic(image);
        }
        else btn.setText(text);
        btn.setStyle(String.format(
            "-fx-background-radius: 100; " + 
            "-fx-min-width: %dpx; " +    
            "-fx-min-height: %dpx; " +   
            "-fx-background-color: linear-gradient(to bottom, #0f2fe4, rgb(55, 202, 247)); "  + 
            "-fx-text-fill: white; " +     
            "-fx-font-size: %dpx; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 0, 1);" ,  
            width, height, fontsize
            ));

        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-background-color: linear-gradient(to bottom, rgb(55, 202, 247), #0f2fe4);"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle() + "-fx-background-color: linear-gradient(to bottom, #0f2fe4,rgb(55, 202, 247));"));

        return btn;
    }



    public void setMove(int row, int col, Rotation rotation){
        move[0].set(move[1].Row(), move[1].Col(), move[1].Rotation());
        move[1].set(row, col, rotation);
    }



    public boolean winCheck(int row1, int col1, int row2, int col2){
        if(row2 < 0 || row2 >= ROWS || col2 < 0 || col2 >= COLS) return false;

        if(row2 - row1 == 1) {
            switch (board.getCell(row2, col2).getType()) {
                case 1:
                case 9: return winCheck(row2, col2, row2 + 1, col2);
                case 3: return winCheck(row2, col2, row2, col2 + 1);
                case 6: return winCheck(row2, col2, row2, col2 - 1);
                default: return false;
            }
        }

        else if(row2 - row1 == -1){
            switch (board.getCell(row2, col2).getType()) {
                case 1:
                case 9: return winCheck(row2, col2, row2 - 1, col2);
                case 4: return winCheck(row2, col2, row2, col2 + 1);
                case 5: return winCheck(row2, col2, row2, col2 - 1);
                default: return false;
            }
        }

        else if(col2 - col1 == 1){
            switch (board.getCell(row2, col2).getType()) {
                case 2:
                case 9: return winCheck(row2, col2, row2, col2 + 1);
                case 5: return winCheck(row2, col2, row2 + 1, col2);
                case 6: return winCheck(row2, col2, row2 - 1, col2);
                case 8: return true;
                default: return false;
            }
        }
        
        else {
            switch (board.getCell(row2, col2).getType()) {
                case 2:
                case 9: return winCheck(row2, col2, row2, col2 - 1);
                case 3: return winCheck(row2, col2, row2 - 1, col2);
                case 4: return winCheck(row2, col2, row2 + 1, col2);
                default: return false;
            }
        }
    }


    //----------------------------------------------------------
    //-----------------------messages---------------------------
    //----------------------------------------------------------

    public void showWinMessage(){
        scoreboard.stoptime();
        
        Label winLabel = new Label("You Win!");
        winLabel.setTextFill(Color.GREEN);
        winLabel.setFont(Font.font("Arial", FontWeight.BOLD, 100));
        winLabel.setEffect(new DropShadow(10, Color.BLACK));

        Button backToMenu = setButton("Back to menu", 350, 100, 30);
        backToMenu.setOnAction(event -> new LevelSelection(stage).show());

        Button nextLevel = setButton("Next level", 350, 100, 30);
        nextLevel.setOnAction(event -> new LevelSelection(stage).startGame(++level));

        VBox winBox = new VBox(10);
        if(level < 3) winBox.getChildren().addAll(winLabel, backToMenu, nextLevel);
        else winBox.getChildren().addAll(winLabel, backToMenu);
        winBox.setAlignment(Pos.CENTER);
        winBox.setStyle("-fx-background-color: rgba(174, 255, 174, 0.7);");
        winBox.setPrefSize(width, height);

        StackPane winPane = new StackPane();
        winPane.getChildren().addAll(gamePane, winBox);
        Scene scene = new Scene(winPane, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void showLoseMessage(String message){    
        Label loseLabel = new Label("You Lose!");
        loseLabel.setTextFill(Color.RED);
        loseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 100));
        loseLabel.setEffect(new DropShadow(10, Color.BLACK));

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setFont(Font.font("Arial", 50));
        messageLabel.setEffect(new DropShadow(5, Color.BLACK));
        

        Button backToMenu = setButton("Back to menu", 350, 100, 30);
        backToMenu.setOnAction(event -> new LevelSelection(stage).show());

        VBox loseBox = new VBox(10);
        loseBox.getChildren().addAll(loseLabel, messageLabel, backToMenu);
        loseBox.setAlignment(Pos.CENTER);
        loseBox.setStyle("-fx-background-color: rgba(255, 174, 174, 0.7);");
        loseBox.setPrefSize(width, height);


        StackPane losePane = new StackPane();
        losePane.getChildren().addAll(gamePane, loseBox);
        Scene scene = new Scene(losePane, width, height);
        stage.setScene(scene);
        stage.show();
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BorderPane getPane() {
        return gamePane;
    }

    public Board getBoard() {
        return board;
    }

    public int getLevel(){
        return level;
    }
}