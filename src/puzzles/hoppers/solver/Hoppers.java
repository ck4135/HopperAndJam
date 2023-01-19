package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.ArrayList;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {
            System.out.println("File: " + args[0]);
            ArrayList<Configuration> path = Solver.findPathPrint(new HoppersConfig(args[0]));
            if (path.isEmpty()) {
                System.out.println("No solution");
            } else {
                for (int i=0; i<path.size(); i++) {
                    System.out.println("Step " + i + ": \n" + path.get(i));
                }
            }
        }
    }
}
