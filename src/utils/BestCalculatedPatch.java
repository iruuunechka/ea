package utils;

import java.util.List;

public class BestCalculatedPatch {
    public List<Integer> patch;
    public int fitness;
    public int median;
    public double average;

    public BestCalculatedPatch(List<Integer> patch, int fitness, int median, double average) {
        this.patch = patch;
        this.fitness = fitness;
        this.median = median;
        this.average = average;
    }
}
