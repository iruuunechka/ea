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

    private int iterCount = 0;

    public SimpleEA(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lambda = lambda;
        rand = new Random();
    }

    @Override
    public void makeIteration() {
        iterCount++;
        BestCalculatedPatch best = new BestCalculatedPatch(mutationRate, lambda, problem);
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

    @Override
    public long getFitnessCount() {
        return iterCount * lambda;
    }

    @Override
    public int getIterCount() {
        return iterCount;
    }

    @Override
    public String getProblemInfo() {
        return problem.getInfo();
    }
}

