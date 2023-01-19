package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;
import puzzles.jam.model.Car;

import java.io.File;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;


    private HoppersModel model;
    private boolean initialized;
    private BorderPane lake;
    private GridPane board;
    private Button[][] buttons;
    private Label header;
    private int rows;
    private int cols;
    // for demonstration purposes
    //greenfrog/redfrog to lillypad
    //water never changes
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));
    private Image lily_pad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));

    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        this.rows = model.getRows();
        this.cols = model.getCols();
        this.header = new Label("Loaded: " + getParameters().getRaw().get(0));
    }

    public void createBoard() {
        this.board = new GridPane();
        this.buttons = new Button[rows][cols];
        for (int i=0; i<this.model.getRows(); i++) {
            for (int j =0; j<this.model.getCols(); j++) {
                Button cell = new Button(String.valueOf(this.model.get(j, i)));
                if (this.model.get(j, i) == '.') {
                    cell.setGraphic(new ImageView(lily_pad));
                } else if (this.model.get(j, i) == '*') {
                    cell.setGraphic(new ImageView(water));
                } else if (this.model.get(j, i) == 'G'){
                    cell.setGraphic(new ImageView(greenFrog));
                } else {
                    cell.setGraphic(new ImageView(redFrog));
                }
                cell.setMinSize(ICON_SIZE, ICON_SIZE);
                cell.setMaxSize(ICON_SIZE, ICON_SIZE);
                int finalI = i;
                int finalJ = j;
                cell.setOnAction(event ->
                        this.model.select(finalJ, finalI));
                buttons[i][j] = cell;
                board.add(buttons[i][j], j, i);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.initialized = true;
        this.lake = new BorderPane();
        Label label = this.header;
        createBoard();
        Button load = new Button("Load");
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/hoppers"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            this.model.file = String.valueOf(selectedFile);
            this.model.load(this.model.file);
        });
        Button reset = new Button("Reset");
        reset.setOnAction(event -> this.model.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(event -> this.model.hint());
        HBox bot = new HBox();
        bot.getChildren().addAll(load, reset, hint);
        bot.setAlignment(Pos.CENTER);
        lake.setTop(label);
        BorderPane.setAlignment(this.header, Pos.TOP_CENTER);
        lake.setBottom(bot);
        lake.setCenter(this.board);
        Scene scene = new Scene(lake);
        stage.setTitle("Hopper GUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if (!initialized) return;
        createBoard();
        for (int i = 0; i < hoppersModel.getRows(); i++) {
            for (int j = 0; j < hoppersModel.getCols(); j++) {
                if (hoppersModel.get(j, i) == '.') {
                    buttons[j][i].setGraphic(new ImageView(lily_pad));
                } else if (hoppersModel.get(j, i) == '*') {
                    buttons[j][i].setGraphic(new ImageView(water));
                } else if (hoppersModel.get(j, i) == 'G'){
                    buttons[j][i].setGraphic(new ImageView(greenFrog));
                } else {
                    buttons[j][i].setGraphic(new ImageView(redFrog));
                }
            }
        }
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
