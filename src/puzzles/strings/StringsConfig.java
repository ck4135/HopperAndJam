package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class StringsConfig implements Configuration {
    private String string;
    private String finish;

    public StringsConfig(String string, String finish) {
        this.string = string;
        this.finish = finish;
    }

    private String createConfig(int index, boolean increase) {
        String result = "";
        for (int i=0; i<string.length(); i++) {
            if (i != index) {
                result += this.string.charAt(i);
            } else {
                char letter = this.string.charAt(i);
                if (increase) {
                    if (letter == 'Z') {
                        result += 'A';
                    } else {
                        result += (char) (letter + 1);
                    }
                } else {
                    if (letter == 'A') {
                        result += 'Z';
                    } else {
                        result += (char) (letter - 1);
                    }
                }
            }
        }
        return result;
    }

    public boolean isSolution() {
        return this.string.equals(this.finish);
    }

    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> successors = new ArrayList<>();
        for (int i=0; i<this.string.length(); i++) {
            successors.add(new StringsConfig(createConfig(i, true), this.finish));
            successors.add(new StringsConfig(createConfig(i, false), this.finish));
        }
        return successors;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StringsConfig o) {
            return this.string.equals(o.string) && this.finish.equals(o.finish);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public String toString() {
        return this.string;
    }
}
