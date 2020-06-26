package runners.newRunner;

import algo.AlgoFactory;

import java.io.FileNotFoundException;

public interface HeavyTailAlgoRunner extends Runner {
    @Override
    void runAlgo(String filename, double beta, AlgoFactory factory) throws FileNotFoundException;
}
