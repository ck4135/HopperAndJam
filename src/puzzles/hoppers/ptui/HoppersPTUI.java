package puzzles.hoppers.ptui;

import puzzles.common.ConsoleApplication;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.PrintWriter;

public class HoppersPTUI extends ConsoleApplication implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private PrintWriter console;
    private boolean initialized;

    @Override
    public void init() {
        this.initialized = false;
        this.model = new HoppersModel(getArguments().get(0));
        this.model.addObserver(this);
    }

    @Override
    public void start(PrintWriter console) throws Exception {
        this.console = console;
        this.initialized = true;
        super.setOnCommand( "s" , 2, "",
                args -> this.model.select(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        super.setOnCommand( "h" , 0, "",
                args -> this.model.hint());
        super.setOnCommand( "l" , 1, "",
                args -> this.model.load(args[0]));
        super.setOnCommand("r" , 0, "", args -> this.model.reset());
        super.setOnCommand("q", 0, "", args -> this.model.quit());
        super.setOnCommand( "select" , 2, "",
                args -> this.model.select(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        super.setOnCommand( "hint" , 0, "",
                args -> this.model.hint());
        super.setOnCommand( "load" , 1, "",
                args -> this.model.load(args[0]));
        super.setOnCommand("reset" , 0, "", args -> this.model.reset());
        super.setOnCommand("quit", 0 , "", args -> this.model.quit());
        super.help(new String[]{});
    }

    @Override
    public void update(HoppersModel hopperModel, String msg) {
        if (!this.initialized) return;

        this.console.println(msg);
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for (int i=0; i<hopperModel.getCols(); i++) {
            result.append(i);
            result.append(" ");
        }
        result.append(System.lineSeparator());
        result.append("  ");
        result.append("-".repeat(Math.max(0, hopperModel.getCols() * 2)));
        result.append(System.lineSeparator());
        for (int i=0; i<hopperModel.getRows(); i++) {
            result.append(i);
            result.append("| ");
            for (int j=0; j<hopperModel.getCols(); j++) {
                result.append(hopperModel.get(j, i));
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        this.console.println(result);

    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        else {
            ConsoleApplication.launch(HoppersPTUI.class, args);
        }
    }
}
