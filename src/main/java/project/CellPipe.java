package project;

import javafx.animation.RotateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

public class CellPipe{

    private final int row;
    private final int col;
    public final double TILE_SIZE;
    private final Button btn = new Button();
    private final ImageView image;
    private int type;
    private final boolean canRotate;
    private final GameUI gameUi;
    private final ScoreBoard scoreboard;

    enum Rotation{Clockwise, Counterclockwise}

    public CellPipe(int type, GameUI gameUi, ScoreBoard scoreBoard, boolean canrotate, int Row, int Col) {

        switch (gameUi.getLevel()) {
            case 1:
                TILE_SIZE = Constants.SCREEN_WIDTH/12.8;
                break;
            case 2:
                TILE_SIZE = Constants.SCREEN_WIDTH/15.36;
                break;
            default:
                TILE_SIZE = Constants.SCREEN_WIDTH/19.2;
        }
        row = Row;
        col = Col;
        this.gameUi = gameUi;
        this.type = type;
        scoreboard = scoreBoard;
        canRotate = canrotate;
        image = setImage();
        initializeButton();
    }

    //----------------------------------------------------------
    //--------------------Initializers--------------------------
    //----------------------------------------------------------

    private void initializeButton(){
        btn.setPrefSize(TILE_SIZE, TILE_SIZE);
        btn.setGraphic(image);
        btn.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        btn.setOnMouseClicked(event -> clickAction(event.getButton()));
        initRotate();
    }

    private void clickAction(javafx.scene.input.MouseButton mb){
        if(type > 0 && type < 7){
            if(canRotate){
                if(mb == MouseButton.PRIMARY) {
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
    }

    private void initRotate(){
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1), btn);
        int rotate = switch (type){
            case 2, 4 -> 90;
            case 5    -> 180;
            case 6    -> -90;
            default   -> 0;
        };
        rotateTransition.setByAngle(rotate);
        rotateTransition.play();
    }

    private ImageView setImage(){
        if (type == 0) return new ImageView();
        String str = switch (type){
            case 1, 2       -> canRotate ? "1.png" : "1.5.png";
            case 3, 4, 5, 6 -> canRotate ? "3.png" : "3.5.png";
            default         -> canRotate ? (type + ".png") : type + ".5.png";
        };
        return ImageFactory.createImageView(str, TILE_SIZE, TILE_SIZE);
    }

    //----------------------------------------------------------
    //----------------------logic-------------------------------
    //----------------------------------------------------------

    public void rotate(Rotation rotation){

        if(!canRotate) {
            SoundManager.playError();
            return;
        }
        SoundManager.playRotate();
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
        type = switch (type) {
            case 0, 7, 8 -> type;
            case 2 -> 1;
            case 6 -> 3;
            default -> type+1;
        };
    }

    private void counterclockwiseRotate(){
        type = switch (type) {
            case 0, 7, 8 -> type;
            case 1 -> 2;
            case 3 -> 6;
            default -> type-1;
        };
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