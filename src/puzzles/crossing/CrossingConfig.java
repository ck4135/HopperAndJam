package puzzles.crossing;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class CrossingConfig implements Configuration {

    private int[] left;
    private int[] right;
    private boolean position;

    public CrossingConfig(int leftP, int leftW, int rightP, int rightW, boolean position) {
        this.left = new int[]{leftP, leftW};
        this.right = new int[]{rightP, rightW};
        this.position = position;
    }

    public boolean isSolution() {
        return this.left[0] == 0 && this.left[1] == 0 && !position;
    }

    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> successors = new ArrayList<>();
        if (position) {
            if (this.left[0] >= 2) {
                successors.add(new CrossingConfig(left[0]-1, left[1], right[0] + 1, right[1], false));
                successors.add(new CrossingConfig(left[0]-2, left[1], right[0] + 2, right[1], false));
            }
            if (this.left[1] >= 1) {
                successors.add(new CrossingConfig(left[0], left[1]-1, right[0], right[1]+1, false));
            }
        } else {
            if (this.right[0] >= 2) {
                successors.add(new CrossingConfig(left[0]+1, left[1], right[0]-1, right[1], true));
                successors.add(new CrossingConfig(left[0]+2, left[1], right[0]-2, right[1], true));
            }
            if (this.right[1] >= 1) {
                successors.add(new CrossingConfig(left[0], left[1]+1, right[0], right[1]+1, true));
            }
        }
        return successors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossingConfig that = (CrossingConfig) o;
        return position == that.position && Arrays.equals(left, that.left) && Arrays.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(position);
        result = 31 * result + Arrays.hashCode(left);
        result = 31 * result + Arrays.hashCode(right);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (position) {
            result.append("(BOAT) ");
        } else {
            result.append("\t");
        }
        result.append("left=[" + left[0] + ", " + left[1] + "], right=[" + right[0] + ", " + right[1] + "]  ");
        if (!position) {
            result.append("(BOAT)");
        }
        return result.toString();
    }
}
