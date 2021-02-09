package reinforcement.agent;

import reinforcement.state.State;
import reinforcement.utils.Map2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GreedyQAgent2actions implements Agent {
    protected final Map2<State, Integer, Double> Q;
    protected final List<Integer> actions;
    private final double alpha;
    private final double gamma;
    protected final double epsilon;
    protected final Random rand;


    public GreedyQAgent2actions(double alpha, double gamma, double epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        rand = ThreadLocalRandom.current();
        Q = new Map2<>(0.0);
        actions = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            actions.add(i);
        }
    }

    @Override
    public void updateExperience(State state, State newState, int action, double reward) {
        double old = Q.get(state, action);
        Q.put(state, action, old + alpha * (reward +
                gamma * Q.max(newState, actions) - old));
    }

    @Override
    public int chooseAction(State state) {
        if (rand.nextDouble() < epsilon) {
            return actions.get(rand.nextInt(actions.size()));
        } else {
            if (Q.get(state, 0).equals(Q.get(state, 1))) {
                return -1;
            } else {
                return Q.argMax(state, actions);
            }
        }
    }
}
