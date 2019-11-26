import algo.*;
import problem.LeadingOnes;
import problem.OneMax;

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
//        runAlgo("tworate.csv", lowerBoundTwoRate, getTwoRateOMImplementation());
//        System.out.println("two rate sq");
//        runAlgo("tworatesq.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation());
//
//        System.out.println("two rate adaptive");
//        runAlgo("adtworate.csv", lowerBoundTwoRate, getAdaptiveTwoRateOMImplementation());
//        System.out.println("two rate sq adaptive");
//        runAlgo("adtworatesq.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateOMImplementation());
//
//        System.out.println("two rate adaptive div");
//        runAlgo("addivtworate.csv", lowerBoundTwoRate, getAdaptiveDivTwoRateOMImplementation());
//        System.out.println("two rate sq adaptive div");
//        runAlgo("addivtworatesq.csv", lowerBoundTwoRateSq, getAdaptiveDivTwoRateOMImplementation());
//
//        System.out.println("two rate ad div");
//        runAlgoOnPoint("addivtworate1600.csv", lowerBoundTwoRate, getAdaptiveDivTwoRateOMImplementation(), 1600);
//        System.out.println("two rate ad div sq");
//        runAlgoOnPoint("addivtworatesq1600.csv", lowerBoundTwoRateSq, getAdaptiveDivTwoRateOMImplementation(), 1600);

//        System.out.println("two rate exp");
//        runAlgo("tworateexp.csv", lowerBoundTwoRate, getTwoRateOMExpImplementation());
//        System.out.println("two rate exp sq");
//        runAlgo("tworateexpsq.csv", lowerBoundTwoRateSq, getTwoRateOMExpImplementation());

        System.out.println("two rate leading ones");
        runAlgo("tworateLO.csv", lowerBoundTwoRate, getTwoRateLOImplementation());
        System.out.println("two rate sq leading ones");
        runAlgo("tworatesqLO.csv", lowerBoundTwoRateSq, getTwoRateLOImplementation());

        System.out.println("two rate exp leading ones");
        runAlgo("tworateexpLO.csv", lowerBoundTwoRate, getTwoRateLOExpImplementation());
        System.out.println("two rate exp leading ones sq ");
        runAlgo("tworateexpsqLO.csv", lowerBoundTwoRateSq, getTwoRateLOExpImplementation());

//        System.out.println("two rate exp");
//        runAlgoOnPoint("tworateexp10.csv", lowerBoundTwoRate, getTwoRateOMExpImplementation(), 10);
//        System.out.println("two rate exp sq");
//        runAlgoOnPoint("tworateexpsq10.csv", lowerBoundTwoRateSq, getTwoRateOMExpImplementation(), 10);
//
//        System.out.println("two rate");
//        runAlgoOnPoint("tworate10.csv", lowerBoundTwoRate, getTwoRateOMImplementation(), 10);
//        System.out.println("two rate sq");
//        runAlgoOnPoint("tworatesq10.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation(), 10);
//
//        System.out.println("simpleAlgo");
//        runAlgo("simple.csv", lowerBoundTwoRateSq, getSimpleEAOMImplementation());
//
//        System.out.println("two rate ad");
//        runAlgoOnPoint("adtworate1600.csv", lowerBoundTwoRate, getAdaptiveTwoRateOMImplementation(), 1600);
//        System.out.println("two rate ad sq");
//        runAlgoOnPoint("adtworatesq1600.csv", lowerBoundTwoRateSq, getAdaptiveTwoRateOMImplementation(), 1600);
//
//        System.out.println("AB algorithm");
//        runAlgo("ab.csv", lowerBoundAb, getABOMImplementation());
//        System.out.println("AB algorithm sq");
//        runAlgo("absq.csv", lowerBoundAbSq, getABOMImplementation());
//
//        System.out.println("AbToExplore algorithm");
//        runAlgo("abexp.csv", lowerBoundAb, getABExpOMImplementation());
//        System.out.println("AB algorithm sq");
//        runAlgo("abexpsq.csv", lowerBoundAbSq, getABExpOMImplementation());
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("ab1600.csv", lowerBoundAb, getABOMImplementation(), 1600);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq1600.csv", lowerBoundAbSq, getABOMImplementation(), 1600);
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("ab6.csv", lowerBoundAb, getABOMImplementation(), 6);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq6.csv", lowerBoundAbSq, getABOMImplementation(), 6);
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp1600.csv", lowerBoundAb, getABExpOMImplementation(), 1600);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq1600.csv", lowerBoundAbSq, getABExpOMImplementation(), 1600);
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp6.csv", lowerBoundAb, getABExpOMImplementation(), 6);
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq6.csv", lowerBoundAbSq, getABExpOMImplementation(), 6);
//
//        System.out.println("Two Rate algorithm");
//        runAlgoByFitnessCount("twoRateFitCou.csv", lowerBoundTwoRate, getTwoRateOMImplementation());
//        System.out.println("Two Rate algorithm sq");
//        runAlgoByFitnessCount("twoRateFitCousq.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation());
//
//        System.out.println("Two Rate NoShift algorithm");
//        runAlgoByFitnessCount("twoRateNoShiftFitCou.csv", lowerBoundTwoRate, getTwoRateOMNoShiftImplementation());
//        System.out.println("Two Rate NoShift algorithm sq");
//        runAlgoByFitnessCount("twoRateNoShiftFitCousq.csv", lowerBoundTwoRateSq, getTwoRateOMNoShiftImplementation());



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
//                    algo.printInfo();
                    curIterCount++;
                }
                averageIterCount = (i == 0) ? curIterCount : (averageIterCount * i + curIterCount) / (i + 1);
                System.out.println(i);
            }
            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
    }
    private static void runAlgoByFitnessCount(String filename, double lowerBound, AlgoFactory factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("fitCou, lambda, iter");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageFitCou = 0;
            double averageIterCou = 0;
            for (int i = 0; i < avCount; i++) {
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
                }
                averageFitCou = (i == 0) ? algo.getFitnessCount() : (averageFitCou * i + algo.getFitnessCount()) / (i + 1);
                averageIterCou = (i == 0) ? algo.getIterCount() : (averageIterCou * i + algo.getIterCount()) / (i + 1);
//                algo.printInfo();
            }
            pw.println((int) averageFitCou + ", " + lambda + ", " + averageIterCou);
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

    private static AlgoFactory getTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactory getTwoRateOMNoShiftImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new OneMax(problemLength));
    }


    private static AlgoFactory getTwoRateOMExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateLOExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }


    private static AlgoFactory getAdaptiveTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveDivTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveDivTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABExpOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgoToExplore(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABSQOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / (n * n), 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getSimpleEAOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMax(problemLength));
    }

}
