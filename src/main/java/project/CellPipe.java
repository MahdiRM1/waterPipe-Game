package project;

import javafx.animation.RotateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

public class CellPipe{

    private final int row;
    private final int col;
    public final int TILE_SIZE;
    private Button btn;
    private ImageView image;
    private int type;   
    private final boolean canRotate;
    private final GameUI gameUi;
    private final ScoreBoard scoreboard;

    enum Rotation{Clockwise, Counterclockwise}

    public CellPipe(int type, GameUI gameUi, ScoreBoard scoreBoard, boolean canrotate, int Row, int Col) {

        switch (gameUi.getLevel()) {
            case 1:
                TILE_SIZE = 150;
                break;
            case 2:
                TILE_SIZE = 130;
                break;
            default:
                TILE_SIZE = 110;
        }
        row = Row;
        col = Col;
        this.gameUi = gameUi;
        this.type = type;
        scoreboard = scoreBoard;
        canRotate = canrotate;
        setImage();
        initializeButton();

    }

    //----------------------------------------------------------
    //--------------------Initializers--------------------------
    //----------------------------------------------------------

    private void initializeButton(){
        btn = new Button();
        btn.setPrefSize(TILE_SIZE, TILE_SIZE);
        btn.setGraphic(image);
        btn.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        btn.setOnMouseClicked(event -> {
            if(type > 0 && type < 7){
                if(canRotate){
                    if(event.getButton() == MouseButton.PRIMARY) {
                        rotate(Rotation.Clockwise);
                        gameUi.setMove(row, col, Rotation.Clockwise);
                    }
                    else {
                        rotate(Rotation.Counterclockwise);
                        gameUi.setMove(row, col, Rotation.Counterclockwise);
                    }
                }
                int firstRow = gameUi.getBoard().getFirstPipeRow();
                int firstCol = gameUi.getBoard().getFirstPipeCol();
                if(gameUi.winCheck(firstRow, firstCol, firstRow + 1, firstCol)){
                   gameUi.showWinMessage();
                }
                else if(scoreboard.getmove() < 1){
                    scoreboard.stoptime();
                    gameUi.showLoseMessage("Your moves are over!");
                }
            }
        });
    }

    private void setImage(){

        if(canRotate) image = new ImageView(new Image("file:pictures/" + type + ".png"));
        else image = new ImageView(new Image("file:pictures/" + type + ".5.png"));
        image.setFitHeight(TILE_SIZE);
        image.setFitWidth(TILE_SIZE);
        
    }

    //----------------------------------------------------------
    //----------------------logic-------------------------------
    //----------------------------------------------------------

    public void rotate(Rotation rotation){

        if(!canRotate) return;
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(100), btn);
        
        if(rotation == Rotation.Clockwise){ 
            rotateTransition.setByAngle(90);
            clockwiseRotate();
        }
        else{
            rotateTransition.setByAngle(-90);
            counterclockwiseRotate();
        }

        scoreboard.move();
        if(scoreboard.remainUndo() > 0) gameUi.UndoBtn(true);
        rotateTransition.play();
    }

    private void clockwiseRotate(){

        switch (type) {

            case 0:
            case 7:
            case 8:break;

            case 2: 
                type = 1;
                break;
            case 6:
                type = 3;
                break;    

            default: type++;    

        }
    }

    private void counterclockwiseRotate(){

        switch (type) {

            case 0:
            case 7:
            case 8:break;

            case 1: 
                type = 2;
                break;
            case 3:
                type = 6;
                break;    

            default: type--;    
        }
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public int getType() {
        return type;
    }


    public Button getButton(){
        return btn;
    }
}