package project;

import project.CellPipe.Rotation;

public class Move{
    private int row;
    private int col;
    private Rotation rotation;

    public Move(){
        row = -1;
        col = -1;
        rotation = null;
    }

    public void set(int row, int col, Rotation rotation){
        this.row = row;
        this.col = col;
        this.rotation = rotation;
    }

    public int Row(){
        return row;
    }

    public int Col(){
        return col;
    }

    public Rotation Rotation(){
        return rotation;
    }
}