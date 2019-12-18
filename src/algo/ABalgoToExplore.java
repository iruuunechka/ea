package algo;

import problem.Problem;
import utils.BestCalculatedPatchOneBitMarker;
import utils.PatchCalcUtil;

import java.util.List;
import java.util.Random;

public class ABalgoToExplore implements Algorithm {
    private double mutationRate;
    private final double a; //a = 2
    private final double b; //b = 0.5
    private final double lowerBound; // 1 / problemLength or 1 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;
    private int iterCount = 0;


    private String info = "";

    public ABalgoToExplore(double mutationRate, double a, double b, double lowerBound, int lambda, Problem problem) {
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
        info = "";
        iterCount++;
        BestCalculatedPatchOneBitMarker bestPatch = new BestCalculatedPatchOneBitMarker(null, -1, false);
        int numberOfBetter = 0;
        int bestFitness = -1;
        boolean bestNotOneBit = false;
        int notOneBitCount = 0;
        int bestCount = 0;
        for (int i = 0; i < lambda; ++i) {
            BestCalculatedPatchOneBitMarker calcPatch = new BestCalculatedPatchOneBitMarker(mutationRate, problem, rand);
            if (calcPatch.fitness >= problem.getFitness()) { //> or >= and why?
                numberOfBetter++;
            }
            if (calcPatch.fitness > bestPatch.fitness) {
                bestPatch = calcPatch;
            }
            if (calcPatch.fitness > bestFitness) {
                bestFitness = calcPatch.fitness;
                bestNotOneBit = !calcPatch.isOneBit;
                notOneBitCount = !calcPatch.isOneBit ? 1 : 0;
                bestCount = 1;
            }
            if (calcPatch.fitness == bestFitness) {
                bestNotOneBit = bestNotOneBit || !calcPatch.isOneBit;
                if (!calcPatch.isOneBit) {
                    notOneBitCount++;
                }
                bestCount++;
            }
        }
        if (bestPatch.patch != null && bestPatch.fitness >= problem.getFitness()) { //учитываем или нет патчи что хуже исходного фитнеса?
            problem.applyPatch(bestPatch.patch, bestPatch.fitness);
        }
        info += bestPatch.fitness;
        if (numberOfBetter >= 0.05 * lambda) {
            mutationRate = Math.min(0.5, a * mutationRate);
            info += " inc" + mutationRate;
        } else if (!bestPatch.isOneBit) { // bestNotOneBit
            mutationRate = Math.max(lowerBound, b * mutationRate);
            info += " dec" + mutationRate;
        } else {
//            if (rand.nextDouble() < 0.75) {
                mutationRate = Math.min(0.5, a * mutationRate);
                info += " rin" + mutationRate;
//            } else {
//                System.out.println(bestPatch.fitness + " unchanged " + " number of better: " + numberOfBetter + " was onebit: " + bestPatch.isOneBit + " hasNotOB " + hasNotOneBit);
//            }
        }
        info += " better: " + numberOfBetter + " OB: " + bestPatch.isOneBit + " hasNotOB " + bestNotOneBit + " notOBcou " + notOneBitCount + " bestCou " + bestCount;
    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(info);
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