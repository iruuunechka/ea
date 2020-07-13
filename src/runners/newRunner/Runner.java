package runners.newRunner;

import algo.AlgoFactory;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;

public interface Runner {
    void runAlgo(ExecutorService es, String filename, double lowerBound, AlgoFactory factory) throws FileNotFoundException;
}
