package algo;

import problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoRate implements Algorithm {
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    public int decreaseCount = 0;
    public List<String> decreaseCountInfo = new ArrayList<>();
    public int increaseCount = 0;
    public int equalCount = 0;

    public TwoRate(double r, double lowerBound, int lambda, Problem problem) {
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
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            } else {
                decreaseCount++;
                decreaseCountInfo.add(problem.getFitness() + "," + mutationRate + "'" + (problem.getFitness() - bpHalf.fitness) + "," + (problem.getFitness() - bpMult.fitness));
            }
            newMutationRate = mutationRate / 2;
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            } else {
                increaseCount++;
            }
            newMutationRate = mutationRate * 2;
        } else { // что если равны?
            if (rand.nextBoolean()) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                } else {
                    equalCount++;
                }
                newMutationRate = mutationRate / 2;
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                } else {
                    equalCount++;
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

    private BestCalculatedPatch getHalfBest(double mutation) {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        for (int i = 0; i < lambda / 2; ++i) {
            List<Integer> patch = Utils.createPatch(mutation, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        return new BestCalculatedPatch(bestPatch, bestFitness);
    }
//@Override
//    public void makeIteration() {
//        BestCalculatedPatch bpHalf = getHalfBest(mutationRate / 2);
//        BestCalculatedPatch bpMult = getHalfBest(mutationRate * 2);
//        double newMutationRate = mutationRate;
//        if (bpHalf.fitness > bpMult.fitness) {
//            problem.applyPatch(bpHalf.patch, bpHalf.fitness);
//            newMutationRate = mutationRate / 2;
//        } else if (bpHalf.fitness < bpMult.fitness) {
//            problem.applyPatch(bpMult.patch, bpMult.fitness);
//            newMutationRate = mutationRate * 2;
//        } else { // что если равны?
//            if (!(bpHalf.fitness == problem.getFitness())) { // если улучшилось решение
//                if (rand.nextBoolean()) {
//                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
//                    newMutationRate = mutationRate / 2;
//                } else {
//                    problem.applyPatch(bpMult.patch, bpMult.fitness);
//                    newMutationRate = mutationRate * 2;
//                }
//            }
//        }
//        if (rand.nextBoolean()) {
//            if (rand.nextBoolean()) {
//                mutationRate = mutationRate / 2;
//            } else {
//                mutationRate = mutationRate * 2;
//            }
//        } else {
//            mutationRate = newMutationRate;
//        }
//
//        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
//    }
//
//    private BestCalculatedPatch getHalfBest(double mutation) {
//        List<Integer> bestPatch = null;
//        int bestFitness = problem.getFitness();
//        for (int i = 0; i < lambda / 2; ++i) {
//            List<Integer> patch = createPatch(mutation);
//            int fitness = problem.calculatePatchFitness(patch);
//            if (fitness > bestFitness) {
//                //а что если равен, и вообще мы не нашли в итоге лучшую особь, но находили такую же. делаем ли мы изменение?
//                bestFitness = fitness;
//                bestPatch = patch;
//            }
//        }
//        return new BestCalculatedPatch(bestPatch, bestFitness);
//    }

//    private List<Integer> createPatch(double mutation) {
//        List<Integer> patch = new ArrayList<>();
//        for (int i = 0; i < problemLength; i++) {
//            if (rand.nextDouble() < mutation) {
//                patch.add(i);
//            }
//        }
//        if (patch.isEmpty()) {
//            patch.add(rand.nextInt(problemLength));
//        }
//        return patch;
//    }

    private class BestCalculatedPatch {
        List<Integer> patch;
        int fitness;

        BestCalculatedPatch(List<Integer> patch, int fitness) {
            this.patch = patch;
            this.fitness = fitness;
        }
    }
}
