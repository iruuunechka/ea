package algo;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ABalgo implements Algorithm {
    private double mutationRate;
    private final double a; //a = 2
    private final double b; //b = 0.5
    private final double lowerBound; // 1 / problemLength or 1 / (problemLength^2)
    private final int lambda;
    private final Problem problem;
    private final int problemLength;
    private final Random rand;

    public ABalgo(double mutationRate, double a, double b, double lowerBound, int lambda, Problem problem) {
        this.mutationRate = mutationRate;
        this.a = a;
        this.b = b;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.rand = new Random();
    }

    @Override
    public void makeIteration() {
        List<Integer> bestPatch = null;
        int bestFitness = problem.getFitness();
        int numberOfBetter = 0;
        for (int i = 0; i < lambda; ++i) {
            List<Integer> patch = createPatch();
            int fitness = problem.calculatePatchFitness(patch);
            if (fitness > problem.getFitness()) {
                numberOfBetter++;
            }
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        if (bestPatch != null) {
            problem.applyPatch(bestPatch, bestFitness);
        }
        if (numberOfBetter > 0.05 * lambda) {
            mutationRate = Math.min(0.5, a * mutationRate);
        } else {
            mutationRate = Math.max(lowerBound, b * mutationRate);
        }
    }

    private List<Integer> createPatch() {
        List<Integer> patch = new ArrayList<>();
        for (int i = 0; i < problemLength; i++) {
            if (rand.nextDouble() < mutationRate) {
                patch.add(i);
            }
        }
        if (patch.isEmpty()) {
            patch.add(rand.nextInt(problemLength));
        }
        return patch;
    }
}
