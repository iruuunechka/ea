package utils;

import problem.Problem;

import java.util.List;

public class BestCalculatedPatchNoShift {
    public List<Integer> patch;
    public int fitness;

    public BestCalculatedPatchNoShift(List<Integer> patch, int fitness) {
        this.patch = patch;
        this.fitness = fitness;
    }

    /**
     *
     * @param mutation mutation probability
     * @param count number of generated individuals (lambda/2 or lambda)
     * @param problem
     */
    public BestCalculatedPatchNoShift (double mutation, int count, Problem problem) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        int problemLength = problem.getLength();
        for (int i = 0; i < count; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatchNoShift(mutation, problemLength);
            if (!patch.isEmpty()) {
                int fitness = problem.calculatePatchFitness(patch);
                if (fitness >= bestFitness) {
                    bestFitness = fitness;
                    bestPatch = patch;
                }
            }
        }
        this.patch = bestPatch;
        this.fitness = bestFitness;
    }
}
