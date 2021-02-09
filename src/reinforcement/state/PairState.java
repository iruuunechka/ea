package reinforcement.state;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PairState implements State {
    private final int lambda;
    private int numberOfBetter;
    private int diversityRank;
    private final Set<Integer> generationFitness;

    public PairState(int numberOfBetter, int lambda) {
        this.lambda = lambda;
        this.numberOfBetter = numberOfBetter;
        this.diversityRank = -1;
        this.generationFitness = new HashSet<>();
    }

    public PairState (int lambda) {
        this(-1, lambda);
    }

    @Override
    public void calculate(int numberOfBetter) {
        this.numberOfBetter = numberOfBetter;
        switch (generationFitness.size()) {
            case 1 -> diversityRank = 1;
            case 2 -> diversityRank = 2;
            default -> diversityRank = 3;
        }
        if (generationFitness.size() == lambda) {
            diversityRank = 5;
        } else if (generationFitness.size() == lambda - 1) {
            diversityRank = 4;
        }

    }

    @Override
    public void saveFitness(int fitness) {
        generationFitness.add(fitness);
    }

    @Override
    public String toString() {
        return diversityRank + String.valueOf(numberOfBetter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairState)) return false;
        PairState pairState = (PairState) o;
        return numberOfBetter == pairState.numberOfBetter && diversityRank == pairState.diversityRank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfBetter, diversityRank);
    }
}
