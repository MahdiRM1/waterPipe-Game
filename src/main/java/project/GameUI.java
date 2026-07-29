package project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import project.CellPipe.Rotation;

public class GameUI {

    private final Board board;
    private final int ROWS;
    private final int COLS;
    private final BorderPane gamePane = new BorderPane();
    private final Stage stage;
    private int level;
    private final Move[] move;
    private final ScoreBoard scoreboard;
    private final VBox buttonsBox;
    private final int BTN_SIDE = (int) (Constants.SCREEN_WIDTH/19.2);


    public GameUI(int level, Stage stage) {

        move = new Move[2];
        move[0] = new Move();
        move[1] = new Move();
        buttonsBox = new VBox(Constants.SCREEN_WIDTH/128);
        this.level = level;
        this.stage = stage;
        scoreboard = new ScoreBoard(level, this);

        int[][] boardValue;
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
        GridPane gridPane = new GridPane();
        initializeUi(gridPane);
        if(winCheck(board.getFirstPipeRow(), board.getFirstPipeCol(), board.getFirstPipeRow() + 1, board.getFirstPipeCol())) new LevelSelection(stage).startGame(level);

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

        ImageView backGround = ImageFactory.createImageView("background.png",
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        gamePane.getChildren().addFirst(backGround);
        gamePane.setCenter(gridPane);
        gamePane.setRight(scoreboard.getpanel());
        InitializeButtons();
    }


    public void InitializeButtons(){
        buttonsBox.setAlignment(Pos.TOP_CENTER);

        Button homeBtn = setButton("home", BTN_SIDE, BTN_SIDE, 0);
        Button settingBtn = setButton("setting", BTN_SIDE, BTN_SIDE, 0);
        Button restartBtn = setButton("restart", BTN_SIDE, BTN_SIDE, 0);
        
        homeBtn.setOnAction(event -> new LevelSelection(stage).show());
        restartBtn.setOnAction(event -> {
            scoreboard.stoptime();
            new LevelSelection(stage).startGame(level);
        }
        );

        buttonsBox.getChildren().addAll(homeBtn, restartBtn, settingBtn, new Button());
        UndoBtn(false);
        gamePane.setLeft(buttonsBox);
    }


    public void UndoBtn(Boolean bool){
        
        Button undoBtn = new Button();
        ImageView image = ImageFactory.createImageView("undo.png",
                Constants.SCREEN_WIDTH/20, Constants.SCREEN_WIDTH/20);
        undoBtn.setGraphic(image);

        if(!bool)undoBtn.setStyle(
            "-fx-background-radius: " + BTN_SIDE + "px; " +
            "-fx-min-width: " + Constants.SCREEN_WIDTH/20 + "px; " +
            "-fx-min-height: " + Constants.SCREEN_WIDTH/20 + "px; " +
            "-fx-background-color: linear-gradient(to bottom,rgb(230, 23, 9),rgb(255, 26, 26)); "  + 
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 0, 1);");
        else undoBtn = setButton("undo", BTN_SIDE, BTN_SIDE, 0);

        undoBtn.setOnAction(event -> Undo());
        buttonsBox.getChildren().remove(3);
        buttonsBox.getChildren().add(undoBtn);
    }

    //----------------------------------------------------------
    //---------------------game logic----------------------------
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
            ImageView image = ImageFactory.createImageView(text + ".png",
                    width, height);
            btn.setGraphic(image);
        }
        else btn.setText(text);
        btn.setStyle(String.format(
            "-fx-background-radius: %d; " +
            "-fx-min-width: %dpx; " +    
            "-fx-min-height: %dpx; " +   
            "-fx-background-color: linear-gradient(to bottom, #0f2fe4, rgb(55, 202, 247)); "  + 
            "-fx-text-fill: white; " +     
            "-fx-font-size: %dpx; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 0, 1);" ,
                BTN_SIDE, width, height, fontsize
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
            return switch (board.getCell(row2, col2).getType()) {
                case 1, 9 -> winCheck(row2, col2, row2 + 1, col2);
                case 3 -> winCheck(row2, col2, row2, col2 + 1);
                case 6 -> winCheck(row2, col2, row2, col2 - 1);
                default -> false;
            };
        }

        else if(row2 - row1 == -1){
            return switch (board.getCell(row2, col2).getType()) {
                case 1, 9 -> winCheck(row2, col2, row2 - 1, col2);
                case 4 -> winCheck(row2, col2, row2, col2 + 1);
                case 5 -> winCheck(row2, col2, row2, col2 - 1);
                default -> false;
            };
        }

        else if(col2 - col1 == 1){
            return switch (board.getCell(row2, col2).getType()) {
                case 2, 9 -> winCheck(row2, col2, row2, col2 + 1);
                case 5 -> winCheck(row2, col2, row2 + 1, col2);
                case 6 -> winCheck(row2, col2, row2 - 1, col2);
                case 8 -> true;
                default -> false;
            };
        }
        
        else {
            return switch (board.getCell(row2, col2).getType()) {
                case 2, 9 -> winCheck(row2, col2, row2, col2 - 1);
                case 3 -> winCheck(row2, col2, row2 - 1, col2);
                case 4 -> winCheck(row2, col2, row2 + 1, col2);
                default -> false;
            };
        }
    }


