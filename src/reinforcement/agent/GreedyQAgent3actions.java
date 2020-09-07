package reinforcement.agent;

import reinforcement.utils.Map2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GreedyQAgent3actions extends GreedyQAgent2actions {

/**
 * 0-div
 * 1-mult
 * 2-nothing
 */
    public GreedyQAgent3actions(double alpha, double gamma, double epsilon) {
        super(alpha, gamma, epsilon);
        actions.add(2);
    }

    @Override
    public int chooseAction(int state) {
        if (rand.nextDouble() < epsilon) {
            return actions.get(rand.nextInt(actions.size()));
        } else {
            double q0 = Q.get(state, 0);
            double q1 = Q.get(state, 1);
            double q2 = Q.get(state, 2);
            double max = Q.max(state, actions);
            if ((q0 == max) && (q0 == q1) && (q0 != q2))  {
                return -1;
            } else {
                return Q.argMax(state, actions);
            }
        }
    }
}

