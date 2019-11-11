package algo;

import problem.Problem;
import utils.BestCalculatedPatch;
import utils.BestCalculatedPatchMedAverage;
import utils.PatchCalcUtil;

import java.util.*;

public class AdaptiveTwoRate implements Algorithm{

    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;
    private final int[] fitnessOfPatches; //created once for calculating median

    private int iterCount = 0;
    private double mutationRate;


    public int decreaseCount = 0;
    public List<String> decreaseCountInfo = new ArrayList<>();
    public int increaseCount = 0;
    public int equalCount = 0;
    public int almostTheSame = 0;
    public double prob;

    public AdaptiveTwoRate(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        rand = new Random();
        fitnessOfPatches = new int[lambda / 2];
    }

    @Override
    public void makeIteration() {
        iterCount++;
        BestCalculatedPatchMedAverage bpHalf = new BestCalculatedPatchMedAverage(mutationRate / 2, lambda / 2, problem);
        BestCalculatedPatchMedAverage bpMult = new BestCalculatedPatchMedAverage(mutationRate * 2, lambda / 2, problem);
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
        } else {
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
        double diff = Math.abs(bpHalf.average - bpMult.average);
        prob = Math.pow(1.3, -diff / 3.5 - 1.6);
        System.out.println(problem.getFitness() + " " + diff + " " + prob);
        if (rand.nextDouble() < prob) {
            almostTheSame++;
            if (rand.nextDouble() < prob + (0.3 * problem.getFitness() / problemLength)) {
                mutationRate = mutationRate * 2;
            } else {
                mutationRate = mutationRate / 2;
            }
        } else {
            mutationRate = newMutationRate;
        }

        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
    }

//    все попытки с настройкой
//    @Override
//    public void makeIteration() {
//        curIter++;
//        BestCalculatedPatch bpHalf = getHalfBest(mutationRate / 2);
//        BestCalculatedPatch bpMult = getHalfBest(mutationRate * 2);
//        double newMutationRate = mutationRate;
//        if (bpHalf.fitness > bpMult.fitness) {
//            if (bpHalf.fitness >= problem.getFitness()) {
//                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
//            } else {
////                decreaseCount++;
////                decreaseCountInfo.add(problem.getFitness() + "," + mutationRate + "'" + (problem.getFitness() - bpHalf.fitness) + "," + (problem.getFitness() - bpMult.fitness));
//            }
//            newMutationRate = mutationRate / 2;
//        } else if (bpHalf.fitness < bpMult.fitness) {
//            if (bpMult.fitness >= problem.getFitness()) {
//                problem.applyPatch(bpMult.patch, bpMult.fitness);
//            } else {
////                increaseCount++;
//            }
//            newMutationRate = mutationRate * 2;
//        } else { // что если равны?
//            if (rand.nextBoolean()) {
//                if (bpHalf.fitness >= problem.getFitness()) {
//                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
//                } else {
////                    equalCount++;
//                }
//                newMutationRate = mutationRate / 2;
//            } else {
//                if (bpHalf.fitness >= problem.getFitness()) {
//                    problem.applyPatch(bpMult.patch, bpMult.fitness);
//                } else {
////                    equalCount++;
//                }
//                newMutationRate = mutationRate * 2;
//            }
//        }
////        boolean almostEqual = Math.abs(bpHalf.median - bpMult.median) < 1;
////        if (almostEqual) {
////            almostTheSame++;
////        }
////        if (rand.nextBoolean() || almostEqual) {
//        double diff = Math.abs(bpHalf.average - bpMult.average);
////        prob = Math.pow(1.3, -diff - 2);
////        prob = Math.pow(1.3, (-3.5 * diff / Math.pow(problem.getFitness(), 0.3)) - 2); непонятно как уравновесить diff и фитнес чтоб не скатываться совсем в 0
//
////        prob = Math.exp(-diff / (problemLength - problem.getFitness()) - 0.6); // идея что в конце меньше рандома должно быть работает не очень.
////        prob = Math.pow(1.3, -diff / 5 - 2);
//        prob = Math.pow(1.3, -diff / 3.5 - 1.6);
////        prob = Math.pow(1.3, -diff / 2 - 1.7);
//
//        System.out.println(problem.getFitness() + " " + diff + " " + prob);
//        if (rand.nextDouble() < prob) {
//            almostTheSame++;
////            if (rand.nextBoolean()) {
//            if (rand.nextDouble() < prob + (0.3 * problem.getFitness() / problemLength)) {
//                mutationRate = mutationRate * 2;
//            } else {
//                mutationRate = mutationRate / 2;
//            }
//        } else {
//            mutationRate = newMutationRate;
//        }
//
//        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
//    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(//"decCou: " + decreaseCount + " incCou: " + increaseCount + " eqCou: " + equalCount +
                "sameCou: " + almostTheSame);
//        for (String s : decreaseCountInfo) {
//            System.out.print(s + " ");
//        }
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
        return iterCount * lambda;
    }

}
