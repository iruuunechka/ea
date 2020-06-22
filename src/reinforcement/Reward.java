package reinforcement;

public interface Reward {
    double calculate(int newFitness, int oldFitness);
}
