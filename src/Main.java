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

//        System.out.println("two rate");
//        runAlgo("tworate1.csv", lowerBoundTwoRate, getTwoRateImplementation());
//        System.out.println("two rate sq");
//        runAlgo("tworatesq1.csv", lowerBoundTwoRateSq, getTwoRateImplementation());

//        System.out.println("two rate adaptive");
//        runAlgo("adtworate.csv", lowerBoundTwoRate, getAdaptiveTwoRateImplementation());
//        System.out.println("two rate sq adaptive");
//        runAlgo("adtworatesq.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateImplementation());

//        System.out.println("two rate adaptive div");
//        runAlgo("addivtworate.csv", lowerBoundTwoRate, getAdaptiveDivTwoRateImplementation());
//        System.out.println("two rate sq adaptive div");
//        runAlgo("addivtworatesq.csv", lowerBoundTwoRateSq, getAdaptiveDivTwoRateImplementation());

//        System.out.println("two rate ad div");
//        runAlgoOnPoint("addivtworate1600.csv", lowerBoundTwoRate, getAdaptiveDivTwoRateImplementation(), 1600);
//        System.out.println("two rate ad div sq");
//        runAlgoOnPoint("addivtworatesq1600.csv", lowerBoundTwoRateSq, getAdaptiveDivTwoRateImplementation(), 1600);
//
//        System.out.println("two rate exp");
//        runAlgo("tworateexp.csv", lowerBoundTwoRate, getTwoRateExpImplementation());
//        System.out.println("two rate exp sq");
//        runAlgo("tworateexpsq.csv", lowerBoundTwoRateSq, getTwoRateExpImplementation());

//        System.out.println("two rate exp");
//        runAlgoOnPoint("tworateexp3200.csv", lowerBoundTwoRate, getTwoRateExpImplementation(), 3200);
//        System.out.println("two rate exp sq");
//        runAlgoOnPoint("tworateexpsq3200.csv", lowerBoundTwoRateSq, getTwoRateExpImplementation(), 3200);

//
//        System.out.println("two rate");
//        runAlgoOnPoint("tworate3200.csv", lowerBoundTwoRate, getTwoRateImplementation(), 3200);
//        System.out.println("two rate sq");
//        runAlgoOnPoint("tworatesq3200.csv", lowerBoundTwoRateSq, getTwoRateImplementation(), 3200);

//        System.out.println("simpleAlgo");
//        runAlgo("simple.csv", lowerBoundTwoRateSq, getSimpleEAImplementation());
//
//        System.out.println("two rate ad");
//        runAlgoOnPoint("adtworate1600.csv", lowerBoundTwoRate, getAdaptiveTwoRateImplementation(), 1600);
//        System.out.println("two rate ad sq");
//        runAlgoOnPoint("adtworatesq1600.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateImplementation(), 1600);

//        System.out.println("AB algorithm");
//        runAlgo("ab.csv", lowerBoundAb, getABImplementation());
//        System.out.println("AB algorithm sq");
//        runAlgo("absq.csv", lowerBoundAbSq, getABImplementation());

//        System.out.println("AbToExplore algorithm");
//        runAlgo("abexp.csv", lowerBoundAb, getABExpImplementation());
//        System.out.println("AB algorithm sq");
//        runAlgo("abexpsq.csv", lowerBoundAbSq, getABExpImplementation());

//        System.out.println("AB algorithm");
//        runAlgoOnPoint("ab1600.csv", lowerBoundAb, getABImplementation(), 1600);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq1600.csv", lowerBoundAbSq, getABImplementation(), 1600);

//        System.out.println("AB algorithm");
//        runAlgoOnPoint("ab6.csv", lowerBoundAb, getABImplementation(), 6);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq6.csv", lowerBoundAbSq, getABImplementation(), 6);

//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp1600.csv", lowerBoundAb, getABExpImplementation(), 1600);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq1600.csv", lowerBoundAbSq, getABExpImplementation(), 1600);

//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp6.csv", lowerBoundAb, getABExpImplementation(), 6);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq6.csv", lowerBoundAbSq, getABExpImplementation(), 6);

        System.out.println("Two Rate algorithm");
        runAlgoByFitnessCount("twoRateFitCou.csv", lowerBoundTwoRate, getTwoRateImplementation());
        System.out.println("Two Rate algorithm sq");
        runAlgoByFitnessCount("twoRateFitCousq.csv", lowerBoundTwoRateSq, getTwoRateImplementation());

        System.out.println("Two Rate NoShift algorithm");
        runAlgoByFitnessCount("twoRateNoShiftFitCou.csv", lowerBoundTwoRate, getTwoRateNoShiftImplementation());
        System.out.println("Two Rate NoShift algorithm sq");
        runAlgoByFitnessCount("twoRateNoShiftFitCousq.csv", lowerBoundTwoRateSq, getTwoRateNoShiftImplementation());



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
//                algo.printInfo();
            }
            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
    }
    private static void runAlgoByFitnessCount(String filename, double lowerBound, AlgoFactory factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("fitCou, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageFitCou = 0;
            for (int i = 0; i < avCount; i++) {
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
                }
                averageFitCou = (i == 0) ? algo.getFitnessCount() : (averageFitCou * i + algo.getFitnessCount()) / (i + 1);
//                algo.printInfo();
            }
            pw.println((int) averageFitCou + ", " + lambda);
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
            algo.printInfo();
        }
        pw.close();
    }

    private static AlgoFactory getTwoRateImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateNoShiftImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new OneMax(problemLength));
    }


    private static AlgoFactory getTwoRateExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMax(problemLength));
    }


    private static AlgoFactory getAdaptiveTwoRateImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveDivTwoRateImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveDivTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveTwoRateSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgoToExplore(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / (n * n), 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getSimpleEAImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMax(problemLength));
    }

}