    //----------------------------------------------------------
    //-----------------------messages---------------------------
    //----------------------------------------------------------

    public void showWinMessage(){
        scoreboard.stoptime();
        
        Label winLabel = new Label("You Win!");
        winLabel.setTextFill(Color.GREEN);
        winLabel.setFont(Font.font("Arial", FontWeight.BOLD, BTN_SIDE));
        winLabel.setEffect(new DropShadow((double) BTN_SIDE /10, Color.BLACK));

        Button backToMenu = setButton("Back to menu", BTN_SIDE *7/5, BTN_SIDE, BTN_SIDE*3/10);
        backToMenu.setOnAction(event -> new LevelSelection(stage).show());

        Button nextLevel = setButton("Next level", BTN_SIDE *7/5, BTN_SIDE, BTN_SIDE*3/10);
        nextLevel.setOnAction(event -> new LevelSelection(stage).startGame(++level));

        VBox winBox = new VBox((double) BTN_SIDE /10);
        if(level < 3) winBox.getChildren().addAll(winLabel, backToMenu, nextLevel);
        else winBox.getChildren().addAll(winLabel, backToMenu);
        winBox.setAlignment(Pos.CENTER);
        winBox.setStyle("-fx-background-color: rgba(174, 255, 174, 0.7);");
        editBox(winBox);
    }

    private void editBox(VBox box) {
        box.setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        StackPane winPane = new StackPane();
        winPane.getChildren().addAll(gamePane, box);
        Scene scene = new Scene(winPane, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public void showLoseMessage(String message){    
        Label loseLabel = new Label("You Lose!");
        loseLabel.setTextFill(Color.RED);
        loseLabel.setFont(Font.font("Arial", FontWeight.BOLD, BTN_SIDE));
        loseLabel.setEffect(new DropShadow((double) BTN_SIDE /10, Color.BLACK));

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setFont(Font.font("Arial", (double) BTN_SIDE /2));
        messageLabel.setEffect(new DropShadow((double) BTN_SIDE /20, Color.BLACK));
        

        Button backToMenu = setButton("Back to menu", BTN_SIDE *7/5, BTN_SIDE, BTN_SIDE*3/10);
        backToMenu.setOnAction(event -> new LevelSelection(stage).show());

        VBox loseBox = new VBox((double) BTN_SIDE /10);
        loseBox.getChildren().addAll(loseLabel, messageLabel, backToMenu);
        loseBox.setAlignment(Pos.CENTER);
        loseBox.setStyle("-fx-background-color: rgba(255, 174, 174, 0.7);");
        editBox(loseBox);
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

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