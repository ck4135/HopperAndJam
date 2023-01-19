package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;

public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String start = args[0];
            String finish = args[1];
            System.out.println("Start: " + start + ", End: " + finish);
            ArrayList<Configuration> path = Solver.findPathPrint(new StringsConfig(start, finish));
            if (path.isEmpty()) {
                System.out.println("No solution");
            } else {
                for (int i=0; i<path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }
        }
    }
}
