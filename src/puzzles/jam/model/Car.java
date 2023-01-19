package puzzles.jam.model;

/**
 * Car object representating a car in a Rush Hour puzzle
 */
public class Car {

    /** Name of car */
    private char name;
    /** Starting row index of car */
    private int startRow;
    /** Starting column index of car */
    private int startCol;
    /** Ending row index of car */
    private int endRow;
    /** Ending column index of car */
    private int endCol;
    /** Whether car is horizontal or not */
    private boolean horizontal = true;

    /**
     * Creates a car object
     */
    public Car(char name, int startRow, int startCol, int endRow, int endCol) {
        this.name = name;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        if (startCol == endCol) this.horizontal = false;
    }

    /**
     * @return Car's name
     */
    public char getName() {
        return this.name;
    }

    /**
     * @return Car's starting row
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * @return Car's starting column
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * @return Car's ending row
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * @return Car's ending column
     */
    public int getEndCol() {
        return endCol;
    }

    /**
     * @return Car's orientation
     */
    public boolean isHorizontal() {
        return horizontal;
    }
}
