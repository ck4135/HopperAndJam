package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.solver.Jam;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.*;

/**
 * Model for Rush Hour puzzle
 */
public class JamModel {

    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();
    /** Keyword representing empty square in board */
    private final static char EMPTY = '.';
    /** the current configuration */
    private JamConfig currentConfig;
    /** The solution path to the puzzle*/
    private ArrayList<Configuration> path;
    /** Possible game states */
    public enum GameState {SELECT, CANT_SELECT, MOVING, CANT_MOVE, LOADED, CANT_LOAD, SOLVED, NO_SOLUTION, RESET, HINT}
    /** Current game state of the puzzle */
    private GameState gameState;
    /** Determines which part of select */
    private boolean selecting = false;
    /** Initial coordinate selected */
    private int[] selection;
    /** Current coordinate selected */
    private int[] currSelection;
    /** Current filename that's loaded */
    public String file;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
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

    /**
     * Loads puzzle in given file
     * @param filename name of file
     */
    public JamModel(String filename) {
        this.file = filename;
        this.load(file);
    }

    /**
     * Returns the value at (row, col)
     * @param row row index
     * @param col column index
     * @return value at given coordinate
     */
    public char get(int row, int col) {
        return this.currentConfig.get(row, col);
    }

    /**
     * Given a txt file, loads the puzzle
     * @param filename name of file
     */
    public void load(String filename) {
        String[] fileName = filename.split("/");
        ArrayList<String> f = new ArrayList<>(Arrays.asList(fileName));
        Collections.reverse(f);
        this.file = f.get(2) + "/" + f.get(1) + "/" + f.get(0);
        try {
            this.selection = new int[2];
            this.currSelection = new int[2];
            this.gameState = GameState.LOADED;
            this.currentConfig = new JamConfig(filename);
            this.path = Solver.findPath(this.currentConfig);
            this.alertObservers("Loaded: " + this.file);
        } catch (IOException ie) {
            this.gameState = GameState.CANT_LOAD;
            this.alertObservers("Failed to load:" + this.file);
        }
    }

    /**
     * Selects a car at (row, col) and attempts to move it
     * @param row given row index
     * @param col given col index
     */
    public void select(int row, int col) {
        if (!this.selecting) {
            if (this.currentConfig.get(row, col) != EMPTY) {
                this.selecting = true;
                this.selection[0] = row;
                this.selection[1] = col;
                this.gameState = GameState.SELECT;
                this.alertObservers("Selected (" + row + ", " + col + ")");
            } else {
                this.gameState = GameState.CANT_SELECT;
                this.alertObservers("No car at (" + row + ", " + col + ")");
            }
        } else {
            Car car = currentConfig.getCar(selection[0], selection[1]);
            this.currSelection[0] = row;
            this.currSelection[1] = col;
            if (car.isHorizontal()) {
                if (row != selection[0] || currentConfig.get(row, col) != EMPTY) {
                    this.gameState = GameState.CANT_MOVE;
                    this.alertObservers("Can't move from (" + selection[0] + ", "
                            + selection[1] + ") to (" + row + ", " + col + ")");
                }
                else {
                    this.gameState = GameState.MOVING;
                    currentConfig.moveCar(selection[0], selection[1], row, col);
                    if (currentConfig.isSolution()) {
                        this.gameState = GameState.SOLVED;
                        this.alertObservers("Puzzle solved!");
                    }
                    else
                        this.alertObservers("Moved from (" + selection[0] + ", "
                            + selection[1] + ") to (" + row + ", " + col + ")");
                }
            } else {
                if (col != selection[1] || currentConfig.get(row, col) != EMPTY) {
                    this.gameState = GameState.CANT_MOVE;
                    this.alertObservers("Can't move from (" + selection[0] + ", "
                            + selection[1] + ") to (" + row + ", " + col + ")");
                } else {
                    this.gameState = GameState.MOVING;
                    currentConfig.moveCar(selection[0], selection[1], row, col);
                    this.alertObservers("Moved from (" + selection[0] + ", "
                            + selection[1] + ") to (" + row + ", " + col + ")");
                }
            }
            this.selecting = false;
        }
    }

    /**
     *
     * @return User's previous row selection coordinate
     */
    public int prevRow() {
        return this.selection[0];
    }

    /**
     *
     * @return user's previous column selection coordinate
     */
    public int prevCol() {
        return this.selection[1];
    }

    /**
     *
     * @return user's current row selection coordinate
     */
    public int currRow() {
        return this.currSelection[0];
    }

    /**
     *
     * @return user's current column selection coordinate
     */
    public int currCol() {
        return this.currSelection[1];
    }

    /**
     * Finds the next step in the puzzle, unless there is no solution or the puzzle has already been solved
     */
    public void hint() {
        this.path = Solver.findPath(this.currentConfig);
        if (this.path.isEmpty()) {
            this.gameState = GameState.NO_SOLUTION;
            this.alertObservers("No solution!");
        }
        else if (this.path.size() == 1) {
            this.gameState = GameState.SOLVED;
            this.alertObservers("Puzzle already solved!");
        } else {
            this.gameState = GameState.HINT;
            this.currentConfig = (JamConfig) this.path.get(1);
            this.alertObservers("Next step!");
        }
    }

    /**
     * Resets the puzzle to its original position
     */
    public void reset() {
        this.load(file);
        this.gameState = GameState.RESET;
        this.alertObservers("Puzzle reset!");
    }

    /**
     * @return Current game state of puzzle
     */
    public GameState gameState() {
        return this.gameState;
    }

    /**
     * @return Returns list of cars in puzzle
     */
    public ArrayList<Car> getCars() {
        return this.currentConfig.getCars();
    }

    /**
     * @return number of cars in puzzle
     */
    public int numCars() {
        return this.currentConfig.numCars();
    }

    /**
     * @return number of rows in puzzle
     */
    public int getRows() {
        return currentConfig.getRows();
    }

    /**
     * @return number of columns in puzzle
     */
    public int getColumns() {
        return currentConfig.getColumns();
    }
}
