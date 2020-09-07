package reinforcement.reward;

public class MinusReward implements Reward {
    @Override
    public double calculate(int newFitness, int oldFitness) {
        return newFitness - oldFitness;
    }
}