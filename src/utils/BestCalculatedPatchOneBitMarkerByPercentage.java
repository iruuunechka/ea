package utils;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.PatchCalcUtil.getNextIndex;

public class BestCalculatedPatchOneBitMarkerByPercentage extends BestCalculatedPatchOneBitMarker {

    public BestCalculatedPatchOneBitMarkerByPercentage(List<Integer> patch, int fitness, boolean isOneBit) {
        super(patch, fitness, isOneBit);
    }

    public BestCalculatedPatchOneBitMarkerByPercentage(double mutation, int count, Problem problem, Random rand) {
        super();
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        int countOneBit = 0;
        int bestCount = 0;
        for (int i = 0; i < count; ++i) {
            createPatchWithOneBitMarker(mutation, problem, rand);
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
        isOneBit = rand.nextDouble() < ((double) countOneBit) / bestCount ? true : false;
//    version 1:    isOneBit = (countOneBit > (bestCount / 2)); sq and not sq cross
//        System.out.println(countOneBit + " " + bestCount);
    }
//    public BestCalculatedPatchOneBitMarkerByPercentage(double mutation, int count, Problem problem, Random rand) {
//        super();
//        List<Integer> bestPatch = null;
//        int bestFitness = -1;
//        int countOneBit = 0;
//        int bestCount = 0;
//        for (int i = 0; i < count; ++i) {
//            createPatchWithOneBitMarker(mutation, problem, rand);
//            if (fitness == bestFitness) {
//                bestPatch = patch;
//                bestCount++;
//                if (isOneBit) {
//                    countOneBit++;
//                }
//            } else if (fitness > bestFitness){
//                bestFitness = fitness;
//                bestPatch = patch;
//                bestCount = 1;
//                countOneBit = isOneBit ? 1 : 0;
//            }
//        }
//        fitness = bestFitness;
//        patch = bestPatch;
//        if (countOneBit >= (double) bestCount / 2) {
//            isOneBit = true;
//        } else {
//            isOneBit = rand.nextDouble() < ((double) countOneBit) / bestCount ? true : false;
//        }
//        System.out.print(countOneBit + " " + bestCount + " ");
//    }
//
//    public BestCalculatedPatchOneBitMarkerByPercentage(double mutation, int count, Problem problem, Random rand) {
//        super();
//        List<Integer> bestPatch = null;
//        int bestFitness = -1;
//        int countOneBit = 0;
//        for (int i = 0; i < count; ++i) {
//            createPatchWithOneBitMarker(mutation, problem, rand);
//            if (fitness == bestFitness) {
//                bestPatch = patch;
//            } else if (fitness > bestFitness){
//                bestFitness = fitness;
//                bestPatch = patch;
//            }
//            if (isOneBit) {
//                countOneBit++;
//            }
//
//        }
//        fitness = bestFitness;
//        patch = bestPatch;
//        isOneBit = rand.nextDouble() < ((double) countOneBit) / count ? true : false;
//    }

    /**
     * Creates one patch with one random bit mutation marker
     * @param mutation
     * @param problem
     * @param rand
     */
    public BestCalculatedPatchOneBitMarkerByPercentage(double mutation, Problem problem, Random rand) {
        super(mutation, problem, rand);
    }
}
