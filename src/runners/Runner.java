package runners;

import algo.AlgoFactory;

import java.io.FileNotFoundException;

public interface Runner {
    void runAlgo(String filename, double lowerBound, AlgoFactory factory) throws FileNotFoundException;
}
