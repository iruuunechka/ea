import algo.*;
import problem.OneMax;
import problem.Problem;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    private static final int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
    private static final int n = 10000;
    private static final int avCount = 10;


    public static void main(String[] args) throws FileNotFoundException {
        double lowerBoundTwoRate = 2.0 / n;
        double lowerBoundTwoRateSq = 2.0 / (n * n);
        double lowerBoundAb = 1.0 / n;
        double lowerBoundAbSq = 1.0 / (n * n);

        System.out.println("two rate");
        runAlgo("tworate.csv", lowerBoundTwoRate, getTwoRateImplementation());
        System.out.println("two rate sq");
        runAlgo("tworatesq.csv", lowerBoundTwoRateSq, getTwoRateImplementation());


//        System.out.println("two rate adaptive");
//        runAlgo("adtworate.csv", lowerBoundTwoRate, getAdaptiveTwoRateImplementation());
//        System.out.println("two rate sq adaptive");
//        runAlgo("adtworatesq.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateImplementation());
//
//        System.out.println("two rate");
//        runAlgoOnPoint("tworate1600.csv", lowerBoundTwoRate, getTwoRateImplementation(), 1600);
//        System.out.println("two rate sq");
//        runAlgoOnPoint("tworatesq1600.csv", lowerBoundTwoRateSq, getTwoRateImplementation(), 1600);
//
//        System.out.println("two rate ad");
//        runAlgoOnPoint("adtworate1600.csv", lowerBoundTwoRate, getAdaptiveTwoRateImplementation(), 1600);
//        System.out.println("two rate ad sq");
//        runAlgoOnPoint("adtworatesq1600.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateImplementation(), 1600);

//        System.out.println("AB algorithm");
//        runAlgo("ab.csv", lowerBoundAb, getABImplementation());
//        System.out.println("AB algorithm sq");
//        runAlgo("absq.csv", lowerBoundAbSq, getABImplementation());

    }

    private static void runAlgo(String filename, double lowerBound, AlgoFactory factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("gen, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageIterCount = 0;
            for (int i = 0; i < avCount; i++) {
                int curIterCount = 0;
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
                    curIterCount++;
                }
                averageIterCount = (i == 0) ? curIterCount : (averageIterCount * i + curIterCount) / (i + 1);
                algo.printInfo();
            }
            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void runAlgoOnPoint(String filename, double lowerBound, AlgoFactory factory, int lambda) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("fitness, mutation, iter");
        int curIterCount = 0;
        Algorithm algo = factory.getInstance(lambda, lowerBound, n);
        while (!algo.isFinished()) {
            algo.makeIteration();
            curIterCount++;
            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + curIterCount);
        }
        pw.close();
    }

    private static AlgoFactory getTwoRateImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveTwoRateSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveTwoRateImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / (n * n), 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }
}
