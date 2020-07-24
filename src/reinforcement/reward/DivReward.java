package reinforcement.reward;

public class DivReward implements Reward {
    @Override
    public double calculate(int newFitness, int oldFitness) {
        return ((double) newFitness + 1) / (oldFitness + 1) - 1;
    }
}