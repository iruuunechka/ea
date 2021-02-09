package reinforcement.agent;

import reinforcement.state.State;

public interface Agent {
    void updateExperience(State state, State newState, int action, double reward);
    int chooseAction(State state);
}
