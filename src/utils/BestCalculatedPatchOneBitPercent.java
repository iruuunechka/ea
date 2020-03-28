package utils;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.PatchCalcUtil.getNextIndex;

public class BestCalculatedPatchOneBitPercent {
    public List<Integer> patch;
    public int fitness;
    public double oneBitPercent;

    protected BestCalculatedPatchOneBitPercent() {
        patch = null;
        fitness = 0;
        oneBitPercent = 0;
    }

    public BestCalculatedPatchOneBitPercent(List<Integer> patch, int fitness, double oneBitPercent) {
        this.patch = patch;
        this.fitness = fitness;
        this.oneBitPercent = oneBitPercent;
    }

    public BestCalculatedPatchOneBitPercent(double mutation, int count, Problem problem, Random rand) {
        super();
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        int countOneBit = 0;
        int bestCount = 0;
        for (int i = 0; i < count; ++i) {
            boolean isOneBit = createPatchWithOneBitMarker(mutation, problem, rand);
            if (fitness == bestFitness) {
                bestPatch = patch;
                bestCount++;
                if (isOneBit) {
                    countOneBit++;
                }
            } else if (fitness > bestFitness){
                bestFitness = fitness;
                bestPatch = patch;
                bestCount = 1;
                countOneBit = isOneBit ? 1 : 0;
            }
        }
        fitness = bestFitness;
        patch = bestPatch;
        oneBitPercent = ((double) countOneBit) / bestCount;
    }

    public boolean createPatchWithOneBitMarker(double mutation, Problem problem, Random rand) {
        List<Integer> patch = new ArrayList<>(16);
        int i = getNextIndex(-1, mutation);
        int problemLength = problem.getLength();
        while (i < problemLength) {
            patch.add(i);
            i = getNextIndex(i, mutation);
        }
        boolean isOneBit;
        if (patch.isEmpty()) {
            patch.add(rand.nextInt(problemLength));
            isOneBit = true; //marker to print info if 1 random bit inverted
        } else {
            isOneBit = false; //marker to print info if mutation occured
        }
        this.patch = patch;
        this.fitness = problem.calculatePatchFitness(patch);
        return isOneBit;
    }
}
