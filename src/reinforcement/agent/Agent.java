package reinforcement.agent;

public interface Agent {
    void updateExperience(int state, int newState, int action, double reward);
    int chooseAction(int state);
}
