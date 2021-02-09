package reinforcement.state;

import java.util.Objects;

public class BetterCountState implements State{
    private int numberOfBetter;

    public BetterCountState(int numberOfBetter) {
        this.numberOfBetter = numberOfBetter;
    }

    @Override
    public void saveFitness(int fitness) {
        //do nothing
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BetterCountState)) return false;
        BetterCountState that = (BetterCountState) o;
        return numberOfBetter == that.numberOfBetter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfBetter);
    }

    public  BetterCountState() {
        this(-1);
    }

    @Override
    public void calculate(int numberOfBetter) {
        this.numberOfBetter = numberOfBetter;
    }

    @Override
    public String toString() {
        return String.valueOf(numberOfBetter);
    }
}
