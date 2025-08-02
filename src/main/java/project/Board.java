package project;

import java.util.Random;

public class Board {

    private final int ROWS;
    private final int COLS;
    public CellPipe[][] cells;
    private final GameUI gameUi;
    private final int[][] values;
    private final ScoreBoard scoreboard;
    private int firstPipeRow;
    private int firstPipeCol;

    public Board(int Rows, int Cols, int[][] values, GameUI gameUi, ScoreBoard scoreBoard){
        this.values = values;
        this.gameUi = gameUi;
        scoreboard = scoreBoard;
        ROWS = Rows; COLS = Cols;
        cells = new CellPipe[ROWS][COLS];
        switch (COLS) {
            case 5:
                InitializeCells(0);
                break;

            case 7:
                InitializeCells(2);
                break;

            default:
                InitializeCells(3);
        }
    }

    //----------------------------------------------------------
    //--------------------Initializers--------------------------
    //----------------------------------------------------------

    private void InitializeCells(int fixePipes){

        for(int i = 0; i < fixePipes; i++){
            int[] quardians = new int[2];
            randomFixePipe(quardians);
            cells[quardians[0]][quardians[1]] = new CellPipe(values[quardians[0]][quardians[1]], gameUi, scoreboard, false, quardians[0], quardians[1]);
        }
        
        Random random = new Random();
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                int a;
                if(cells[row][col] == null){
                    switch (values[row][col]) {
                        case 0:{
                            a = random.nextInt(7);
                            break;
                        }
                        case 1:
                        case 2:{
                            a = random.nextInt(2) + 1;
                            break;
                        }
                        case 3:
                        case 4:
                        case 5:
                        case 6:{
                            a = random.nextInt(4) + 3;
                            break;
                        }
                        case 7:{
                            firstPipeRow = row;
                            firstPipeCol = col;
                        }
                        case 8:
                        case 9:
                            a = values[row][col];
                            break;
                        default: a = 0;
                    }
                    cells[row][col] = new CellPipe(a, gameUi, scoreboard, true, row, col);
                }
            }
        }
    }

    private void randomFixePipe(int[] quardians){
        Random rdm = new Random();
        int row = rdm.nextInt(5);
        int col = rdm.nextInt(5);
        if(values[row][col] > 0 && values[row][col] < 7){
            quardians[0] = row;
            quardians[1] = col;
        }
        else{
            randomFixePipe(quardians);
        }
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public CellPipe getCell(int row, int col){
        return cells[row][col];
    }

    public int getFirstPipeRow(){
        return firstPipeRow;
    }

    public int getFirstPipeCol(){
        return firstPipeCol;
    }
}
