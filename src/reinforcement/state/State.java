package reinforcement.state;

import java.util.Set;

public interface State {
    void calculate(int numberOfBetter);
    void saveFitness(int fitness);
}

