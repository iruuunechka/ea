package algo;

import problem.Problem;
import utils.BestCalculatedPatch;
import utils.BestCalculatedPatchOneBitMarker;
import utils.BestCalculatedPatchOneBitMarkerByPercentage;
import utils.BestCalculatedPatchOneBitPercent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TwoRateStagnationDetection implements Algorithm{
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private int iterCount = 0;
    private final int maxStagnation;
    private final int probToCheck = 8;
    private int curStagnation = 0;


    private String info = "";
    private boolean oneBit = false; //true if one random bit inverted in createPatch

    public TwoRateStagnationDetection(double r, double lowerBound, int lambda, Problem problem) {
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.mutationRate = r / problemLength;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        rand = ThreadLocalRandom.current();
        maxStagnation = (int) Math.max(5, 60/(Math.log(lambda) / Math.log(2)));
    }

    @Override
    public void makeIteration() {
        iterCount++;
        BestCalculatedPatchOneBitMarker bpHalf = new BestCalculatedPatchOneBitMarkerByPercentage(mutationRate / 2, lambda / 2, problem, rand);
        BestCalculatedPatchOneBitMarker bpMult = new BestCalculatedPatchOneBitMarkerByPercentage(mutationRate * 2, lambda / 2, problem, rand);
        double newMutationRate = mutationRate;
        info = "";

        if (bpHalf.fitness > problem.getFitness() || bpMult.fitness > problem.getFitness()) {
            curStagnation = 0;
        } else {
            curStagnation++;
        }

        if (curStagnation > maxStagnation) {
            curStagnation = 0;
            BestCalculatedPatchOneBitMarker[] mutationRes= new BestCalculatedPatchOneBitMarker[probToCheck];
            int curProb = -1;
            double[] checkedProbs = new double[probToCheck];
            double mid = Math.abs((Math.log10(lowerBound) - Math.log10(0.25)) / 2);
            double step = mid / 2;
            double stepMin = mid - step;
            double stepMax = mid + step;
            while (curProb < probToCheck - 1) {
                checkedProbs[++curProb] = Math.pow(10, stepMin + Math.log10(lowerBound));
                mutationRes[curProb] = new BestCalculatedPatchOneBitMarkerByPercentage(checkedProbs[curProb], Math.max(lambda / probToCheck, 1), problem, rand);
                checkedProbs[++curProb] = Math.pow(10, stepMax + Math.log10(lowerBound));
                mutationRes[curProb] = new BestCalculatedPatchOneBitMarkerByPercentage(checkedProbs[curProb], Math.max(lambda / probToCheck, 1), problem, rand);
                if (mutationRes[curProb].fitness > mutationRes[curProb - 1].fitness) {
                    mid = stepMax;
                    step /= 2;
                    stepMin = mid - step;
                    stepMax = mid + step;
                } else if (mutationRes[curProb].fitness < mutationRes[curProb - 1].fitness) {
                    mid = stepMin;
                    step /= 2;
                    stepMin = mid - step;
                    stepMax = mid + step;
                } else {
                    step /= 2;
                    stepMin = mid - step;
                    stepMax = mid + step;
                }
            }
            if (lambda / probToCheck >= 1) {
                iterCount++;
            } else {
                iterCount += probToCheck / lambda;
            }

            int maxRes = Integer.MIN_VALUE;
            BestCalculatedPatchOneBitMarker maxPatch = null;
            for (BestCalculatedPatchOneBitMarker bp : mutationRes) {
                if (bp.fitness >= maxRes) {
                    maxRes = bp.fitness;
                    maxPatch = bp;
                }
            }

            if (maxRes >= problem.getFitness()) {
                problem.applyPatch(maxPatch.patch, maxPatch.fitness);
                List<Double> optimalProb = new ArrayList<>();
                for (int i = 0; i < mutationRes.length; ++i) {
                    if (mutationRes[i].fitness == maxRes) {
                        optimalProb.add(checkedProbs[i]);
                    }
                }

                double prob = rand.nextDouble();
                int i = 1;
                while (prob > (1.0 / optimalProb.size()) * i) {
                    i++;
                }

                mutationRate = optimalProb.get(i - 1);

//                mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
                assert (mutationRate >= lowerBound && mutationRate <= 0.25);
                return;
            }
        }
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
