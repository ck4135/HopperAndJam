package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Graphical user interface of Rush Hour puzzle
 */
public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    /** Maps each car letter to a different color */
    private final static HashMap<String, String> carsColors = new HashMap<>();
    /** Controller for model */
    private JamModel model;
    /** Used to prevent displaying anything before GUI has been set up */
    private boolean initialized;
    /** Borderpane of scene */
    private BorderPane jam;
    /** Center board of cars */
    private GridPane board;
    /** Grid of buttons used for the puzzle */
    private Button[][] buttons;
    /** Top label for puzzle */
    private Label header;

    /**
     * Initializes the GUI and sets it up as an observer of the model
     */
    public void init() {
        this.initialized = false;
        carsColors.put("A", "#F0F8FF");
        carsColors.put("B", "#0000FF");
        carsColors.put("C", "#00FFFF");
        carsColors.put("D", "#E0FFFF");
        carsColors.put("E", "#FFB6C1");
        carsColors.put("F", "#7B68EE");
        carsColors.put("G", "#00FA9A");
        carsColors.put("H", "#00FF7F");
        carsColors.put("I", "#FFFF00");
        carsColors.put("J", "#A0522D");
        carsColors.put("K", "#DA70D6");
        carsColors.put("L", "#FFDAB9");
        carsColors.put("O", "#87CEEB");
        carsColors.put("P", "#008000");
        carsColors.put("Q", "#DDA0DD");
        carsColors.put("R", "#FFFFE0");
        carsColors.put("S", "#FF00FF");
        String filename = getParameters().getRaw().get(0);
        this.model = new JamModel(filename);
        this.model.addObserver(this);
        this.header = new Label("Loaded: " + this.model.file);
    }

    /**
     * Creates center grid pane of buttons representing cars
     */
    public void createBoard() {
        this.board = new GridPane();
        this.buttons = new Button[this.model.getRows()][this.model.getColumns()];
        for (int i=0; i<this.model.getRows(); i++) {
            for (int j =0; j<this.model.getColumns(); j++) {
                Button cell = new Button(String.valueOf(this.model.get(i, j)));
                if (this.model.get(i, j) == '.') {
                    cell.setText(" ");
                    cell.setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;");
                } else if (this.model.get(i, j) == 'X') {
                    cell.setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;" +
                                    "-fx-background-color: #ff0000;");
                } else {
                    cell.setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;" +
                                    "-fx-background-color: " + carsColors.get(String.valueOf(this.model.get(i, j))) + ";");
                }
                cell.setMinSize(ICON_SIZE, ICON_SIZE);
                cell.setMaxSize(ICON_SIZE, ICON_SIZE);
                int finalI = i;
                int finalJ = j;
                cell.setOnAction(event ->
                        this.model.select(finalI, finalJ));
                buttons[i][j] = cell;
                board.add(buttons[i][j], j, i);
            }
        }
    }

    /**
     * Sets up the GUI and initial scene
     * @param stage initial stage to display the scene
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.initialized = true;
        this.jam = new BorderPane();
        Label label = this.header;
        createBoard();
        Button load = new Button("Load");
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/jam"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            this.model.file = String.valueOf(selectedFile);
            this.model.load(String.valueOf(this.model.file));
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.sizeToScene();
        });
        Button reset = new Button("Reset");
        reset.setOnAction(event -> this.model.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(event -> this.model.hint());
        HBox bot = new HBox();
        bot.getChildren().addAll(load, reset, hint);
        bot.setAlignment(Pos.CENTER);
        jam.setTop(label);
        BorderPane.setAlignment(this.header, Pos.TOP_CENTER);
        jam.setBottom(bot);
        jam.setCenter(this.board);
        Scene scene = new Scene(jam);
        stage.setTitle("Jam GUI");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Updates the GUI every time the model changes something
     * @param jamModel changes that model queries
     * @param msg message that model wants to display
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        if (!initialized) return;

        createBoard();
        jam.setCenter(board);
        if (model.gameState() == JamModel.GameState.SELECT)
            header.setText("Selected (" + model.prevRow() + ", " + model.prevCol() + ")");
        if (model.gameState() == JamModel.GameState.CANT_SELECT)
            header.setText("No car at (" + model.prevRow() + ", " + model.prevCol() + ")");
        if (model.gameState() == JamModel.GameState.MOVING)
            header.setText("Moved from (" + model.prevRow() + ", " + model.prevCol() + ") to " +
                    "(" + model.currRow() + ", " + model.currCol() + ")");
        if (model.gameState() == JamModel.GameState.CANT_MOVE)
            header.setText("Can't move from (" + model.prevRow() + ", " + model.prevCol() + ") to " +
                    "(" + model.currRow() + ", " + model.currCol() + ")");
        if (model.gameState() == JamModel.GameState.LOADED)
            header.setText("Loaded: " + this.model.file);
        if (model.gameState() == JamModel.GameState.CANT_LOAD)
            header.setText("Failed to load: " + getParameters().getRaw().get(0));
        if (model.gameState() == JamModel.GameState.SOLVED)
            header.setText("Already solved!");
        if (model.gameState() == JamModel.GameState.NO_SOLUTION)
            header.setText("No solution!");
        if (model.gameState() == JamModel.GameState.RESET)
            header.setText("Reset puzzle!");
        if (model.gameState() == JamModel.GameState.HINT)
            header.setText("Next step!");
        for (int i = 0; i < jamModel.getRows(); i++) {
            for (int j = 0; j < jamModel.getColumns(); j++) {
                if (jamModel.get(i, j) == '.') {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;");
                } else if (jamModel.get(i, j) == 'X') {
                    buttons[i][j].setText("X");
                    buttons[i][j].setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;" +
                                    "-fx-background-color: #ff0000;");
                } else {
                    buttons[i][j].setText(String.valueOf(jamModel.get(i, j)));
                    buttons[i][j].setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-border-radius: 1;" +
                                    "-fx-border-width: .5;" +
                                    "-fx-background-color: " + carsColors.get(String.valueOf(this.model.get(i, j))) + ";");
                }       
            }
        }
    }

    /**
     * Launches the GUI application
     * @param args given name of file
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
