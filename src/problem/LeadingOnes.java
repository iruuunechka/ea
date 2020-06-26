package problem;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LeadingOnes implements Problem {
    private boolean[] individual;
    private int fitness;

    public LeadingOnes(int n) {
        individual = new boolean[n];
        Random rand = ThreadLocalRandom.current();
        fitness = 0;
        boolean fitnessCounted = false;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
            if (!fitnessCounted) {
                if (individual[i]) {
                    fitness++;
                } else {
                    fitnessCounted = true;
                }
            }
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = fitness;
        for (int i : patch) {
            if (newFitness < i) {
                return newFitness;
            } else if (individual[i]) { //newFitness > i;
                return i;
            } else if (!individual[i]) { //newFitness == i
                newFitness++;
                while (newFitness < individual.length && individual[newFitness]) {
                    newFitness++;
                }
            }
        }
        return newFitness;
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (Integer i : patch) {
            individual[i] = !individual[i];
        }
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getLength() {
        return individual.length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == individual.length;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
