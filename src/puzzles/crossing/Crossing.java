package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;

public class Crossing {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
        } else {
            int pups = Integer.parseInt(args[0]);
            int wolves = Integer.parseInt(args[1]);
            System.out.println("Pups: " + pups + ", Wolves: " + wolves);
            ArrayList<Configuration> path = Solver.findPath(new CrossingConfig(pups, wolves, 0, 0, true));
//                    new CrossingConfig(0, 0, pups, wolves, false));
            for (int i=0; i<path.size(); i++) {
                System.out.println("Step: " + i + ": " + path.get(i));
            }
        }
    }
}
