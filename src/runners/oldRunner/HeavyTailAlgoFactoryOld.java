package runners.oldRunner;

import algo.Algorithm;

public interface HeavyTailAlgoFactoryOld extends AlgoFactoryOld {
    @Override
    Algorithm getInstance(int lambda, double beta, int problemLength);
}
