package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.IOException;
import java.util.*;

public class HoppersModel {

    public enum GameState {SELECT, CANT_SELECT, MOVING, CANT_MOVE, ILLEGAL_MOVE, LOADED, CANT_LOAD, WON, LOST, RESET, HINT}

    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig startConfig;
    private HoppersConfig currentConfig;
    private ArrayList<Configuration> path;

    private HoppersModel.GameState gameState;
    private boolean pick = false;
    private int[] selection;
    public String file;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    public HoppersModel(String filename) {
        try{
            this.selection = new int[2];
            this.currentConfig = new HoppersConfig(filename);
        } catch (IOException ioException){
            alertObservers("File can't load");
        }

    }

    public char get(int col, int row){
        return this.currentConfig.get(col, row);
    }

    public void load(String filename){
        try {
            this.startConfig = new HoppersConfig(file);
            this.currentConfig = new HoppersConfig(file);
            this.path = Solver.findPath(currentConfig);
            this.alertObservers("Loaded: " + file);
        } catch (IOException ioException){
            this.alertObservers("Invalid File");
        }
    }

    public void hint(){
        this.path = Solver.findPath(this.currentConfig);
        if (this.path.isEmpty()) {
            this.gameState = GameState.LOST;
            this.alertObservers("No solution!");
        }
        else if (this.path.size() == 1) {
            this.gameState = GameState.WON;
            this.alertObservers("Puzzle already solved!");
        } else {
            this.gameState = HoppersModel.GameState.HINT;
            this.currentConfig = (HoppersConfig) this.path.get(1);
            this.alertObservers("Next step!");
        }
    }

    public void select(int col, int row) {
        if (!this.pick) {
            System.out.println("Select False");
            if (this.currentConfig.get(col, row) != '.' && this.currentConfig.get(col, row) != '*') {
                this.gameState = GameState.SELECT;
                System.out.println("Frog picked");
                this.pick = true;
                selection[0] = col;
                selection[1] = row;
                this.alertObservers("Piece at col: " + col + " row: " + row + " selected");
                System.out.println(pick);
            } else {
                this.gameState = GameState.CANT_SELECT;
                this.alertObservers("Please pick a frog");
            }
        } else {
                System.out.println("Select True");
                if ((Math.abs(selection[0] - col) + Math.abs(selection[1] - row)) == 4 && currentConfig.get(col, row) == '.') {
                    currentConfig.hop(selection[0], selection[1], col, row);
                    this.gameState = GameState.MOVING;
                    this.alertObservers("Moved from (" + selection[0] + ", " + selection[1] + ") to (" + row + ", " + col + ")");
                } else {
                    this.gameState = GameState.ILLEGAL_MOVE;
                    this.alertObservers("Can't move from (" + selection[0] + ", " + selection[1] + ") to (" + col + ", " + row + ")");
                }
                this.pick = false;
            }
        }

    public void quit(){
        System.exit(0);
    }

    public void reset(){
        this.currentConfig = this.startConfig;
        this.gameState = GameState.RESET;
        this.alertObservers("Puzzle reset!");
    }

    public int getCols(){
        return currentConfig.getCols();
    }

    public int getRows(){
        return currentConfig.getRows();
    }
}
