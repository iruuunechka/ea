package algo;

import problem.Problem;
import utils.BestCalculatedPatch;
import utils.BestCalculatedPatchNoShiftFitnessCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoRateNoShiftFitnessCount implements Algorithm {
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private long fitnessCount = 0;
    private int iterCount = 0;


    public TwoRateNoShiftFitnessCount(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        rand = new Random();
    }

    @Override
    public void makeIteration() {
        iterCount++;
        BestCalculatedPatchNoShiftFitnessCount bpHalf = new BestCalculatedPatchNoShiftFitnessCount(mutationRate / 2, lambda / 2, problem, rand);
        BestCalculatedPatchNoShiftFitnessCount bpMult = new BestCalculatedPatchNoShiftFitnessCount(mutationRate * 2, lambda / 2, problem, rand);
        fitnessCount += bpHalf.fitnessCount + bpMult.fitnessCount;
        double newMutationRate = mutationRate;
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            }
            newMutationRate = mutationRate / 2;
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            }
            newMutationRate = mutationRate * 2;
        } else { // что если равны?
            if (rand.nextBoolean()) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                }
                newMutationRate = mutationRate / 2;
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                }
                newMutationRate = mutationRate * 2;
            }
        }
        if (rand.nextBoolean()) {
            if (rand.nextBoolean()) {
                mutationRate = mutationRate / 2;
            } else {
                mutationRate = mutationRate * 2;
            }
        } else {
            mutationRate = newMutationRate;
        }

        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println();
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
        return fitnessCount;
    }

    @Override
    public int getIterCount() {
        return iterCount;
    }

    @Override
    public String getProblemInfo() { return problem.getInfo();}

}

