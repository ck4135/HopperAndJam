package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.crossing.CrossingConfig;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamConfig;

import java.io.*;
import java.io.FileReader;
import java.util.*;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration {
    private int rows;
    private int cols;
    private char[][] board;
    private static final char GREEN_FROG = 'G';
    private static final char RED_FROG = 'R';
    private static final char EMPTY_SPACE = '.';
    private static final char INVALID_SPACE = '*';
    private ArrayList<Frog> frogs;
    private String file;

    public HoppersConfig(String filename) throws IOException{

        try (
            BufferedReader in = new BufferedReader(new FileReader(filename))) {
        // read first line: rows cols
        String[] fields = in.readLine().split("\\s+");

        this.rows = Integer.parseInt(fields[0]);
        this.cols = Integer.parseInt(fields[1]);
        this.board = new char[cols][rows];
        this.frogs = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                board[j][i] = EMPTY_SPACE;
            }
        }

        for(int i = 0; i < rows; i++){
            fields = in.readLine().split("\\s+");
            for(int j = 0; j < cols; j++){
                if(fields[j].charAt(0) == GREEN_FROG || fields[j].charAt(0) == RED_FROG){
                    frogs.add(new Frog(j, i, fields[j].charAt(0)));
                } else if(fields[j].charAt(0) == INVALID_SPACE)
                this.board[j][i] = INVALID_SPACE;
            }
        }

        for(Frog frog: frogs){
            char frogType = frog.getType();
            int frogCol = frog.getCol();
            int frogRow = frog.getRow();
            board[frogCol][frogRow] = frogType;
        }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    private HoppersConfig(HoppersConfig other, ArrayList<Frog> copy) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.board = new char[this.cols][this.rows];
        this.frogs = copy;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.board[j][i] = EMPTY_SPACE;
            }
        }
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(other.board[j][i] == INVALID_SPACE){
                    this.board[j][i] = INVALID_SPACE;
                }
            }
        }

        for(Frog frog: frogs){
            char frogType = frog.getType();
            int frogCol = frog.getCol();
            int frogRow = frog.getRow();
            this.board[frogCol][frogRow] = frogType;
        }
    }

    public boolean isSolution(){
        int redFrog = 0;
        int greenFrog = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(board[j][i] == RED_FROG){
                    redFrog++;
                } else if(board[j][i] == GREEN_FROG){
                    greenFrog++;
                }
            }
        }
        return greenFrog == 0 && redFrog == 1;
        //if(frogs.size() == 1){
        //    Frog frog = frogs.get(0);
        //    return frog.getType() == RED_FROG;
        //}
        //return false;
    }

    public Collection<Configuration> getNeighbors(){
        Collection<Configuration> successors = new ArrayList<>();
        ArrayList<Frog> copy = new ArrayList<>();
        int rowCheck = rows - 1;
        int colCheck = cols - 1;
        for(int i = 0; i < frogs.size(); i++){
            Frog frog = frogs.get(i);
            int row = frog.getRow();
            int col = frog.getCol();
            if(row % 2 == 0){
                int hopUp = row - 4;
                int hopDUp = row - 2;
                int hopRight = col + 4;
                int hopDRight = col + 2;
                int hopDown = row + 4;
                int hopDDown = row + 2;
                int hopLeft = col - 4;
                int hopDLeft = col - 2;
                if(hopUp >= 0){

                    char upCheck = board[col][hopUp];
                    char frogCheck = board[col][hopUp + 2];
                    if(frogCheck == GREEN_FROG){
                        if(upCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(col, hopUp, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopDUp >= 0  && hopDRight <= colCheck){

                    char urCheck = board[hopDRight][hopDUp];
                    char frogCheck = board[hopDRight - 1][hopDUp + 1];
                    if(frogCheck == GREEN_FROG){
                        if(urCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(hopDRight, hopDUp, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopRight <= colCheck){

                    char rightCheck = board[hopRight][row];
                    char frogCheck = board[hopRight - 2][row];
                    if(frogCheck == GREEN_FROG){
                        if(rightCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(hopRight, row, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopDDown <= rowCheck && hopDRight <= colCheck){

                    char drCheck = board[hopDRight][hopDDown];
                    char frogCheck = board[hopDRight - 1][hopDDown - 1];
                    if(frogCheck == GREEN_FROG){
                        if(drCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(hopDRight, hopDDown, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopDown <= rowCheck){

                    char downCheck = board[col][hopDown];
                    char frogCheck = board[col][hopDown - 2];
                    if(frogCheck == GREEN_FROG){
                        if(downCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(col, hopDown, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopDDown <= rowCheck && hopDLeft >= 0){

                    char dlCheck = board[hopDLeft][hopDDown];
                    char frogCheck = board[hopDLeft + 1][hopDDown - 1];
                    if(frogCheck == GREEN_FROG){
                        if(dlCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(hopDLeft, hopDDown, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopLeft >= 0){

                    char leftCheck = board[hopLeft][row];
                    char frogCheck = board[hopLeft + 2][row];
                    if(frogCheck == GREEN_FROG){
                        if(leftCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            for(int h = 0; h < frogs.size(); h++){
                                Frog removeFrog = frogs.get(h);
                                if(removeFrog.getCol() == hopLeft + 2 && removeFrog.getRow() == row){
                                    copy.remove(removeFrog);
                                }
                            }
                            copy.add(new Frog(hopLeft, row, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if(hopDUp >= 0 && hopDLeft >= 0){

                    char ulCheck = board[hopDLeft][hopDUp];
                    char frogCheck = board[hopDLeft + 1][hopDUp + 1];
                    if(frogCheck == GREEN_FROG){
                        if(ulCheck == EMPTY_SPACE){
                            copy.remove(frog);
                            copy.add(new Frog(hopDLeft, hopDUp, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }
            } else {
                int hopDUp = row - 2;
                int hopDRight = col + 2;
                int hopDDown = row + 2;
                int hopDLeft = col - 2;
                if (hopDUp >= 0 && hopDRight <= colCheck) {

                    char urCheck = board[hopDRight][hopDUp];
                    char frogCheck = board[hopDRight - 1][hopDUp + 1];
                    if (frogCheck == GREEN_FROG) {
                        if (urCheck == EMPTY_SPACE) {
                            copy.remove(frog);
                            copy.add(new Frog(hopDRight, hopDUp, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if (hopDDown <= rowCheck && hopDRight <= colCheck) {

                    char drCheck = board[hopDRight][hopDDown];
                    char frogCheck = board[hopDRight - 1][hopDDown - 1];
                    if (frogCheck == GREEN_FROG) {
                        if (drCheck == EMPTY_SPACE) {
                            copy.remove(frog);
                            copy.add(new Frog(hopDRight, hopDDown, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if (hopDDown <= rowCheck && hopDLeft >= 0) {

                    char dlCheck = board[hopDLeft][hopDDown];
                    char frogCheck = board[hopDLeft + 1][hopDDown - 1];
                    if (frogCheck == GREEN_FROG) {
                        if (dlCheck == EMPTY_SPACE) {
                            copy.remove(frog);
                            copy.add(new Frog(hopDLeft, hopDDown, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }  if (hopDUp >= 0 && hopDLeft >= 0) {

                    char ulCheck = board[hopDLeft][hopDUp];
                    char frogCheck = board[hopDLeft + 1][hopDUp + 1];
                    if (frogCheck == GREEN_FROG) {
                        if (ulCheck == EMPTY_SPACE) {
                            copy.remove(frog);
                            copy.add(new Frog(hopDLeft, hopDUp, frog.getType()));
                            successors.add(new HoppersConfig(this, copy));
                        }
                    }
                }
            }
        }
        return successors;
    }

    public char get(int col, int row){
        return board[col][row];
    }


    public void hop(int startCol, int startRow, int endCol, int endRow){
        Frog frog = new Frog(startCol ,startRow , GREEN_FROG);
        for(Frog frogCheck: frogs){
            if(frogCheck.getRow() == startRow && frogCheck.getCol() == startCol){
                frog = frogCheck;
            }
        }

        System.out.println("Col: " + frog.getCol() + " Row: " + frog.getRow());
        System.out.println("EndCol: " + endCol + " EndRow: " + endRow);
            if (frog.getRow() % 2 == 0) {
                if(startCol == endCol && startRow > endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol < endCol && startRow > endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol < endCol && startRow == endRow){
                    System.out.println("Correct");
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol < endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol == endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol > endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol > endCol && startRow == endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol > endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
            } else {
                if(startCol < endCol && startRow > endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol < endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol > endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
                if(startCol > endCol && startRow < endRow){
                    frogs.remove(frog);
                    frogs.add(new Frog(endCol, endRow, frog.getType()));
                }
            }
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(this.board[j][i] != EMPTY_SPACE && this.board[j][i] != INVALID_SPACE){
                    this.board[j][i] = EMPTY_SPACE;
                }
            }
        }
        for(Frog frogger: frogs){
            char frogType = frogger.getType();
            int frogCol = frogger.getCol();
            int frogRow = frogger.getRow();
            this.board[frogCol][frogRow] = frogType;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoppersConfig that = (HoppersConfig) o;
        return rows == that.rows &&
                cols == that.cols &&
                Arrays.deepEquals(board, that.board) &&
                Objects.equals(frogs, that.frogs);
    }

    @Override
    public int hashCode() {
        int result = 31 + Arrays.deepHashCode(board);
        return result;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<this.rows; i++) {
            for (int j = 0; j<this.cols; j++) {
                result.append(this.board[j][i]);
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    public int getCols(){
        return cols;
    }

    public int getRows(){
        return rows;
    }
}
