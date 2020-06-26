package problem;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ruggedness implements Problem {
    private boolean[] individual;
    private final int length;
    private int fitness;
    private int onesCount;
    private final int r;
    private final int optimum;

    public Ruggedness(int n, int r) {
        individual = new boolean[n];
        length = n;
        Random rand = ThreadLocalRandom.current();
        this.r = r;
        int om = 0;
        optimum = countFitness(n);
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
            if (individual[i]) {
                om++;
            }
        }
        onesCount = om;
        fitness = countFitness(om);
    }

    private int countFitness(int om) {
        if (r == 1) {
//            if (om == length) {
//                return om / 2 + 2;
//            }
                return om / 2 + 1 + om % 2;
//            }
        } else {
//            if (om == length) {
//                return length;
//            }
            return r * (om / r) + r - 1 - om % r;
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newOnesCount = onesCount;
        for (Integer i : patch) {
            if (individual[i]) {
                newOnesCount--;
            } else {
                newOnesCount++;
            }
        }
        return countFitness(newOnesCount);
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (Integer i : patch) {
            if (individual[i]) {
                onesCount--;
            } else {
                onesCount++;
            }
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
        return length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == optimum;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
