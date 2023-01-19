package puzzles.jam.ptui;

import puzzles.common.ConsoleApplication;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.PrintWriter;

/**
 * A Plain Text User Interface (PTUI) for the Rush Hour puzzle.
 *
 * @author chansungkim
 */
public class JamPTUI extends ConsoleApplication implements Observer<JamModel, String> {

    /* View/Access controller for model */
    private JamModel model;
    /* Prints this class' messages */
    private PrintWriter console;
    /* Used to prevent this class displaying any info before the UI has been completely set up */
    private boolean initialized;

    /**
     * Creates the Rush Hour model and adds to the list of the model's observers.
     */
    @Override
    public void init() {
        this.initialized = false;
        this.model = new JamModel(getArguments().get(0));
        this.model.addObserver(this);
    }

    /**
     * Tells model to reset the board
     */
    public void reset() {
        this.model.reset();
    }

    /**
     * Sets up the PTUI for Rush Hour and creates the command handlers
     *
     * @param console Where the UI should print output. It is recommended to save
     *                this object in a field in the subclass.
     * @throws Exception
     */
    @Override
    public void start(PrintWriter console) throws Exception {
        this.console = console;
        this.initialized = true;
        this.model.load(getArguments().get(0));
        super.setOnCommand( "s" , 2, "",
                args -> this.model.select(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        super.setOnCommand( "h" , 0, "",
                args -> this.model.hint());
        super.setOnCommand( "l" , 1, "",
                args -> this.model.load(args[0]));
        super.setOnCommand("r" , 0, "", args -> this.reset());
        super.setOnCommand( "select" , 2, "",
                args -> this.model.select(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        super.setOnCommand( "hint" , 0, "",
                args -> this.model.hint());
        super.setOnCommand( "load" , 1, "",
                args -> this.model.load(args[0]));
        super.setOnCommand("reset" , 0, "", args -> this.reset());
        super.help(new String[]{});
    }

    /**
     * Processes and prints the changes that the model wants to make to the view
     *
     * @param jamModel observed subject of this observer
     * @param msg the message that the model is printing
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        if (!this.initialized) return;

        this.console.println(msg);
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for (int i=0; i<jamModel.getColumns(); i++) {
            result.append(i);
            result.append(" ");
        }
        result.append(System.lineSeparator());
        result.append("  ");
        result.append("-".repeat(Math.max(0, jamModel.getColumns() * 2)));
        result.append(System.lineSeparator());
        for (int i=0; i<jamModel.getRows(); i++) {
            result.append(i);
            result.append("| ");
            for (int j=0; j<jamModel.getColumns(); j++) {
                result.append(jamModel.get(i, j));
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        this.console.println(result);

    }

    /**
     * Starts the console application for the PTUI
     *
     * @param args given filename for Rush Hour
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
        else {
            ConsoleApplication.launch(JamPTUI.class, args);
        }
    }
}
