package reinforcement.reward;

public interface Reward {
    double calculate(int newFitness, int oldFitness);
}
