package reinforcement.reward;

public class DivReward implements Reward {
    @Override
    public double calculate(int newFitness, int oldFitness) {
        return ((double) newFitness) / oldFitness - 1;
    }
}