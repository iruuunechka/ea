package algo;

import problem.Problem;
import utils.BestCalculatedPatch;
import utils.PatchCalcUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static utils.PatchCalcUtil.createPatch;

public class SimpleEA implements Algorithm {
    private double mutationRate;
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    public SimpleEA(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lambda = lambda;
        rand = new Random();
    }

    @Override
    public void makeIteration() {
        BestCalculatedPatch best = getBest();
        if (best.fitness > problem.getFitness()) {
            problem.applyPatch(best.patch, best.fitness);
        }
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(problem.getFitness());
    }

    @Override
    public double getMutationRate() {
        return mutationRate;
    }

    @Override
    public int getFitness() {
        return problem.getFitness();
    }

    private BestCalculatedPatch getBest() {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        for (int i = 0; i < lambda; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutationRate, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        return new BestCalculatedPatch(bestPatch, bestFitness, 0, 0);
    }
}

