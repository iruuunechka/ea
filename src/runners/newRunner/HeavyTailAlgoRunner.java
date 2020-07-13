package runners.newRunner;

import algo.AlgoFactory;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;

public interface HeavyTailAlgoRunner extends Runner {
    @Override
    void runAlgo(ExecutorService es, String filename, double beta, AlgoFactory factory) throws FileNotFoundException;
}
