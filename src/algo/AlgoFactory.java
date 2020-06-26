package algo;

import runners.newRunner.parameterSets.ParameterSet;

public interface AlgoFactory<P extends ParameterSet> {
    Algorithm getInstance (P ps);
}
