package problem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OneMax implements Problem {
    private boolean[] individual;
    private int fitness;

    public OneMax(int n) {
        individual = new boolean[n];
        Random rand = new Random();
        fitness = 0;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
            if (individual[i]) {
                fitness++;
            }
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = fitness;
        for (Integer i : patch) {
            if (individual[i]) {
                newFitness--;
            } else {
                newFitness++;
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
}
