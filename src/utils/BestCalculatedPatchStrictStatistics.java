package utils;

import problem.Problem;

import java.util.List;

public class BestCalculatedPatchStrictStatistics {
    public List<Integer> patch;
    public int fitness;
    public int betterCount;

    public BestCalculatedPatchStrictStatistics(List<Integer> patch, int fitness) {
        this.patch = patch;
        this.fitness = fitness;
    }

    /**
     *
     * @param mutation mutation probability
     * @param count number of generated individuals (lambda/2 or lambda)
     * @param problem
     */
    public BestCalculatedPatchStrictStatistics (double mutation, int count, Problem problem) {
        this.betterCount = 0;
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        int problemLength = problem.getLength();
        for (int i = 0; i < count; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutation, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (fitness > problem.getFitness()) {
                this.betterCount++;
            }
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        this.patch = bestPatch;
        this.fitness = bestFitness;
    }
}
