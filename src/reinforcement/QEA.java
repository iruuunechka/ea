package reinforcement;

import algo.Algorithm;
import problem.Problem;
import reinforcement.agent.Agent;
import reinforcement.reward.Reward;
import reinforcement.state.State;
import utils.PatchCalcUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QEA implements Algorithm {

    private double mutationRate;
    private final double a; //a = 2
    private final double b; //b = 0.5
    private final double lowerBound; // 1 / problemLength or 1 / (problemLength^2)
    private final int lambda;
    private final boolean strict;

    private final Problem problem;
    private final int problemLength;

    private final Reward reward;
    private final State state;
    private final Agent agent;

    private final Random rand;
    private int iterCount = 0;

    private int curState = -1;
    private int curAction = -1;

    public QEA(double mutationRate, double a, double b, boolean strict, double lowerBound, int lambda, Problem problem, Reward reward, State state, Agent agent) {
        this.mutationRate = mutationRate;
        this.a = a;
        this.b = b;
        this.lowerBound = lowerBound;
        this.lambda = lambda;
        this.problem = problem;
        this.problemLength = problem.getLength();
        this.rand = ThreadLocalRandom.current();
        this.reward = reward;
        this.state = state;
        this.agent = agent;
        this.strict = strict;
    }

    @Override
    public void makeIteration() {
        List<Integer> bestPatch = null;
        int bestFitness = -1;
        int numberOfBetter = 0;
        for (int i = 0; i < lambda; ++i) {
            List<Integer> patch = PatchCalcUtil.createPatch(mutationRate, problemLength);
            int fitness = problem.calculatePatchFitness(patch);
            if (strict ? (fitness > problem.getFitness()) : (fitness >= problem.getFitness())) {
                numberOfBetter++;
            }

            if (fitness >= bestFitness) {
                bestFitness = fitness;
                bestPatch = patch;
            }
        }
        double newReward = reward.calculate(bestFitness, problem.getFitness());
        int newState = state.calculate(numberOfBetter);

        if (iterCount != 0) {
            agent.updateExperience(curState, newState, curAction, newReward);
        }
        curState = newState;
        curAction = agent.chooseAction(curState);
        if (curAction == -1) {
            if (rand.nextBoolean()) {
                curAction = 0;
            } else {
                curAction = 1;
            }
        }
        if (bestFitness >= problem.getFitness()) {
            problem.applyPatch(bestPatch, bestFitness);
        }
        if (curAction == 0) {
            mutationRate = Math.min(0.5, a * mutationRate);
        } else {
            mutationRate = Math.max(lowerBound, b * mutationRate);
        }
        iterCount++;

    }

    @Override
    public boolean isFinished() {
        return problem.isOptimized();
    }

    @Override
    public void printInfo() {
        System.out.println(iterCount + " " + problem.getFitness() + " " + curState + " " + curAction);
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
