package algo;

import problem.Problem;
import utils.BestCalculatedPatchOneBitPercent;

import java.util.Random;

public class TwoRateNew implements Algorithm {
    private double mutationRate;
    private final double lowerBound; // 2.0 / problemLength or 2.0 / (problemLength^2)
    private final int lambda;

    private final Problem problem;
    private final int problemLength;

    private final Random rand;

    private final int maxStagnation = 10;
    private int curStagnation = 0;

    private int iterCount = 0;


    private String info = "";
    private boolean oneBit = false; //true if one random bit inverted in createPatch

    public TwoRateNew(double r, double lowerBound, int lambda, Problem problem) {
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
        BestCalculatedPatchOneBitPercent bpHalf = new BestCalculatedPatchOneBitPercent(mutationRate / 2, lambda / 2, problem, rand);
        BestCalculatedPatchOneBitPercent bpMult = new BestCalculatedPatchOneBitPercent(mutationRate * 2, lambda / 2, problem, rand);
        double newMutationRate = mutationRate;
        info = "";
        if (bpHalf.fitness > problem.getFitness() || bpMult.fitness > problem.getFitness()) {
            curStagnation = 0;
        } else {
            curStagnation++;
        }

        if (curStagnation > maxStagnation) {
            curStagnation = 0;
            BestCalculatedPatchOneBitPercent bpHalfNew = new BestCalculatedPatchOneBitPercent(Math.max(mutationRate / 8, lowerBound), lambda / 2, problem, rand);
            BestCalculatedPatchOneBitPercent bpMultNew = new BestCalculatedPatchOneBitPercent(Math.min(mutationRate * 8, 0.25), lambda / 2, problem, rand);
            iterCount++;
            if (Math.max(bpHalfNew.fitness, bpMultNew.fitness) > problem.getFitness()) {
                if (bpHalfNew.fitness == bpMultNew.fitness) {
                    double prob = rand.nextDouble();
                    if (prob < 0.5) {
                        mutationRate /= 8;
                    } else {
                        mutationRate *= 8;
                    }
                } else if (bpHalfNew.fitness > problem.getFitness()) {
                    mutationRate /= 8;
                } else if (bpMultNew.fitness > problem.getFitness()) {
                    mutationRate *= 8;
                }
                mutationRate = Math.min(Math.max(lowerBound, mutationRate), 0.25);
                return;
            }
        }
        if (bpHalf.fitness > bpMult.fitness) {
            if (bpHalf.fitness >= problem.getFitness()) {
                problem.applyPatch(bpHalf.patch, bpHalf.fitness);
            }
            if (! (bpHalf.oneBitPercent >= 0.5 && bpMult.oneBitPercent >= 0.5)) {
                newMutationRate = mutationRate / 2;
                info = "div     calc" + (bpHalf.oneBitPercent >= 0.5 ? " onebit" : "       ") + (bpMult.oneBitPercent >= 0.5 ? " onebit" : "       ");
            }
        } else if (bpHalf.fitness < bpMult.fitness) {
            if (bpMult.fitness >= problem.getFitness()) {
                problem.applyPatch(bpMult.patch, bpMult.fitness);
            }
            if (! (bpHalf.oneBitPercent >= 0.5 && bpMult.oneBitPercent >= 0.5)) {
                newMutationRate = mutationRate * 2;
                info = "mult    calc" + (bpMult.oneBitPercent >= 0.5 ? " onebit" : "       ") + (bpHalf.oneBitPercent >= 0.5 ? " onebit" : "       ");
            }
        } else {
            if (rand.nextDouble() < 0.5) {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpHalf.patch, bpHalf.fitness);
                }
                if (! (bpHalf.oneBitPercent >= 0.5 && bpMult.oneBitPercent >= 0.5)) {
                    newMutationRate = mutationRate / 2;
                    info = "div  eq calc" + (bpHalf.oneBitPercent >= 0.5 ? " onebit" : "       ") + (bpMult.oneBitPercent >= 0.5 ? " onebit" : "       ");
                }
            } else {
                if (bpHalf.fitness >= problem.getFitness()) {
                    problem.applyPatch(bpMult.patch, bpMult.fitness);
                }
                if (! (bpHalf.oneBitPercent >= 0.5 && bpMult.oneBitPercent >= 0.5)) {
                    newMutationRate = mutationRate * 2;
                    info = "mult eq calc" + (bpMult.oneBitPercent >= 0.5 ? " onebit" : "       ") + (bpHalf.oneBitPercent >= 0.5 ? " onebit" : "       ");
                }
            }
        }
        if (newMutationRate == mutationRate) { // if both are one random bit mutation
//            if (rand.nextDouble() < 0.1) {
//                newMutationRate = mutationRate / 2; //убрали уменьшение мутации когда она и так мала
//                info = "unchan both onebit rand   ";
//            } else {
            newMutationRate = mutationRate * (2.66 * Math.max(bpHalf.oneBitPercent, bpMult.oneBitPercent));
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

}
