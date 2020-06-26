package runners.oldRunner;

import algo.*;
import problem.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int[] lambdas = new int[] {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096};//6, 10, 50, 100, 200, 400, 800, 1600, 3200};
    private static final int n = 10000;
    private static final int runCount = 100;
    private static final int[] rugs = new int[] {20};//1, 2, 5, 10, 15, 20};//{1, 2, 5};
    private static final double beta = 2.5;
    private static final double lowerBoundTwoRate = 2.0 / n;
    private static final double lowerBoundTwoRateSq = 2.0 / (n * n);
    private static final double lowerBoundTwoRateByFlipBits = 1.0 / n;
    private static final double lowerBoundTwoRateByFlipBitsSq = 1.0 / (n * n);
    private static final double lowerBoundAb = 1.0 / n;
    private static final double lowerBoundAbSq = 1.0 / (n * n);


    public static void main(String[] args) throws FileNotFoundException {

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
////        n = 500;
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
////        n = 1000;
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
//
//        //Run all on Neutral3 on 1600
//        int lambda = 1600;
//        System.out.println("Two Rate Neutral" + lambda);
//        runAlgoOnPoint("twoRateNeutral"+lambda+".csv", getTwoRateNeutralImplementation().getInstance(lambda, lowerBoundTwoRate, n));
//        System.out.println("Two Rate sq Neutral" + lambda);
//        runAlgoOnPoint("twoRateNeutral" + lambda + "sq.csv", getTwoRateNeutralImplementation().getInstance(lambda, lowerBoundTwoRateSq, n));
//        System.out.println("Two Rate Exp Neutral" + lambda);
//        runAlgoOnPoint("twoRateExpNeutral" + lambda +".csv", getTwoRateNeutralExpImplementation().getInstance(lambda, lowerBoundTwoRate, n));
//        System.out.println("Two Rate Exp sq Neutral" + lambda);
//        runAlgoOnPoint("twoRateExpNeutral" + lambda + "sq.csv", getTwoRateNeutralExpImplementation().getInstance(lambda, lowerBoundTwoRateSq, n));
//        System.out.println("Heavy Tail Neutral" + lambda);
//        runAlgoOnPoint("heavyTailNeutral" + lambda + ".csv", getHeavyTailNeutralImplementation().getInstance(lambda, beta, n));
//
//        //Run all on int[] lambdaOMPoints = new int[] {1, 2, 5, 10, 50, 100, 200, 400, 800};
//        int[] lambdaOMPoints = new int[] {2, 5, 10, 50, 100, 200, 400, 800};
//        int runCount = 5;
//        String folder = "GradientPlotOMManyPoints/";
//        new File(folder).mkdir();
//        for (int lambda : lambdaOMPoints) {
//            System.out.println("Two Rate OM " + lambda);
//            runAlgoOnPointGradientPlot(folder + "twoRateOM_" + n + "_" + lambda+".csv", getTwoRateOMImplementation(), lambda, lowerBoundTwoRate, runCount);
//            System.out.println("Two Rate sq OM " + lambda);
//            runAlgoOnPointGradientPlot(folder + "twoRatesqOM_" + n + "_" + lambda + ".csv", getTwoRateOMImplementation(), lambda, lowerBoundTwoRateSq, runCount);
//            System.out.println("Two Rate Exp OM " + lambda);
//            runAlgoOnPointGradientPlot(folder + "twoRateExpOM_" + n + "_" + lambda +".csv", getTwoRateOMExpImplementation(), lambda, lowerBoundTwoRate, runCount);
//            System.out.println("Two Rate Exp sq OM " + lambda);
//            runAlgoOnPointGradientPlot(folder + "twoRateExpsqOM_" + n + "_" + lambda + ".csv", getTwoRateOMExpImplementation(), lambda, lowerBoundTwoRateSq, runCount);
//        }
//
////        Run plateau
//        runAlgo("tworatePlateau.csv", lowerBoundTwoRate, getTwoRatePlateauImplementation(2));
////        Run Ruggedness on fixed budget
//
//        int budget = 3200 * 100;
//        String folder = "RuggednessFixedBudget400_800_" + budget + "_" + n +"/";
//        new File(folder).mkdir();
//        for (int r : rugs) {
//            String folderRug = folder + r + "/";
//            new File(folderRug).mkdir();
////            System.out.println("Heavy Tail algo Ruggedness, r = " + r);
////            runHeavyTailAlgoOnFixedBudget(folderRug + "heavyTailRug" + r + "_" + n + "Av.csv", folderRug + "heavyTailRug" + r + "_" + n + ".csv", beta, getHeavyTailRugImplementation(r), budget);
//            System.out.println("Two Rate algo Ruggedness, r = " + r);
//            runAlgoOnFixedBudget(lambdas, runCount, n, folderRug + "twoRateRug" + r + "_" + n + "Av.csv", folderRug + "twoRateRug" + r + "_" + n + ".csv", lowerBoundTwoRate, getTwoRateRugImplementation(r), budget);
//            System.out.println("Two Rate sq algo Ruggedness, r = " + r);
//            runAlgoOnFixedBudget(folderRug + "twoRateSqRug" + r + "_" + n + "Av.csv", folderRug + "twoRateSqRug" + r + "_" + n + ".csv", lowerBoundTwoRateSq, getTwoRateRugImplementation(r), budget);
//            System.out.println("Two Rate Exp algo Ruggedness, r = " + r);
//            runAlgoOnFixedBudget(folderRug + "twoRateExpRug" + r + "_" + n + "Av.csv", folderRug + "twoRateExpRug" + r + "_" + n + ".csv", lowerBoundTwoRate, getTwoRateExpRugImplementation(r), budget);
//            System.out.println("Two Rate Exp algo Ruggedness sq, r = " + r);
//            runAlgoOnFixedBudget(folderRug + "twoRateExpSqRug" + r + "_" + n + "Av.csv", folderRug + "twoRateExpSqRug" + r + "_" + n + ".csv", lowerBoundTwoRateSq, getTwoRateExpRugImplementation(r), budget);
//        }

//        Runs for article
//
//        ExecutorService es = Executors.newCachedThreadPool();
//        Thread thread;
//        String folder = "Article100runs10000lenSimple/";
//        new File(folder).mkdir();
//
//        String folderPlateau = folder + "Plateau/";
//        new File(folderPlateau).mkdir();
//        int k = 2;
//
//        thread = new Thread(() -> {
//            try {
////                System.out.println("simpleAlgo Plateau");
//                runAlgo(lambdas, runCount, n,folderPlateau + "simplePlateau.csv", lowerBoundTwoRateSq, getSimpleEAPlateauImplementation(k));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate Plateau");
//                runAlgo(lambdas, runCount, n,folderPlateau + "tworatePlateau.csv", lowerBoundTwoRate, getTwoRatePlateauImplementation(k));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate sq Plateau");
//                runAlgo(folderPlateau + "tworatesqPlateau.csv", lowerBoundTwoRateSq, getTwoRatePlateauImplementation(k));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate exp Plateau");
//                runAlgo(folderPlateau + "tworateexpPlateau.csv", lowerBoundTwoRate, getTwoRateExpPlateauImplementation(k));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate exp sq Plateau");
//                runAlgo(folderPlateau + "tworateexpsqPlateau.csv", lowerBoundTwoRateSq, getTwoRateExpPlateauImplementation(k));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//
//        String folderOM = folder + "OM/";
//        new File(folderOM).mkdir();
//        thread = new Thread(() -> {
//            try {
////                System.out.println("simpleAlgo OM");
//                runAlgo(folderOM + "simpleOM.csv", lowerBoundTwoRateSq, getSimpleEAOMImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate OM");
//                runAlgo(folderOM + "tworateOM.csv", lowerBoundTwoRate, getTwoRateOMImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate sq OM");
//                runAlgo(folderOM + "tworatesqOM.csv", lowerBoundTwoRateSq, getTwoRateOMImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate exp OM");
//                runAlgo(folderOM + "tworateexpOM.csv", lowerBoundTwoRate, getTwoRateOMExpImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate exp sq OM");
//                runAlgo(folderOM + "tworateexpsqOM.csv", lowerBoundTwoRateSq, getTwoRateOMExpImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        String folderNeutral = folder + "Neutral3/";
//        new File(folderNeutral).mkdir();
//        thread = new Thread(() -> {
//            try {
////                System.out.println("simpleAlgo Neutral3");
//                runAlgo(folderNeutral + "simpleNeutral.csv", lowerBoundTwoRateSq, getSimpleEANeutralImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate Neutral3");
//                runAlgo(folderNeutral + "tworateNeutral.csv", lowerBoundTwoRate, getTwoRateNeutralImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate sq Neutral3");
//                runAlgo(folderNeutral + "tworateNeutralsq.csv", lowerBoundTwoRateSq, getTwoRateNeutralImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate Neutral3");
//                runAlgo(folderNeutral + "tworateNeutralExp.csv", lowerBoundTwoRate, getTwoRateNeutralExpImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//        thread = new Thread(() -> {
//            try {
////                System.out.println("two rate sq Neutral3");
//                runAlgo(folderNeutral + "tworateNeutralExpsq.csv", lowerBoundTwoRateSq, getTwoRateNeutralExpImplementation());
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        int[] articleRugs = {2, 10, 20};
//        int budget = 1000;
//        String folderRugs = folder + "/" + "RuggednessFixedBudget" + budget + "_" + n +"/";
//        new File(folderRugs).mkdir();
//        for (int r : articleRugs) {
//            String folderRug = folderRugs + r + "/";
//            new File(folderRug).mkdir();
//            thread = new Thread(() -> {
//                try {
////                    System.out.println("Simple algo Ruggedness, r = " + r);
//                    runAlgoOnFixedBudget(folderRug + "simpleRug" + r + "_" + n + ".csv", lowerBoundTwoRate, getSimpleEARugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//            thread = new Thread(() -> {
//                try {
////                    System.out.println("Two Rate algo Ruggedness, r = " + r);
//                    runAlgoOnFixedBudget(folderRug + "twoRateRug" + r + "_" + n + ".csv", lowerBoundTwoRate, getTwoRateRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//            thread = new Thread(() -> {
//                try {
////                    System.out.println("Two Rate sq algo Ruggedness, r = " + r);
//                    runAlgoOnFixedBudget(folderRug + "twoRateSqRug" + r + "_" + n + ".csv", lowerBoundTwoRateSq, getTwoRateRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//            thread = new Thread(() -> {
//                try {
////                    System.out.println("Two Rate Exp algo Ruggedness, r = " + r);
//                    runAlgoOnFixedBudget(folderRug + "twoRateExpRug" + r + "_" + n + ".csv", lowerBoundTwoRate, getTwoRateExpRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//            thread = new Thread(() -> {
//                try {
////                    System.out.println("Two Rate Exp algo Ruggedness sq, r = " + r);
//                    runAlgoOnFixedBudget(folderRug + "twoRateExpSqRug" + r + "_" + n + ".csv", lowerBoundTwoRateSq, getTwoRateExpRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//        }
//        es.shutdown();
//
////        Run all on int[] lambdaPoints
//        int[] lambdaOMPoints = new int[] {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144};
//        String folder = "PlotsOMNoshiftGradient/";
//        new File(folder).mkdir();
//        ExecutorService es = Executors.newCachedThreadPool();
//        Thread thread;
//
//        for (int lambda : lambdaOMPoints) {
//            thread = new Thread(() -> {
//            try {
//                System.out.println("Two Rate OM " + lambda);
//                runAlgoOnPointGradientPlot(folder + "twoRateOM_" + n + "_" + lambda + ".csv", getTwoRateNoShiftOMImplementation(), lambda, lowerBoundTwoRate);
//            } catch (FileNotFoundException e) {
//            }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//            try {
//                System.out.println("Two Rate sq OM " + lambda);
//                runAlgoOnPointGradientPlot(folder + "twoRatesqOM_" + n + "_" + lambda + ".csv", getTwoRateNoShiftOMImplementation(), lambda, lowerBoundTwoRateSq);
//            } catch (FileNotFoundException e) {
//            }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//            try {
//                System.out.println("Two Rate Exp OM " + lambda);
//                runAlgoOnPointGradientPlot(folder + "twoRateExpOM_" + n + "_" + lambda + ".csv", getTwoRateOMExpImplementation(), lambda, lowerBoundTwoRate);
//            } catch (FileNotFoundException e) {
//            }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//            try {
//                System.out.println("Two Rate Exp sq OM " + lambda);
//                runAlgoOnPointGradientPlot(folder + "twoRateExpsqOM_" + n + "_" + lambda + ".csv", getTwoRateOMExpImplementation(), lambda, lowerBoundTwoRateSq);
//            } catch (FileNotFoundException e) {
//            }
//            });
//            es.execute(thread);
//        }
//        es.shutdown();
//
////        Run twoRateNew on all problems
//        String folder = "TwoRateNewStagnationVersion_" + n + "/";
//        new File(folder).mkdir();
//        ExecutorService es = Executors.newCachedThreadPool();
//        Thread thread;
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNewOM.csv", lowerBoundTwoRate, getTwoRateNewOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNewOMsq.csv", lowerBoundTwoRateSq, getTwoRateNewOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        int point = 3200;
//        thread = new Thread(() -> {
//        try {
//            runAlgoOnPoint(folder + "twoRateNewOMsq" + point + ".csv", getTwoRateNewOMImplementation().getInstance(point, lowerBoundTwoRateSq, n));
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//            try {
//                runAlgoOnPoint(folder + "twoRateNewOM" + point + ".csv", getTwoRateNewOMImplementation().getInstance(point, lowerBoundTwoRate, n));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNewNeutral.csv", lowerBoundTwoRate, getTwoRateNewNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNewNeutralsq.csv", lowerBoundTwoRateSq, getTwoRateNewNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        int[] articleRugs = {2, 10, 20};
//        int budget = 1000;
//        String folderRugs = folder + "/" + "RuggednessFixedBudget" + budget + "/";
//        new File(folderRugs).mkdir();
//        for (int r : articleRugs) {
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateNewRuggedness" + r + ".csv", lowerBoundTwoRate, getTwoRateNewRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateNewRuggedness" + r + "sq.csv", lowerBoundTwoRateSq, getTwoRateNewRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//        }
//
////        Run twoRateStagnationDetection on all problems
//        String folder = "TwoRateStagnationDetection_" + n + "/";
//        new File(folder).mkdir();
//        ExecutorService es = Executors.newCachedThreadPool();
//        Thread thread;
//
//        int point = 3200;
//        thread = new Thread(() -> {
//            try {
//                runAlgoOnPoint(folder + "twoRateStagDetectOMsq" + point + ".csv", getTwoRateStagDetectOMImplementation().getInstance(point, lowerBoundTwoRateSq, n));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//            try {
//                runAlgoOnPoint(folder + "twoRateStagDetectOM" + point + ".csv", getTwoRateStagDetectOMImplementation().getInstance(point, lowerBoundTwoRate, n));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateStagDetectOM.csv", lowerBoundTwoRate, getTwoRateStagDetectOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateStagDetectOMsq.csv", lowerBoundTwoRateSq, getTwoRateStagDetectOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateStagDetectNeutral.csv", lowerBoundTwoRate, getTwoRateStagDetectNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateStagDetectNeutralsq.csv", lowerBoundTwoRateSq, getTwoRateStagDetectNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        int[] articleRugs = {2, 10, 20};
//        int budget = 1000;
//        String folderRugs = folder + "/" + "RuggednessFixedBudget" + budget + "/";
//        new File(folderRugs).mkdir();
//        for (int r : articleRugs) {
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateStagDetectRuggedness" + r + ".csv", lowerBoundTwoRate, getTwoRateStagDetectRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateStagDetectRuggedness" + r + "sq.csv", lowerBoundTwoRateSq, getTwoRateStagDetectRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//        }
//
//        es.shutdown();
//
//
////        Run twoRateNoShift on all problems
//        String folder = "TwoRateNoShift_" + n + "/";
//        new File(folder).mkdir();
//        ExecutorService es = Executors.newCachedThreadPool();
//        Thread thread;
//
//        int point = 3200;
//        thread = new Thread(() -> {
//            try {
//                runAlgoOnPoint(folder + "twoRateNoShiftOMsq" + point + ".csv", getTwoRateNoShiftOMImplementation().getInstance(point, lowerBoundTwoRateSq, n));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//            try {
//                runAlgoOnPoint(folder + "twoRateNoShiftOM" + point + ".csv", getTwoRateNoShiftOMImplementation().getInstance(point, lowerBoundTwoRate, n));
//            } catch (FileNotFoundException e) {
//            }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNoShiftOM.csv", lowerBoundTwoRate, getTwoRateNoShiftOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNoShiftOMsq.csv", lowerBoundTwoRateSq, getTwoRateNoShiftOMImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNoShiftNeutral.csv", lowerBoundTwoRate, getTwoRateNoShiftNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        thread = new Thread(() -> {
//        try {
//            runAlgo(folder + "twoRateNoShiftNeutralsq.csv", lowerBoundTwoRateSq, getTwoRateNoShiftNeutralImplementation());
//        } catch (FileNotFoundException e) {
//        }
//        });
//        es.execute(thread);
//
//        int[] articleRugs = {2, 10, 20};
//        int budget = 1000;
//        String folderRugs = folder + "/" + "RuggednessFixedBudget" + budget + "/";
//        new File(folderRugs).mkdir();
//        for (int r : articleRugs) {
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateNoShiftRuggedness" + r + ".csv", lowerBoundTwoRate, getTwoRateNoShiftRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//
//            thread = new Thread(() -> {
//                try {
//                    runAlgoOnFixedBudget(folderRugs + "twoRateNoShiftRuggedness" + r + "sq.csv", lowerBoundTwoRateSq, getTwoRateNoShiftRugImplementation(r), budget);
//                } catch (FileNotFoundException e) {
//                }
//            });
//            es.execute(thread);
//        }
//
//        Run twoRateByFlipBits on all problems
        String folder = "TwoRateByFlipBits_" + n + "/";
        new File(folder).mkdir();
        ExecutorService es = Executors.newCachedThreadPool();
        Thread thread;

        int point = 3200;
        thread = new Thread(() -> {
            try {
                runAlgoOnPoint(folder + "twoRateByFlipBitssq" + point + ".csv", getTwoRateByFlipBitsOMImplementation().getInstance(point, lowerBoundTwoRateByFlipBitsSq, n));
            } catch (FileNotFoundException e) {
            }
        });
        es.execute(thread);

        thread = new Thread(() -> {
            try {
                runAlgoOnPoint(folder + "twoRateByFlipBitsOM" + point + ".csv", getTwoRateByFlipBitsOMImplementation().getInstance(point, lowerBoundTwoRateByFlipBits, n));
            } catch (FileNotFoundException e) {
            }
        });
        es.execute(thread);

        thread = new Thread(() -> {
        try {
            runAlgo(folder + "twoRateByFlipBitsOM.csv", lowerBoundTwoRateByFlipBits, getTwoRateByFlipBitsOMImplementation());
        } catch (FileNotFoundException e) {
        }
        });
        es.execute(thread);

        thread = new Thread(() -> {
        try {
            runAlgo(folder + "twoRateByFlipBitsOMsq.csv", lowerBoundTwoRateByFlipBitsSq, getTwoRateByFlipBitsOMImplementation());
        } catch (FileNotFoundException e) {
        }
        });
        es.execute(thread);

        thread = new Thread(() -> {
        try {
            runAlgo(folder + "twoRateByFlipBitsNeutral.csv", lowerBoundTwoRateByFlipBits, getTwoRateByFlipBitsNeutralImplementation());
        } catch (FileNotFoundException e) {
        }
        });
        es.execute(thread);

        thread = new Thread(() -> {
        try {
            runAlgo(folder + "twoRateByFlipBitsNeutralsq.csv", lowerBoundTwoRateByFlipBitsSq, getTwoRateByFlipBitsNeutralImplementation());
        } catch (FileNotFoundException e) {
        }
        });
        es.execute(thread);

        int[] articleRugs = {20};
        int budget = 1000;
        String folderRugs = folder + "/" + "RuggednessFixedBudget" + budget + "/";
        new File(folderRugs).mkdir();
        for (int r : articleRugs) {
            thread = new Thread(() -> {
                try {
                    runAlgoOnFixedBudget(folderRugs + "twoRateByFlipBitsRuggedness" + r + ".csv", lowerBoundTwoRateByFlipBits, getTwoRateByFlipBitsRugImplementation(r), budget);
                } catch (FileNotFoundException e) {
                }
            });
            es.execute(thread);

            thread = new Thread(() -> {
                try {
                    runAlgoOnFixedBudget(folderRugs + "twoRateByFlipBitsRuggedness" + r + "sq.csv", lowerBoundTwoRateByFlipBitsSq, getTwoRateByFlipBitsRugImplementation(r), budget);
                } catch (FileNotFoundException e) {
                }
            });
            es.execute(thread);
        }

        es.shutdown();

    }

    private static void runHeavyTailAlgo(String filename, double beta, HeavyTailAlgoFactoryOld factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("gen, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + beta);
            double averageIterCount = 0;
            for (int i = 0; i < runCount; i++) {
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

    private static void runHeavyTailAlgoOnFixedBudget(String filenameAveraged, String filenameNotAveraged, double beta, HeavyTailAlgoFactoryOld factory, int budget) throws FileNotFoundException {
        PrintWriter pwAveraged = new PrintWriter(filenameAveraged);
        pwAveraged.println("fitness, lambda");
        PrintWriter pwNotAveraged = new PrintWriter(filenameNotAveraged);
        pwNotAveraged.println("fitness, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + beta);
            double averageFitness = 0;
            int iterCount = budget / lambda;
            for (int i = 0; i < runCount; i++) {
                int curIterCount = 0;
                Algorithm algo = factory.getInstance(lambda, beta, n);
                while (curIterCount < iterCount) {
                    algo.makeIteration();
//                    algo.printInfo();
                    curIterCount++;
                }
                averageFitness = (i == 0) ? algo.getFitness() : (averageFitness * i + algo.getFitness()) / (i + 1);
                System.out.println(i);
                pwNotAveraged.println(algo.getFitness() + ", " + lambda);
            }
            pwAveraged.println(averageFitness + ", " + lambda);
        }
        pwAveraged.close();
        pwNotAveraged.close();
    }

    private static void runAlgo(String filename, double lowerBound, AlgoFactoryOld factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("gen, lambda");
        for (int lambda : lambdas) {
            System.out.println(filename + " " + lambda + " " + lowerBound);
            double averageIterCount = 0;
            for (int i = 0; i < runCount; i++) {
                int curIterCount = 0;
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
//                    algo.printInfo();
//                    curIterCount++;
                }
                pw.println(algo.getIterCount() + ", " + lambda);
//                averageIterCount = (i == 0) ? curIterCount : (averageIterCount * i + curIterCount) / (i + 1);
                System.out.println(filename + " " + lambda + " " + i);
            }
            pw.flush();
//            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void runAlgoOnFixedBudget(String filenameNotAveraged, double lowerBound, AlgoFactoryOld factory, int budget) throws FileNotFoundException {
//        PrintWriter pwAveraged = new PrintWriter(filenameAveraged);
//        pwAveraged.println("fitness, lambda");
        PrintWriter pw = new PrintWriter(filenameNotAveraged);
        pw.println("fitness, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageFitness = 0;
//            int iterCount = budget / lambda;
            int iterCount = budget;
            for (int i = 0; i < runCount; i++) {
                int curIterCount = 0;
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (curIterCount < iterCount) {
                    algo.makeIteration();
//                    algo.printInfo();
                    curIterCount++;
                }
//                averageFitness = (i == 0) ? algo.getFitness() : (averageFitness * i + algo.getFitness()) / (i + 1);
                System.out.println(filenameNotAveraged + " " + lambda + " " + i);
                pw.println(algo.getFitness() + ", " + lambda);
            }
            pw.flush();
//            pwAveraged.println(averageFitness + ", " + lambda);
        }
//        pwAveraged.close();
        pw.close();
    }

    private static void runAlgoOnPoint(String filename, Algorithm algo) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
//        pw.println("fitness, mutation, iter, zero, one, two, three");
        pw.println("fitness, mutation, iter");
        int curIterCount = 0;
        while (!algo.isFinished()) {
            algo.makeIteration();
//            curIterCount++;
//            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + curIterCount + algo.getProblemInfo());
            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + algo.getIterCount());
//            algo.printInfo();
        }
        pw.close();
    }

    private static void runAlgoOnPointGradientPlot(String filename, AlgoFactoryOld factory, int lambda, double lowerBound) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
//        pw.println("fitness, mutation, iter, zero, one, two, three");
        for (int i = 0; i < runCount; i++) {
            int curIterCount = 0;
            pw.println("\\addplot coordinates {");
            Map<Integer, List<Double>> vals = new TreeMap<>();
            Algorithm algo = factory.getInstance(lambda, lowerBound, n);
            while (!algo.isFinished()) {
                algo.makeIteration();
                curIterCount++;
                if (!vals.containsKey(n - algo.getFitness())) {
                    vals.put(n - algo.getFitness(), new ArrayList<>());
                }
                vals.get(n - algo.getFitness()).add(algo.getMutationRate() * n);
//                pw.print("(" + (n - algo.getFitness()) + ", " + algo.getMutationRate() + ")");
            }
            for (int dist : vals.keySet()) {
                pw.print("(" + dist + ", " + vals.get(dist).get(0) + ")" + "(" + dist + ", " + vals.get(dist).get(vals.get(dist).size() - 1) + ")");
                pw.print("(" + dist + ", " + Collections.min(vals.get(dist)) + ")" + "(" + dist + ", " + Collections.max(vals.get(dist)) + ")");
            }
            System.out.println(filename + " " + lambda + " " + i);
            pw.println("};");
        }
        pw.close();
    }



    private static void runAlgoByFitnessCount(String filename, double lowerBound, AlgoFactoryOld factory) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("fitCou, lambda, iter");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageFitCou = 0;
            double averageIterCou = 0;
            for (int i = 0; i < runCount; i++) {
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
    private static AlgoFactoryOld getTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    private static AlgoFactoryOld getTwoRatePlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    //Two Rate No Shift Doerr
    private static AlgoFactoryOld getTwoRateOMNoShiftImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShiftFitnessCount(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    //Two Rate Exp
    private static AlgoFactoryOld getTwoRateOMExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateLOExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNeutralExpImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateExpRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    private static AlgoFactoryOld getTwoRateExpPlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    //Two Rate NoShift
    private static AlgoFactoryOld getTwoRateNoShiftOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNoShiftLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNoShiftNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNoShiftRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    private static AlgoFactoryOld getTwoRateNoShiftPlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    //Two Rate New (Stagnation experiments1)
    private static AlgoFactoryOld getTwoRateNewOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNewLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNewNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateNewRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    private static AlgoFactoryOld getTwoRateNewPlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    //Two Rate Stagnation Detection (ver.2)
    private static AlgoFactoryOld getTwoRateStagDetectOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateStagDetectLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateStagDetectNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateStagDetectRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    private static AlgoFactoryOld getTwoRateStagDetectPlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    //Two Rate Adaptive
    private static AlgoFactoryOld getAdaptiveTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getAdaptiveDivTwoRateOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveDivTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength));
    }

    //Two Rate Square Start
    private static AlgoFactoryOld getTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getAdaptiveTwoRateOMSQImplementation() {
        return (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0 / n, lowerBound, lambda, new OneMax(problemLength));
    }

    //AB
    private static AlgoFactoryOld getABExpOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgoToExplore(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getABOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getABSQOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / (n * n), 2, 0.5, lowerBound, lambda, new OneMax(problemLength));
    }

    //Simple
    private static AlgoFactoryOld getSimpleEAOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getSimpleEAPlateauImplementation(int k) {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Plateau(problemLength, k));
    }

    private static AlgoFactoryOld getSimpleEANeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getSimpleEARugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Ruggedness(problemLength, r));
    }

    //Heavy Tail
    private static HeavyTailAlgoFactoryOld getHeavyTailOMImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new OneMax(problemLength)));
    }

    private static HeavyTailAlgoFactoryOld getHeavyTailLOImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new LeadingOnes(problemLength)));
    }

    private static HeavyTailAlgoFactoryOld getHeavyTailRugImplementation(int r) {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Ruggedness(problemLength, r)));
    }

    private static HeavyTailAlgoFactoryOld getHeavyTailNeutralImplementation() {
        return ((lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Neutral3(problemLength)));
    }

    //TwoRateByFlipBits
    private static AlgoFactoryOld getTwoRateByFlipBitsOMImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new OneMax(problemLength));
    }

    private static AlgoFactoryOld getTwoRateByFlipBitsLOImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new LeadingOnes(problemLength));
    }

    private static AlgoFactoryOld getTwoRateByFlipBitsNeutralImplementation() {
        return (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new Neutral3(problemLength));
    }

    private static AlgoFactoryOld getTwoRateByFlipBitsRugImplementation(int r) {
        return (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new Ruggedness(problemLength, r));
    }
}
