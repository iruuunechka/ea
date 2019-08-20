package algo;

import problem.Problem;

public interface AlgoFactory {
    Algorithm getInstance (int lambda, double lowerBound, int problemLength);
}
