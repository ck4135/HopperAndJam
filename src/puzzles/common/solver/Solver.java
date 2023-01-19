package puzzles.common.solver;

import java.util.*;

public class Solver {

    private static HashMap<Configuration, Configuration> predecessors;
    private static List<Configuration> queue;
    private static int numConfig;
    private static int uniqueConfig;
    private static boolean found = false;

    public static ArrayList<Configuration> findPathPrint(Configuration start) {
        Configuration finish = null;
        HashMap<Configuration, Configuration> predecessor = new HashMap<>();
        predecessor.put(start, null);
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(start);
        boolean found = false;
        while (!queue.isEmpty()) {
            Configuration current = queue.poll();
            if (current.isSolution()) {
                finish = current;
                found = true;
                break;
            }
            Collection<Configuration> neighbor = current.getNeighbors();
            numConfig += neighbor.size();
            for (Configuration next : neighbor) {
                if (!predecessor.containsKey(next)) {
                    uniqueConfig += 1;
                    queue.add(next);
                    predecessor.put(next, current);
                }
            }
        }

        ArrayList<Configuration> path = new ArrayList<>();
        if (!found) {
            return path;
        }
        Configuration back = finish;
        while (predecessor.get(back) != null) {
            path.add(back);
            back = predecessor.get(back);
        }
        path.add(back);
        Collections.reverse(path);
        System.out.println("Total configs: " + numConfig);
        System.out.println("Unique configs: " + uniqueConfig);
        return path;
    }

    public static ArrayList<Configuration> findPath(Configuration start) {
        Configuration finish = null;
        HashMap<Configuration, Configuration> predecessor = new HashMap<>();
        predecessor.put(start, null);
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(start);
        boolean found = false;
        while (!queue.isEmpty()) {
            Configuration current = queue.poll();
            if (current.isSolution()) {
                finish = current;
                found = true;
                break;
            }
            Collection<Configuration> neighbor = current.getNeighbors();
            numConfig += neighbor.size();
            for (Configuration next : neighbor) {
                if (!predecessor.containsKey(next)) {
                    uniqueConfig += 1;
                    queue.add(next);
                    predecessor.put(next, current);
                }
            }
        }

        ArrayList<Configuration> path = new ArrayList<>();
        if (!found) {
            return path;
        }
        Configuration back = finish;
        while (predecessor.get(back) != null) {
            path.add(back);
            back = predecessor.get(back);
        }
        path.add(back);
        Collections.reverse(path);
        return path;
    }
}
