package utils;

import algo.TwoRateToExplore;
import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.PatchCalcUtil.getNextIndex;

public class BestCalculatedPatchOneBitMarker {
    public List<Integer> patch;
    public int fitness;
    public boolean isOneBit;

    public BestCalculatedPatchOneBitMarker(List<Integer> patch, int fitness, boolean isOneBit) {
        this.patch = patch;
        this.fitness = fitness;
        this.isOneBit = isOneBit;
    }

    public BestCalculatedPatchOneBitMarker(double mutation, int count, Problem problem, Random rand) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        boolean bestFitnessOneBit = false;
        for (int i = 0; i < count; ++i) {
            createPatchWithOneBitMarker(mutation, problem, rand);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
                bestFitnessOneBit = isOneBit;
            }
        }
        fitness = bestFitness;
        patch = bestPatch;
        isOneBit = bestFitnessOneBit;
    }

    /**
     * Creates one patch with one random bit mutation marker
     * @param mutation
     * @param problem
     * @param rand
     */
    public BestCalculatedPatchOneBitMarker(double mutation, Problem problem, Random rand) {
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
            patch.add(rand.nextInt(problemLength));
            this.isOneBit = true; //marker to print info if 1 random bit inverted
        } else {
            this.isOneBit = false; //marker to print info if mutation occured
        }
        this.patch = patch;
        this.fitness = problem.calculatePatchFitness(patch);
    }

}
