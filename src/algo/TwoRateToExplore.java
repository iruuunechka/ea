package algo;

import problem.Problem;
import utils.PatchCalcUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoRateToExplore implements Algorithm {
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private String info = "";
    private boolean oneBit = false; //true if one random bit inverted in createPatch

    public TwoRateToExplore(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        rand = new Random();
    }

    @Override
    public void makeIteration() {
        BestCalculatedPatch bpHalf = getHalfBest(mutationRate / 2);
        BestCalculatedPatch bpMult = getHalfBest(mutationRate * 2);
        double newMutationRate = mutationRate;
        info = "";
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            }
            if (! (bpHalf.isOneBit && bpMult.isOneBit)) {
                newMutationRate = mutationRate / 2;
                info = "div     calc" + (bpHalf.isOneBit ? " onebit" : "       ") + (bpMult.isOneBit ? " onebit" : "       ");
            }
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            }
            if (! (bpHalf.isOneBit && bpMult.isOneBit)) {
                newMutationRate = mutationRate * 2;
                info = "mult    calc" + (bpMult.isOneBit ? " onebit" : "       ") + (bpHalf.isOneBit ? " onebit" : "       ");
            }
        } else {
            if (rand.nextDouble() < 0.5) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                }
                if (! (bpHalf.isOneBit && bpMult.isOneBit)) {
                    newMutationRate = mutationRate / 2;
                    info = "div  eq calc" + (bpHalf.isOneBit ? " onebit" : "       ") + (bpMult.isOneBit ? " onebit" : "       ");
                }
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                }
                if (! (bpHalf.isOneBit && bpMult.isOneBit)) {
                    newMutationRate = mutationRate * 2;
                    info = "mult eq calc" + (bpMult.isOneBit ? " onebit" : "       ") + (bpHalf.isOneBit ? " onebit" : "       ");
                }
            }
        }
        if (newMutationRate == mutationRate) { // if both are one random bit mutation
//            if (rand.nextDouble() < 0.1) {
//                newMutationRate = mutationRate / 2; //убрали уменьшение мутации когда она и так мала
//                info = "unchan both onebit rand   ";
//            } else {
                newMutationRate = mutationRate * 2;
                info = "mult both onebit rand     ";
//            }
        }
        if (rand.nextDouble() < 0.5) {
            if (rand.nextDouble() < 0.5) {
                mutationRate = mutationRate / 2;
                info = "div     rand              ";
            } else {
                mutationRate = mutationRate * 2;
                info = "mult    rand              ";
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
        System.out.println(info + " " + problem.getFitness() + " " + mutationRate);
    }

    @Override
    public double getMutationRate() {
        return mutationRate;
    }

    @Override
    public int getFitness() {
        return problem.getFitness();
    }

    private BestCalculatedPatch getHalfBest(double mutation) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        boolean bestFitnessOneBit = false;
        for (int i = 0; i < lambda / 2; ++i) {
            List<Integer> patch = createPatch(mutation, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
                bestFitnessOneBit = oneBit;
            }
        }
        return new BestCalculatedPatch(bestPatch, bestFitness, bestFitnessOneBit);
    }

    private class BestCalculatedPatch {
        List<Integer> patch;
        int fitness;
        boolean isOneBit;

        BestCalculatedPatch(List<Integer> patch, int fitness, boolean isOneBit) {
            this.patch = patch;
            this.fitness = fitness;
            this.isOneBit = isOneBit;
        }
    }

    private List<Integer> createPatch (double mutation, int problemLength) {
        List<Integer> patch = new ArrayList<>(16);
        int i = getNextIndex(-1, mutation);
        while (i < problemLength) {
            patch.add(i);
            i = getNextIndex(i, mutation);
        }

        if (patch.isEmpty()) {
            patch.add(rand.nextInt(problemLength));
            oneBit = true; //marker to print info if 1 random bit inverted
        } else {
            oneBit = false; //marker to print info if mutation occured
        }
        return patch;

    }

    private int getNextIndex(int curIndex, double mutation) {
        return curIndex + 1 + (int) (Math.log(rand.nextDouble()) / Math.log(1.0 - mutation));
    }
}
