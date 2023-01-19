package puzzles.hoppers.model;

public class Frog {

    private int row;
    private int col;
    private char type;

    public Frog(int col, int row, char type){
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public char getType(){return type;}

    public void updateRow(int row){
        this.row = row;
    }

    public void updateCol(int col){
        this.col = col;
    }
}
