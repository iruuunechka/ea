package algo;

import problem.Problem;
import utils.BestCalculatedPatch;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TwoRateByFlipBits implements Algorithm{

    private final double halfCoeff = 1.1;
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private int iterCount = 0;
    private double flipBits;
    private final double precision;
    private final double lowerBoundFlipBits;
    private final double upperBoundFlipBits;

    private double bitsFromMutation(double mutation) {
        return problemLength * mutation + Math.pow(1 - mutation, problemLength);
    }

    public TwoRateByFlipBits(double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        problemLength = problem.getLength();
        this.mutationRate = 1.0 / problemLength;
        this.lambda = lambda;

        this.lowerBound = lowerBound;
        lowerBoundFlipBits = Math.max(1.0 * halfCoeff, bitsFromMutation(lowerBound)*halfCoeff);
        upperBoundFlipBits = bitsFromMutation(0.25);

        this.precision = lowerBound;
        this.flipBits = 1.0 + Math.pow(1 - mutationRate, problemLength);

        rand = ThreadLocalRandom.current();
    }

    private double calculateMutation(double expectedFlipBits) {
        double p = 0.25;
        double diff = p;
        double r = 0.25;
        double l = 0.0;
        while (Math.abs(diff) > precision) {
            p = (r + l) / 2;
            diff = p * problemLength + Math.pow(1.0 - p, problemLength) - expectedFlipBits;
            if (diff > 0) {
                r = p;
            } else {
                l = p;
            }
        }
        return p;
    }

    @Override
    public void makeIteration() {
        iterCount++;
        double halfMutRate = calculateMutation(flipBits / halfCoeff);
        double twiceMutRate = calculateMutation(flipBits * halfCoeff);

        BestCalculatedPatch bpHalf = new BestCalculatedPatch(halfMutRate, lambda / 2, problem);
        BestCalculatedPatch bpMult = new BestCalculatedPatch(twiceMutRate, lambda / 2, problem);
        double newMutationRate;
        double newFlipBits;
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            }
            newMutationRate = halfMutRate;
            newFlipBits = flipBits / halfCoeff;
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            }
            newMutationRate = twiceMutRate;
            newFlipBits = flipBits * halfCoeff;
        } else {
            if (rand.nextBoolean()) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                }
                newMutationRate = halfMutRate;
                newFlipBits = flipBits / halfCoeff;
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                }
                newMutationRate = twiceMutRate;
                newFlipBits = flipBits * halfCoeff;
            }
        }
        if (rand.nextBoolean()) {
            if (rand.nextBoolean()) {
                mutationRate = halfMutRate;
                flipBits /= halfCoeff;
            } else {
                mutationRate = twiceMutRate;
                flipBits *= halfCoeff;
            }
        } else {
            mutationRate = newMutationRate;
            flipBits = newFlipBits;
        }

        flipBits = Math.min(Math.max(lowerBoundFlipBits, flipBits), upperBoundFlipBits);
        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(iterCount + " " + problem.getFitness() + " " + mutationRate + " " + flipBits);
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
        return null;
    }

    @Override
    public int getOptimum() {
        return problem.getOptimum();
    }
}
