package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;
import puzzles.strings.StringsConfig;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Solver for Rush Hour puzzle
 */
public class Jam {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        } else {
            System.out.println("File: " + args[0]);
            ArrayList<Configuration> path = Solver.findPathPrint(new JamConfig(args[0]));
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