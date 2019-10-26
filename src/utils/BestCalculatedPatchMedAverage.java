package utils;

import problem.Problem;

import java.util.Arrays;
import java.util.List;

public class BestCalculatedPatchMedAverage {
    public List<Integer> patch;
    public int fitness;
    public int median;
    public double average;

    public BestCalculatedPatchMedAverage(List<Integer> patch, int fitness, int median, double average) {
        this.patch = patch;
        this.fitness = fitness;
        this.median = median;
        this.average = average;
    }

    /**
     *
     * @param mutation mutation probability
     * @param count number of generated individuals (lambda/2 or lambda)
     * @param problem
     */
    public BestCalculatedPatchMedAverage (double mutation, int count, Problem problem) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        double average = 0;
        int[] fitnessOfPatches = new int[count];
        int problemLength = problem.getLength();
        for (int i = 0; i < count; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutation, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            fitnessOfPatches[i] = fitness; //убрать если не надо считать медиану
            average = (i == 0) ? fitness : (average * i + fitness) / (i + 1);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        Arrays.sort(fitnessOfPatches); //убрать если не надо считать медиану
        this.patch = bestPatch;
        this.fitness = bestFitness;
        this.median = fitnessOfPatches[count];
        this.average = average;
    }

}
