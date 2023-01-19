package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;

/**
 * Representation of a Rush Hour configuration of a puzzle
 */
public class JamConfig implements Configuration {

    /** Keyword representing an empty block in the puzzle */
    private final static char EMPTY = '.';
    /** Number of rows */
    private int rows;
    /** Number of columns */
    private int columns;
    /** Representation of board in the puzzle */
    private char[][] board;
    /** List of cars in puzzle */
    private ArrayList<Car> cars;

    /**
     * Constructs the board given a data file
     *
     * @param filename name of file
     * @throws IOException
     */
    public JamConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] line = in.readLine().split("\\s+");
            this.rows = Integer.parseInt(line[0]);
            this.columns = Integer.parseInt(line[1]);
            line = in.readLine().split("\\s+");
            int count = Integer.parseInt(line[0]);
            this.cars = new ArrayList<>();
            this.board = new char[this.rows][this.columns];
            for (int i=0; i<this.rows; i++) {
                for (int j=0; j<this.columns; j++) {
                    this.board[i][j] = EMPTY;
                }
            }
            for (int i=0; i<count; i++) {
                line = in.readLine().split("\\s+");
                char name = line[0].charAt(0);
                cars.add(new Car(name, Integer.parseInt(line[1]), Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]), Integer.parseInt(line[4])));
            }
            for (Car car: cars) {
                if (!car.isHorizontal())
                    for (int i = car.getStartRow(); i < car.getEndRow() + 1; i++) {
                        this.board[i][car.getEndCol()] = car.getName();
                    }
                else
                    for (int i=car.getStartCol(); i<car.getEndCol() + 1; i++) {
                        this.board[car.getEndRow()][i] = car.getName();
                    }
            }
        }
    }

    /**
     * Copy constructor that changes the board based on the moved cars
     *
     * @param other Config to copy
     * @param copy New list of cars
     */
    private JamConfig(JamConfig other, ArrayList<Car> copy) {
        this.rows = other.rows;
        this.columns = other.columns;
        this.board = new char[this.rows][this.columns];
        this.cars = copy;
        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                this.board[i][j] = EMPTY;
            }
        }
        for (Car car: copy) {
            if (!car.isHorizontal())
                for (int i = car.getStartRow(); i < car.getEndRow() + 1; i++) {
                    this.board[i][car.getEndCol()] = car.getName();
                }
            else
                for (int i=car.getStartCol(); i<car.getEndCol() + 1; i++) {
                    this.board[car.getEndRow()][i] = car.getName();
                }
        }
    }

    /**
     * @return if current config is solution or not
     */
    public boolean isSolution() {
        for (Car car: cars) {
            if (car.getName() == 'X' && car.getEndCol() == this.columns - 1) return true;
        }
        return false;
    }

    /**
     * Generates neighbors based on which cars can move 1 space back and forth
     * @return list of neighbors of current config
     */
    public Collection<Configuration> getNeighbors() {
        List<Configuration> successors = new ArrayList<>();
        for (Car car: cars) {
            ArrayList<Car> copy = new ArrayList<>(cars);
            //If car is horizontal
            if (car.isHorizontal()) {
                if (car.getEndCol() + 1 != this.columns) {
                    if (this.board[car.getStartRow()][car.getEndCol() + 1] == EMPTY) {
                        copy.remove(car);
                        copy.add(new Car(car.getName(), car.getStartRow(), car.getStartCol() + 1,
                                car.getEndRow(), car.getEndCol() + 1));
                        successors.add(new JamConfig(this, copy));
                        copy = new ArrayList<>(cars);
                    }
                }
                if (car.getStartCol() != 0) {
                    if (this.board[car.getStartRow()][car.getStartCol() - 1] == EMPTY) {
                        copy.remove(car);
                        copy.add(new Car(car.getName(), car.getStartRow(), car.getStartCol() - 1,
                                car.getEndRow(), car.getEndCol() - 1));
                        successors.add(new JamConfig(this, copy));
                    }
                }
            }
            // If car is vertical
            else {
                if (car.getEndRow() + 1 != this.rows) {
                    if (this.board[car.getEndRow() + 1][car.getEndCol()] == EMPTY) {
                        copy.remove(car);
                        copy.add(new Car(car.getName(), car.getStartRow() + 1, car.getStartCol(),
                                car.getEndRow() + 1, car.getEndCol()));
                        successors.add(new JamConfig(this, copy));
                        copy = new ArrayList<>(cars);
                    }
                }
                if (car.getStartRow() != 0) {
                    if (this.board[car.getStartRow() - 1][car.getEndCol()] == EMPTY) {
                        copy.remove(car);
                        copy.add(new Car(car.getName(), car.getStartRow() - 1, car.getStartCol(),
                                car.getEndRow() - 1, car.getEndCol()));
                        successors.add(new JamConfig(this, copy));
                    }
                }
            }
        }
        return successors;
    }

    /**
     * @param row row index
     * @param col col index
     * @return value at (row, col) in board
     */
    public char get(int row, int col) {
        return this.board[row][col];
    }

    /**
     * If possible, moves car from (startRow, startCol) to (endRow, endCol)
     * @param startRow starting row index
     * @param startCol starting col index
     * @param endRow ending row index
     * @param endCol ending col index
     */
    public void moveCar(int startRow, int startCol, int endRow, int endCol) {
        Car car = getCar(startRow, startCol);
        if (car.isHorizontal()) {
            if (endRow == car.getStartRow())
                if (this.board[endRow][endCol] == EMPTY) {
                    if (endCol < car.getStartCol()) {
                        cars.remove(car);
                        cars.add(new Car(car.getName(), startRow, endCol, endRow, car.getEndCol() - (car.getStartCol() - endCol)));
                    } else {
                        cars.remove(car);
                        cars.add(new Car(car.getName(), startRow, car.getStartCol() + (endCol - car.getEndCol()), endRow, endCol));
                    }
                }
        } else {
            if (endCol == car.getStartCol())
                if (this.board[endRow][endCol] == EMPTY) {
                    if (endRow < car.getStartRow()) {
                        cars.remove(car);
                        cars.add(new Car(car.getName(), endRow, startCol, car.getEndRow() - (car.getStartRow() - endRow), endCol));
                    } else {
                        cars.remove(car);
                        cars.add(new Car(car.getName(), car.getStartRow() + (endRow - car.getEndRow()), startCol, endRow, endCol));
                    }
                }
        }
        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                this.board[i][j] = EMPTY;
            }
        }
        for (Car vehicle: cars) {
            if (!vehicle.isHorizontal())
                for (int i = vehicle.getStartRow(); i < vehicle.getEndRow() + 1; i++) {
                    this.board[i][vehicle.getEndCol()] = vehicle.getName();
                }
            else
                for (int i=vehicle.getStartCol(); i<vehicle.getEndCol() + 1; i++) {
                    this.board[vehicle.getEndRow()][i] = vehicle.getName();
                }
        }
    }

    /**
     * @return number of cars in config
     */
    public int numCars() {
        return this.cars.size();
    }

    /**
     * @return list of cars in config
     */
    public ArrayList<Car> getCars() {
        return this.cars;
    }

    /**
     * @param name name of car
     * @return car with given name
     */
    public Car getCar(char name) {
        for (Car car: cars) {
            if (car.getName() == name) return car;
        }
        return null;
    }

    /**
     * @param row row index
     * @param col col index
     * @return Car at (row, col)
     */
    public Car getCar(int row, int col) {
        return getCar(this.board[row][col]);
    }

    /**
     * @return number of rows in config
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return number of columns in config
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns if two JamConfigs are equal or not
     * @param o given JamConfig object
     * @return If equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JamConfig jamConfig = (JamConfig) o;
        return Arrays.deepEquals(board, jamConfig.board);
    }

    /**
     * @return Hash code of current config
     */
    @Override
    public int hashCode() {
        int result = 31 + Arrays.deepHashCode(board);
        return result;
    }

    /**
     * To string representation of puzzle, prints out board
     * @return String of puzzle
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                result.append(this.board[i][j]);
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
}
