package reinforcement;

import reinforcement.utils.FastRandom;
import reinforcement.utils.Map2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreedyQAgent implements Agent {
    private final Map2<Integer, Integer, Double> Q;
    private final List<Integer> actions;
    private final double alpha;
    private final double gamma;
    private final double epsilon;
    private final Random rand;

    public GreedyQAgent(int actionsCount, double alpha, double gamma, double epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        rand = FastRandom.threadLocal();
        Q = new Map2<>(0.0);
        actions = new ArrayList<>();
        for (int i = 0; i < actionsCount; ++i) {
            actions.add(i);
        }
    }

    @Override
    public void updateExperience(int state, int newState, int action, double reward) {
        double old = Q.get(state, action);
        Q.put(state, action, old + alpha * (reward +
                gamma * Q.max(newState, actions) - old));
    }

    @Override
    public int chooseAction(int state) {
        if (rand.nextDouble() < epsilon) {
            return actions.get(rand.nextInt(actions.size()));
        } else {
            return Q.argMax(state, actions);
        }
    }
}
