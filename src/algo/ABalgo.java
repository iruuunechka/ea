package algo;

import problem.Problem;
import utils.PatchCalcUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ABalgo implements Algorithm {
    private double mutationRate;
    private final double a; //a = 2
    private final double b; //b = 0.5
    private final double lowerBound; // 1 / problemLength or 1 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;
    private final boolean strict;
    private int iterCount = 0;

    private int state = 0;
    private int action = 0;

    public ABalgo(double mutationRate, double a, double b, double lowerBound, int lambda, boolean strict, Problem problem) {
        this.mutationRate = mutationRate;
        this.a = a;
        this.b = b;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.rand = ThreadLocalRandom.current();
        this.strict = strict;
    }

    @Override
    public void makeIteration() {
        iterCount++;
        List<Integer> bestPatch = null;
        int bestFitness = problem.getFitness();
        int numberOfBetter = 0;
        for (int i = 0; i < lambda; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutationRate, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (strict ? (fitness > problem.getFitness()) : (fitness >= problem.getFitness())) {
                numberOfBetter++;
            }
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        if (bestPatch != null) {
            problem.applyPatch(bestPatch, bestFitness);
        }
        if (numberOfBetter > 0) {
            mutationRate = Math.min(0.5, a * mutationRate);
            action = 1;
        } else {
            mutationRate = Math.max(lowerBound, b * mutationRate);
            action = 2;
        }
        state = numberOfBetter;
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {

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

    @Override
    public String getInfo() {
        return iterCount + ", fitness, " + getFitness() +"\n" +
               iterCount + ", action, " + action * problemLength / 2 + "\n" +
               iterCount + ", mutation" + mutationRate + "\n" +
               iterCount + ", state, " + state;

    }

    @Override
    public int getOptimum() {
        return problem.getOptimum();
    }

//    private List<Integer> createPatch() {
//        List<Integer> patch = new ArrayList<>();
//        for (int i = 0; i < problemLength; i++) {
//            if (rand.nextDouble() < mutationRate) {
//                patch.add(i);
//            }
//        }
//        if (patch.isEmpty()) {
//            patch.add(rand.nextInt(problemLength));
//        }
//        return patch;
//    }
}
