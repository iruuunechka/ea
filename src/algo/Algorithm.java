package algo;

public interface Algorithm {
    void makeIteration();
    boolean isFinished();
    void printInfo();
    double getMutationRate();
    int getFitness();
    long getFitnessCount();
    int getIterCount();

}
