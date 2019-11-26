package algo;

import problem.Problem;
import utils.BestCalculatedPatchOneBitMarker;
import utils.BestCalculatedPatchOneBitMarkerByPercentage;

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

    private int iterCount = 0;

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
        iterCount++;
        BestCalculatedPatchOneBitMarker bpHalf = new BestCalculatedPatchOneBitMarker(mutationRate / 2, lambda / 2, problem, rand);
        BestCalculatedPatchOneBitMarker bpMult = new BestCalculatedPatchOneBitMarker(mutationRate * 2, lambda / 2, problem, rand);
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
        double prob = rand.nextDouble();
        if (prob < 0.25) {
            mutationRate = mutationRate / 2;
            info = "div     rand              ";
        } else if (prob < 0.5){
                mutationRate = mutationRate * 2;
                info = "mult    rand              ";
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
        System.out.println(iterCount + " " + info + " " + problem.getFitness() + " " + mutationRate);
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

}
