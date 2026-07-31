package project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.CellPipe.Rotation;

public class GameUI {

    private final Board board;
    private final int ROWS;
    private final int COLS;
    private final StackPane stackPane = new StackPane();
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
            case 1 -> {
                ROWS = 5; COLS = 5;
                boardValue = new int[][]{
                {7, 0, 0, 0, 0},
                {3, 2, 2, 5, 0},
                {4, 2, 2, 6, 0},
                {1, 0, 0, 0, 0},
                {3, 2, 2, 2, 8}
                };
                board = new Board(ROWS, COLS, boardValue, this, scoreboard);
            }

            case 2 -> {
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
            }
            default -> {
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
            }
        }
        GridPane gridPane = new GridPane();
        initializeUi(gridPane);
        if(winCheck(board.getFirstPipeRow(), board.getFirstPipeCol(),
                board.getFirstPipeRow() + 1, board.getFirstPipeCol())) new LevelSelection(stage).startGame(level);
        stackPane.getChildren().add(gamePane);
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
        
        homeBtn.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).show();
        });
        restartBtn.setOnAction(event -> {
            scoreboard.stoptime();
            SoundManager.playClick();
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
        SoundManager.playClick();
        if(move[1].Rotation() == null || scoreboard.remainUndo() < 1) return;

        if(move[1].Rotation() == Rotation.Clockwise) board.getCell(move[1].Row(), move[1].Col()).rotate(Rotation.Counterclockwise);
        else board.getCell(move[1].Row(), move[1].Col()).rotate(Rotation.Clockwise);
        scoreboard.undo();
        move[1].set(move[0].Row(), move[0].Col(), move[0].Rotation());
        move[0].set(-1, -1, null);
        if(scoreboard.remainUndo() < 1 || move[1].Rotation() == null) UndoBtn(false);
    }

    private Button setButton(String text, double width, double height, double fontsize){
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
                BTN_SIDE, (int)width, (int)height, (int)fontsize
            ));

        btn.setOnMouseEntered(e -> {
            SoundManager.playHover();
            btn.setStyle(btn.getStyle() + "-fx-background-color: linear-gradient(to bottom, rgb(55, 202, 247), #0f2fe4);");
        });
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
        SoundManager.playWin();
        Pane pane = new Pane();

        ImageView winPic = ImageFactory.createImageView("win.png",
                Constants.SCREEN_WIDTH/8, Constants.SCREEN_WIDTH/12);
        ImageFactory.setNodePosition(winPic,
                Constants.SCREEN_WIDTH/3, Constants.SCREEN_HEIGHT/3);

        pane.getChildren().add(winPic);
        stackPane.getChildren().add(pane);
        finishAnimation(winPic, "win");
    }

    private void winPane(){
        Pane pane = (Pane) stackPane.getChildren().getLast();

        Button backToMenu = setButton("Back to menu", Constants.SCREEN_WIDTH/5, BTN_SIDE, BTN_SIDE*3/10);
        backToMenu.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).show();
        });
        ImageFactory.setNodePosition(backToMenu, Constants.SCREEN_WIDTH/4, Constants.SCREEN_HEIGHT/1.5);

        Button nextLevel = setButton("Next level", Constants.SCREEN_WIDTH/5, BTN_SIDE, BTN_SIDE*3/10);
        nextLevel.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).startGame(++level);
        });
        ImageFactory.setNodePosition(nextLevel, Constants.SCREEN_WIDTH*0.55, Constants.SCREEN_HEIGHT/1.5);

        Button restartLevel = setButton("Restart level", Constants.SCREEN_WIDTH/5, BTN_SIDE, BTN_SIDE*3/10);
        restartLevel.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).startGame(level);
        });
        ImageFactory.setNodePosition(restartLevel, Constants.SCREEN_WIDTH*0.55, Constants.SCREEN_HEIGHT/1.5);

        if(level < 3) pane.getChildren().addAll(backToMenu, nextLevel);
        else pane.getChildren().addAll(backToMenu, restartLevel);

        pane.setStyle("-fx-background-color: rgba(174, 255, 174, 0.7);");
        stackPane.getChildren().add(pane);
    }

    public void showLoseMessage(String message){
        scoreboard.stoptime();
        SoundManager.playLose();
        Pane pane = new Pane();

        ImageView winPic = ImageFactory.createImageView("lose.png",
                Constants.SCREEN_WIDTH/8, Constants.SCREEN_WIDTH/12);
        ImageFactory.setNodePosition(winPic,
                Constants.SCREEN_WIDTH/3, Constants.SCREEN_HEIGHT/3);

        pane.getChildren().add(winPic);
        stackPane.getChildren().add(pane);
        finishAnimation(winPic, message);
    }

    private void losePane(String message){
        Pane pane = (Pane) stackPane.getChildren().getLast();

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setFont(Font.font("Arial", (double) BTN_SIDE /2));
        messageLabel.setEffect(new DropShadow((double) BTN_SIDE /20, Color.BLACK));
        ImageFactory.setNodePosition(messageLabel,
                message.contains("up") ? Constants.SCREEN_WIDTH*0.4 : Constants.SCREEN_WIDTH*0.37,
                Constants.SCREEN_HEIGHT/1.7);

        Button backToMenu = setButton("Back to menu", Constants.SCREEN_WIDTH/5, BTN_SIDE, BTN_SIDE*3/10);
        backToMenu.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).show();
        });
        ImageFactory.setNodePosition(backToMenu, Constants.SCREEN_WIDTH*0.25, Constants.SCREEN_HEIGHT/1.4);

        Button restartLevel = setButton("Restart level", Constants.SCREEN_WIDTH/5, BTN_SIDE, BTN_SIDE*3/10);
        restartLevel.setOnAction(event -> {
            SoundManager.playClick();
            new LevelSelection(stage).startGame(level);
        });
        ImageFactory.setNodePosition(restartLevel, Constants.SCREEN_WIDTH*0.55, Constants.SCREEN_HEIGHT/1.4);

        pane.getChildren().addAll(messageLabel, backToMenu, restartLevel);
        pane.setStyle("-fx-background-color: rgba(255, 174, 174, 0.7);");
        stackPane.getChildren().add(pane);
    }

    private void finishAnimation(ImageView image, String cond){
        image.setOnMouseEntered(e -> {});
        image.setOnMouseClicked(e -> {});
        double sizeH = image.getFitHeight();
        double sizeW = image.getFitWidth();
        double diffX = image.getLayoutX() - Constants.SCREEN_WIDTH/4;
        double diffY = image.getLayoutY() - Constants.SCREEN_HEIGHT/10;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e ->{
            image.setFitWidth(image.getFitWidth() + (sizeW / 50));
            image.setFitHeight(image.getFitHeight() + (sizeH / 50));
            image.setLayoutX(image.getLayoutX() - (diffX / 150));
            image.setLayoutY(image.getLayoutY() - (diffY / 150));
        }));
        timeline.setCycleCount(150);
        timeline.setOnFinished(e -> {
            if ("win".equals(cond)) {
                winPane();
            } else {
                losePane(cond);
            }
        });
        timeline.play();
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public Pane getPane() {
        return stackPane;
    }

    public Board getBoard() {
        return board;
    }

    public int getLevel(){
        return level;
    }
}