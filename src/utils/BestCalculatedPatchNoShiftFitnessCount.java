package utils;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.PatchCalcUtil.getNextIndex;

public class BestCalculatedPatchNoShiftFitnessCount {

    public List<Integer> patch;
    public int fitness;
    public int fitnessCount = 0;

    public BestCalculatedPatchNoShiftFitnessCount(double mutation, int count, Problem problem, Random rand) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        for (int i = 0; i < count; ++i) {
            createPatchWithOneBitMarker(mutation, problem, rand);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        fitness = bestFitness;
        patch = bestPatch;
    }

    /**
     * Creates one patch with one random bit mutation marker
     * @param mutation
     * @param problem
     * @param rand
     */
    public BestCalculatedPatchNoShiftFitnessCount(double mutation, Problem problem, Random rand) {
        createPatchWithOneBitMarker(mutation, problem, rand);
    }

    public void createPatchWithOneBitMarker(double mutation, Problem problem, Random rand) {
        List<Integer> patch = new ArrayList<>(16);
        int i = getNextIndex(-1, mutation);
        int problemLength = problem.getLength();
        while (i < problemLength) {
            patch.add(i);
            i = getNextIndex(i, mutation);
        }

        if (patch.isEmpty()) {
            this.patch = null;
            this.fitness = -1;
        } else {
            this.patch = patch;
            this.fitness = problem.calculatePatchFitness(patch);
            this.fitnessCount++;
        }
    }

}