package algo;

import problem.Problem;
import utils.BestCalculatedPatch;
import utils.PatchCalcUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AdaptiveDivTwoRate implements Algorithm {
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;
    private final int[] fitnessOfPatches; //created once for calculating median

    private int curIter = 0;
    private double mutationRate;


    public int calcMultCou = 0;
    public int calcDivCou = 0;
    public int randDivCou = 0;
    public int randMultCou = 0;

    public double prob;

    public AdaptiveDivTwoRate(double r, double lowerBound, int lambda, Problem problem) {
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
        curIter++;
        BestCalculatedPatch bpHalf = getHalfBest(mutationRate / 2);
        BestCalculatedPatch bpMult = getHalfBest(mutationRate * 2);
        double newMutationRate = mutationRate;
        double diff = Math.abs(bpHalf.average - bpMult.average);
        double rateMult = Math.max(Math.exp(-diff/7 + 0.95), 1.2); // на этой получилось лучше tworate//Math.max(1.3, Math.pow(3.7, -diff/15 +0.7)); //Math.pow(8, diff / 10);
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            }
            newMutationRate = mutationRate / rateMult;
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            }
            newMutationRate = mutationRate * rateMult;
        } else {
            if (rand.nextBoolean()) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                }
                newMutationRate = mutationRate / rateMult;
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                }
                newMutationRate = mutationRate * rateMult;
            }
        }
        prob = Math.pow(1.3, -diff / 3.5 - 1.6);
        String s = problem.getFitness() + " " + diff + " " + prob + " " + mutationRate + " " + rateMult + " ";
        if (rand.nextDouble() < prob) {

            if (rand.nextDouble() < 0.5) {
                mutationRate = mutationRate * rateMult;
                s += "mult rand";
                randMultCou++;
            } else {
                mutationRate = mutationRate / rateMult;
                s += "div rand";
                randDivCou++;
            }
        } else {
            if (newMutationRate < mutationRate) {
                s += " calc div";
                calcDivCou++;
            } else {
                s += "calc mult";
                calcMultCou++;
            }
            mutationRate = newMutationRate;
        }
//        System.out.println(s);
        mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println("calcMultCou " + calcMultCou + " calcDivCou: " + calcDivCou + " randMultCou: " + randMultCou + " randDivCou: " +randDivCou);
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
        double average = 0;
        for (int i = 0; i < lambda / 2; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutation, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
//            fitnessOfPatches[i] = fitness; //убрать если не надо считать медиану
            average = (i == 0) ? fitness : (average * i + fitness) / (i + 1);
            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
//        Arrays.sort(fitnessOfPatches); //убрать если не надо считать медиану
        return new BestCalculatedPatch(bestPatch, bestFitness, fitnessOfPatches[lambda / 4], average);
    }



}
