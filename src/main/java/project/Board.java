package project;

import java.util.Random;

public class Board {

    private final int ROWS;
    private final int COLS;
    public CellPipe[][] cells;
    private final GameUI gameUi;
    private final int[][] values;
    private final ScoreBoard scoreboard;
    private int firstPipeCol;

    public Board(int Rows, int Cols, int[][] values, GameUI gameUi, ScoreBoard scoreBoard){
        this.values = values;
        this.gameUi = gameUi;
        scoreboard = scoreBoard;
        ROWS = Rows; COLS = Cols;
        cells = new CellPipe[ROWS][COLS];
        switch (COLS) {
            case 5 -> InitializeCells(0);
            case 7 -> InitializeCells(2);
            default -> InitializeCells(3);
        }
    }

    //----------------------------------------------------------
    //--------------------Initializers--------------------------
    //----------------------------------------------------------

    private void InitializeCells(int fixePipes){
        fixPipes(fixePipes);
        otherPipes();
    }

    private void fixPipes(int fixePipes){
        for(int i = 0; i < fixePipes; i++){
            int[] qs = new int[2];
            randomFixPipe(qs);
            cells[qs[0]][qs[1]] = new CellPipe(values[qs[0]][qs[1]], gameUi, scoreboard, false, qs[0], qs[1]);
        }
        for (int col = 0; col < COLS; col++) {
            if (values[0][col] == 7) {
                firstPipeCol = col;
                return;
            }
        }
    }

    private void otherPipes(){
        Random random = new Random();
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++){
                if(cells[row][col] == null){
                    int a = switch (values[row][col]) {
                        case 0          -> random.nextInt(7);
                        case 1, 2       -> random.nextInt(2) + 1;
                        case 3, 4, 5, 6 -> random.nextInt(4) + 3;
                        case 7, 8, 9    -> values[row][col];
                        default -> 0;
                    };
                    cells[row][col] = new CellPipe(a, gameUi, scoreboard, true, row, col);
                }
            }
        }
    }

    private void randomFixPipe(int[] quardinations){
        Random rdm = new Random();
        int row = rdm.nextInt(5);
        int col = rdm.nextInt(5);
        if(values[row][col] > 0 && values[row][col] < 7){
            quardinations[0] = row;
            quardinations[1] = col;
        }
        else{
            randomFixPipe(quardinations);
        }
    }

    //----------------------------------------------------------
    //-----------------------getters----------------------------
    //----------------------------------------------------------

    public CellPipe getCell(int row, int col){
        return cells[row][col];
    }

    public int getFirstPipeRow(){
        return 0;
    }

    public int getFirstPipeCol(){
        return firstPipeCol;
    }
}
