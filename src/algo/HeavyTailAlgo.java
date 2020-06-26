package algo;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HeavyTailAlgo implements Algorithm {
    private final double beta;
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private int iterCount = 0;
    private final double[] probabilities;
    private final boolean[] flipped;

    public HeavyTailAlgo(double beta, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.beta = beta;
        this.lambda = lambda;
        rand = ThreadLocalRandom.current();
        probabilities = new double[problemLength];
        flipped = new boolean[problemLength];
        double sum = 0;
        for (int i = 0; i < problemLength; i++) {
            probabilities[i] = Math.pow(i + 1, -beta);
            sum += probabilities[i];
        }
        for (int i = 0; i < problemLength; i++) {
            probabilities[i] /= sum;
        }
        for (int i = 1; i < problemLength; i++) {
            probabilities[i] += probabilities[i - 1];
        }
    }

    @Override
    public void makeIteration() {
        iterCount++;
        List<Integer> bestPatch = new ArrayList<>(4);
        List<Integer> patch = new ArrayList<>(4);
        int bestFitness = -1;
        for (int count = 0; count < lambda; count++) {
            double prob = rand.nextDouble();
            int x = binSearch(prob);
            int i = 0;
            while (i < x) {
                int pos = rand.nextInt(problemLength);
                if (!flipped[pos]) {
                    patch.add(pos);
                    flipped[pos] = true;
                    i++;
                }
            }
            for (int p : patch) {
                flipped[p] = false;
            }
            int patchFitness = problem.calculatePatchFitness(patch);
            if (patchFitness >= bestFitness) {
                bestFitness = patchFitness;
                bestPatch.clear();
                bestPatch.addAll(patch);
            }
            patch.clear();
        }

        if (bestFitness >= problem.getFitness()) {
            problem.applyPatch(bestPatch, bestFitness);
        }
    }

    private int binSearch(double prob) {
        int l = 0;
        int r = problemLength;
        while (l != r) {
            int m = (l + r) / 2;
            if (prob < probabilities[m]) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l + 1;
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(problem.getFitness());
    }

    /*
        Returns tail parameter --- beta
     */
    @Override
    public double getMutationRate() {
        return beta;
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
