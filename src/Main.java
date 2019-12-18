import algo.*;
import problem.LeadingOnes;
import problem.OneMax;
import problem.OneMaxNeutral3;
import problem.Ruggedness;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    private static final int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
    private static  int n = 10000;
    private static final int avCount = 10;
    private static final int[] rugs = new int[] {1, 2, 5};
    private static final double beta = 2.5;


    public static void main(String[] args) throws FileNotFoundException {
        double lowerBoundTwoRate = 2.0 / n;
        double lowerBoundTwoRateSq = 2.0 / (n * n);
        double lowerBoundAb = 1.0 / n;
        double lowerBoundAbSq = 1.0 / (n * n);

//        //Simple Algo
//        System.out.println("simpleAlgo");
//        runAlgo("simple.csv", lowerBoundTwoRateSq, getSimpleEAOMImplementation());
//
//        //Simple TwoRate
//        System.out.println("two rate");
//        runAlgo("tworate.csv", lowerBoundTwoRate, getTwoRateOMImplementation());
//        System.out.println("two rate sq");
//        runAlgo("tworatesq.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation());
//
//        System.out.println("two rate leading ones");
//        runAlgo("tworateLO.csv", lowerBoundTwoRate, getTwoRateLOImplementation());
//        System.out.println("two rate sq leading ones");
//        runAlgo("tworatesqLO.csv", lowerBoundTwoRateSq, getTwoRateLOImplementation());
//
//        System.out.println("two rate");
//        runAlgoOnPoint("tworate10.csv", getTwoRateOMImplementation().getInstance(10, lowerBoundTwoRate, n));
//        System.out.println("two rate sq");
//        runAlgoOnPoint("tworatesq10.csv", getTwoRateOMImplementation().getInstance(10, lowerBoundTwoRateSq, n));
//
//        //Adaptive TwoRate
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
//        runAlgoOnPoint("addivtworate1600.csv", getAdaptiveDivTwoRateOMImplementation().getInstance(1600, lowerBoundTwoRate, n));
//        System.out.println("two rate ad div sq");
//        runAlgoOnPoint("addivtworatesq1600.csv", getAdaptiveDivTwoRateOMImplementation().getInstance(1600, lowerBoundTwoRateSq, n));
//
//        System.out.println("two rate ad");
//        runAlgoOnPoint("adtworate1600.csv", getAdaptiveTwoRateOMImplementation().getInstance(1600, lowerBoundTwoRate, n));
//        System.out.println("two rate ad sq");
//        runAlgoOnPoint("adtworatesq1600.csv", getAdaptiveTwoRateOMImplementation().getInstance(1600, lowerBoundTwoRateSq, n));
//
//        //AB algo
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
//        runAlgoOnPoint("ab1600.csv", getABOMImplementation().getInstance(1600, lowerBoundAb, n));
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq1600.csv", getABOMImplementation().getInstance(1600, lowerBoundAbSq, n));
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("ab6.csv", getABOMImplementation().getInstance(6, lowerBoundAb, n));
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("absq6.csv", getABOMImplementation().getInstance(6, lowerBoundAbSq, n));
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp1600.csv", getABExpOMImplementation().getInstance(1600, lowerBoundAb, n));
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq1600.csv", getABExpOMImplementation().getInstance(1600, lowerBoundAbSq, n));
//
//        System.out.println("AB algorithm");
//        runAlgoOnPoint("abexp6.csv", getABExpOMImplementation().getInstance(6, lowerBoundAb, n));
//        System.out.println("AB algorithm sq");
//        runAlgoOnPoint("abexpsq6.csv", getABExpOMImplementation().getInstance(6, lowerBoundAbSq, n));
//
//        //Run by fitness count
//        System.out.println("Two Rate algorithm");
//        runAlgoByFitnessCount("twoRateFitCou.csv", lowerBoundTwoRate, getTwoRateOMImplementation());
//        System.out.println("Two Rate algorithm sq");
//        runAlgoByFitnessCount("twoRateFitCousq.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation());
//
//        System.out.println("Two Rate NoShift algorithm");
//        runAlgoByFitnessCount("twoRateNoShiftFitCou.csv", lowerBoundTwoRate, getTwoRateOMNoShiftImplementation());
//        System.out.println("Two Rate NoShift algorithm sq");
//        runAlgoByFitnessCount("twoRateNoShiftFitCousq.csv", lowerBoundTwoRateSq, getTwoRateOMNoShiftImplementation());
//
//        //TwoRate Exp
//        System.out.println("two rate exp");
//        runAlgo("tworateexp.csv", lowerBoundTwoRate, getTwoRateOMExpImplementation());
//        System.out.println("two rate exp sq");
//        runAlgo("tworateexpsq.csv", lowerBoundTwoRateSq, getTwoRateOMExpImplementation());
//
//        System.out.println("two rate exp leading ones");
//        runAlgo("tworateexpLO.csv", lowerBoundTwoRate, getTwoRateLOExpImplementation());
//        System.out.println("two rate exp leading ones sq ");
//        runAlgo("tworateexpsqLO.csv", lowerBoundTwoRateSq, getTwoRateLOExpImplementation());
//
//        System.out.println("two rate exp");
//        runAlgoOnPoint("tworateexp10.csv", getTwoRateOMExpImplementation().getInstance(10, lowerBoundTwoRate, n));
//        System.out.println("two rate exp sq");
//        runAlgoOnPoint("tworateexpsq10.csv", getTwoRateOMExpImplementation().getInstance(10, lowerBoundTwoRateSq, n));
//
//        //HeavyTail OM + Ruggedness
//        System.out.println("Heavy Tail algo OneMax");
//        runHeavyTailAlgo("heavyTail1.4.csv", beta, getHeavyTailOMImplementation());
//
//        n = 500;
//        for (int i : rugs) {
//            System.out.println("Heavy Tail algo Ruggedness, r = " + i);
//            runHeavyTailAlgo("heavyTailRug" + i + "_" + n +".csv", beta, getHeavyTailRugImplementation(i));
//        }
//
//        for (int i : rugs) {
//            System.out.println("Two Rate algo Ruggedness, r = " + i);
//            runAlgo("twoRateRug" + i + "_" + n +".csv", lowerBoundTwoRate, getTwoRateRugImplementation(i));
//            System.out.println("Two Rate algo Ruggedness sq, r = " + i);
//            runAlgo("twoRateRug" + i + "_" + n +".csv", lowerBoundTwoRateSq, getTwoRateRugImplementation(i));
//        }
//
//        for (int i : rugs) {
//            System.out.println("Simple algo Ruggedness, r = " + i);
//            runAlgo("simpleRug" + i + "_" + n +".csv", lowerBoundTwoRate, getSimpleEARugImplementation(i));
//        }
//
//        n = 1000;
//        System.out.println("Heavy Tail algo Ruggedness, r = 5");
//        runHeavyTailAlgo("heavyTailRug5" + "_" + n +".csv", beta, getHeavyTailRugImplementation(5));
//
//        System.out.println("Two Rate algo Ruggedness, r = 5");
//        runAlgo("twoRateRug5" + "_" + n +".csv", lowerBoundTwoRate, getTwoRateRugImplementation(5));
//        System.out.println("Two Rate algo Ruggedness sq, r = 5");
//        runAlgo("twoRateRug5" + "_" + n +".csv", lowerBoundTwoRateSq, getTwoRateRugImplementation(5));
//
//        System.out.println("Simple algo Ruggedness, r = 5");
//        runAlgo("simpleRug5" + "_" + n +".csv", lowerBoundTwoRate, getSimpleEARugImplementation(5));
//
//        //Run all on Neutral3
//        System.out.println("simpleAlgo");
//        runAlgo("simpleNeutral.csv", lowerBoundTwoRateSq, getSimpleEANeutralImplementation());
//
//        System.out.println("two rate");
//        runAlgo("tworateNeutral.csv", lowerBoundTwoRate, getTwoRateNeutralImplementation());
//
//        System.out.println("two rate sq");
//        runAlgo("tworateNeutralsq.csv", lowerBoundTwoRateSq, getTwoRateNeutralImplementation());
//
//        System.out.println("two rate");
//        runAlgo("tworateNeutralExp.csv", lowerBoundTwoRate, getTwoRateNeutralExpImplementation());
//
//        System.out.println("two rate sq");
//        runAlgo("tworateNeutralExpsq.csv", lowerBoundTwoRateSq, getTwoRateNeutralExpImplementation());
//
//        System.out.println("Heavy Tail algo Neutral3");//tail = 2.5
//        runHeavyTailAlgo("heavyTailNeutral.csv", beta, getHeavyTailNeutralImplementation());

        //Run all on Neutral3 on 1600
        int lambda = 1600;
        System.out.println("Two Rate Neutral" + lambda);
        runAlgoOnPoint("twoRateNeutral"+lambda+".csv", getTwoRateNeutralImplementation().getInstance(lambda, lowerBoundTwoRate, n));
        System.out.println("Two Rate sq Neutral" + lambda);
        runAlgoOnPoint("twoRateNeutral" + lambda + "sq.csv", getTwoRateNeutralImplementation().getInstance(lambda, lowerBoundTwoRateSq, n));
        System.out.println("Two Rate Exp Neutral" + lambda);
        runAlgoOnPoint("twoRateExpNeutral" + lambda +".csv", getTwoRateNeutralExpImplementation().getInstance(lambda, lowerBoundTwoRate, n));
        System.out.println("Two Rate Exp sq Neutral" + lambda);
        runAlgoOnPoint("twoRateExpNeutral" + lambda + "sq.csv", getTwoRateNeutralExpImplementation().getInstance(lambda, lowerBoundTwoRateSq, n));
        System.out.println("Heavy Tail Neutral" + lambda);
        runAlgoOnPoint("heavyTailNeutral" + lambda + ".csv", getHeavyTailNeutralImplementation().getInstance(lambda, beta, n));

    }

    private static void runHeavyTailAlgo(String filename, double beta, HeavyTailAlgoFactory factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("gen, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + beta);
            double averageIterCount = 0;
            for (int i = 0; i < avCount; i++) {
                Algorithm algo = factory.getInstance(lambda, beta, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
//                    algo.printInfo();
                }
                averageIterCount = (i == 0) ? algo.getIterCount() : (averageIterCount * i + algo.getIterCount()) / (i + 1);
                System.out.println(i);
            }
            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
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

    private static void runAlgoOnPoint(String filename, Algorithm algo) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("fitness, mutation, iter, zero, one, two, three");
        int curIterCount = 0;
        while (!algo.isFinished()) {
            algo.makeIteration();
            curIterCount++;
            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + curIterCount + algo.getProblemInfo());
//            algo.printInfo();
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

    //Two Rate
    private static AlgoFactory getTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactory getTwoRateNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMaxNeutral3(problemLength));
    }

    private static AlgoFactory getTwoRateRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    //Two Rate No Shift
    private static AlgoFactory getTwoRateOMNoShiftImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    //Two Rate Exp
    private static AlgoFactory getTwoRateOMExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getTwoRateLOExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactory getTwoRateNeutralExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMaxNeutral3(problemLength));
    }

    //Two Rate Adaptive
    private static AlgoFactory getAdaptiveTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveDivTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveDivTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    //Two Rate Square Start
    private static AlgoFactory getTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getAdaptiveTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    //AB
    private static AlgoFactory getABExpOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgoToExplore(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getABSQOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / (n * n), 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    //Simple
    private static AlgoFactory getSimpleEAOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactory getSimpleEANeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMaxNeutral3(problemLength));
    }

    private static AlgoFactory getSimpleEARugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    //Heavy Tail
    private static AlgoFactory getHeavyTailOMImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new OneMax(problemLength)));
    }

    private static AlgoFactory getHeavyTailLOImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new LeadingOnes(problemLength)));
    }

    private static AlgoFactory getHeavyTailRugImplementation(int r) {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Ruggedness(problemLength, r)));
    }

    private static AlgoFactory getHeavyTailNeutralImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new OneMaxNeutral3(problemLength)));
    }

}
