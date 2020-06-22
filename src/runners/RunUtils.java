package runners;

import algo.AlgoFactory;
import algo.Algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static runners.Params.*;


public class RunUtils {

    public static void main(String[] args) throws FileNotFoundException {
        ExecutorService es = Executors.newCachedThreadPool();
        String mainFolder = "TestNewRunner/";
        new File(mainFolder).mkdir();
//        Example:
//        runAlgo(es, mainFolder, runByOptimum());
//        Problems.OM.used = false;
//        Problems.LO.used = false;
//        Problems.RUG.used = true;
//        runAlgo(es, mainFolder, runOnFixedBudget());
        es.shutdown();
    }

    public static void runAlgo(ExecutorService es, String mainFolder, Runner runner) {
        Params.Algos.stream().filter(a -> a.used).forEach(algo -> {
            for (String problem : Params.Problems.stream().filter(p -> p.used).map(p -> p.name).toArray(String[]::new)) {
                if (problem.equals("Rug")) {
                    for (int r : rugs) {
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + r + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem + r));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + r + "sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem + r));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                    }
                } else if (problem.equals("Plateau")) {
                    for (int k : plateaus) {
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + k + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem + k));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + k + "sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem + k));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                    }
                } else {
                    es.execute(new Thread(() -> {
                        try {
                            runner.runAlgo(mainFolder + algo.name + problem + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }));
                    es.execute(new Thread(() -> {
                        try {
                            runner.runAlgo(mainFolder + algo.name + problem + "sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }
        });
    }

    private static AlgoFactory getAlgoFactory(String algo, String problem) {
        return Params.methods.get(algo + problem);
    }

    public static void runHeavyTailAlgo(ExecutorService es, String mainFolder, HeavyTailAlgoRunner runner) {
        Params.Algos.stream().filter(a -> a.used).forEach(algo -> {
            for (String problem : Params.Problems.stream().filter(p -> p.used).map(p -> p.name).toArray(String[]::new)) {
                if (problem.equals("Rug")) {
                    for (int r : rugs) {
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + r + ".csv", beta, getAlgoFactory(algo.name, problem + r));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                    }
                } else if (problem.equals("Plateau")) {
                    for (int k : plateaus) {
                        es.execute(new Thread(() -> {
                            try {
                                runner.runAlgo(mainFolder + algo.name + problem + k + ".csv", beta, getAlgoFactory(algo.name, problem + k));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }));
                    }
                } else {
                    es.execute(new Thread(() -> {
                        try {
                            runner.runAlgo(mainFolder + algo.name + problem + ".csv", beta, getAlgoFactory(algo.name, problem));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }
        });
    }


    public static Runner runByOptimum() throws FileNotFoundException {
        return (filename, lowerBound, factory) -> {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("gen, lambda");
            for (int lambda : Params.lambdas) {
                System.out.println(filename + " " + lambda + " " + lowerBound);
                for (int i = 0; i < Params.runCount; i++) {
                    Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                    while (!algo.isFinished()) {
                        algo.makeIteration();
//                    algo.printInfo();
                    }
                    pw.println(algo.getIterCount() + ", " + lambda);
                    System.out.println(filename + " " + lambda + " " + i);
                }
                pw.flush();
            }
            pw.close();
        };
    }


    public static Runner runOnFixedBudget() {
        return (filename, lowerBound, factory) -> {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("fitness, lambda");
            for (int lambda : Params.lambdas) {
                System.out.println(lambda + " " + lowerBound);
//            int iterCount = budget / lambda;
                int iterCount = Params.budget;
                for (int i = 0; i < Params.runCount; i++) {
                    int curIterCount = 0;
                    Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                    while (curIterCount < iterCount) {
                        algo.makeIteration();
//                    algo.printInfo();
                        curIterCount++;
                    }
                    System.out.println(filename+ " " + lambda + " " + i);
                    pw.println(algo.getFitness() + ", " + lambda);
                }
                pw.flush();
            }
//        pwAveraged.close();
            pw.close();
        };
    }

    public static Runner runOnPoint() {
        return (filename, lowerBound, factory) -> {
            for (int lambda : Params.lambdaPoints) {
                String filenameNew;
                if (filename.contains("sq")) {
                    filenameNew = filename.substring(0, filename.length() - 6) + "_" + lambda + filename.substring(filename.length() - 6);
                } else {
                    filenameNew = filename.substring(0, filename.length() - 4) + "_" + lambda + filename.substring(filename.length() - 4);
                }
                PrintWriter pw = new PrintWriter(filenameNew);
//        pw.println("fitness, mutation, iter, zero, one, two, three");
                pw.println("fitness, mutation, iter");
//                int curIterCount = 0;
                Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                while (!algo.isFinished()) {
                    algo.makeIteration();
//            curIterCount++;
//            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + curIterCount + algo.getProblemInfo());
                    pw.println(algo.getFitness() + ", " + algo.getMutationRate() + ", " + algo.getIterCount());
//            algo.printInfo();
                }
                pw.close();
            }
        };
    }

    public static Runner runOnPointGradientPlot() {
        return (filename, lowerBound, factory) -> {
            for (int lambda : Params.lambdaPoints) {
                String filenameNew;
                if (filename.contains("sq")) {
                    filenameNew = filename.substring(0, filename.length() - 6) + "_" + lambda + filename.substring(filename.length() - 6);
                } else {
                    filenameNew = filename.substring(0, filename.length() - 4) + "_" + lambda + filename.substring(filename.length() - 4);
                }
                PrintWriter pw = new PrintWriter(filenameNew);
//        pw.println("fitness, mutation, iter, zero, one, two, three");
                for (int i = 0; i < Params.runCount; i++) {
//                    int curIterCount = 0;
                    pw.println("\\addplot coordinates {");
                    Map<Integer, List<Double>> vals = new TreeMap<>();
                    Algorithm algo = factory.getInstance(lambda, lowerBound, n);
                    while (!algo.isFinished()) {
                        algo.makeIteration();
//                        curIterCount++;
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
        };
    }


    public static Runner runAlgoByFitnessCount() {
        return (filename, lowerBound, factory) -> {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("fitCou, lambda, iter");
            for (int lambda : Params.lambdas) {
                System.out.println(lambda + " " + lowerBound);
                double averageFitCou = 0;
                double averageIterCou = 0;
                for (int i = 0; i < Params.runCount; i++) {
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
        };
    }

    public static HeavyTailAlgoRunner runHeavyTailAlgo() {
        return (filename, beta, factory) -> {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("gen, lambda");
            for (int lambda : Params.lambdas) {
                System.out.println(lambda + " " + beta);
                for (int i = 0; i < Params.runCount; i++) {
                    Algorithm algo = factory.getInstance(lambda, beta, n);
                    while (!algo.isFinished()) {
                        algo.makeIteration();
//                    algo.printInfo();
                    }
                    System.out.println(i);
                    pw.println(algo.getFitness() + ", " + lambda);
                }
            }
            pw.close();
        };
    }

    public static HeavyTailAlgoRunner runHeavyTailAlgoOnFixedBudget() {
        return (filename, beta, factory) -> {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("fitness, lambda");
            for (int lambda : Params.lambdas) {
                System.out.println(lambda + " " + beta);
                int iterCount = Params.budget / lambda;
                for (int i = 0; i < Params.runCount; i++) {
                    int curIterCount = 0;
                    Algorithm algo = factory.getInstance(lambda, beta, n);
                    while (curIterCount < iterCount) {
                        algo.makeIteration();
//                    algo.printInfo();
                        curIterCount++;
                    }
                    System.out.println(i);
                    pw.println(algo.getFitness() + ", " + lambda);
                }
            }
            pw.close();
        };
    }
}