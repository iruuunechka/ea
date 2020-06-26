package runners.oldRunner;

import algo.Algorithm;

public interface AlgoFactoryOld {
    Algorithm getInstance(int lambda, double lowerBound, int problemLength);
}
