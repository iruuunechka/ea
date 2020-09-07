package runners.newRunner;

import algo.AlgoFactory;
import algo.Algorithm;
import runners.newRunner.parameterSets.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static runners.newRunner.Params.*;


public class RunAlgorithm {

    public static void main(String[] args) throws FileNotFoundException {
        ExecutorService es = Executors.newCachedThreadPool();
        String mainFolder = "HQEAABNeutral" + n + "_" + runCount + "/";
        new File(mainFolder).mkdir();
//        Example:
//        runAlgo(es, mainFolder, runByOptimum());
//        Problems.OM.used = false;
//        Problems.LO.used = false;
//        Problems.RUG.used = true;
//        runAlgo(es, mainFolder, runOnFixedBudget());
        runAlgo(es, mainFolder);
//        runAlgo(es, mainFolder, runByOptimum());
        es.shutdown();
//        es.shutdown();
    }

    public static void runAlgo(ExecutorService es, String mainFolder) {
        Params.Algos.stream().filter(a -> a.used).forEach(algo -> {
            Runner runner = algo.runner;
            for (String problem : Params.Problems.stream().filter(p -> p.used).map(p -> p.name).toArray(String[]::new)) {
                try {
                    if (problem.equals("Rug")) {
                        for (int r : rugs) {
                            runner.runAlgo(es, mainFolder + algo.name + "_" + problem + r + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem + r));
                            runner.runAlgo(es, mainFolder + algo.name + "_" + problem + r + "sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem + r));
                        }
                    } else if (problem.equals("Plateau")) {
                        for (int k : plateaus) {
                            runner.runAlgo(es, mainFolder + algo.name + "_" + problem + k + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem + k));
                            runner.runAlgo(es, mainFolder + algo.name + "_" + problem + k + "sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem + k));
                        }
                    } else {
                        runner.runAlgo(es, mainFolder + algo.name + "_" + problem + "_" + ".csv", algo.lowerBoundConst / n, getAlgoFactory(algo.name, problem));
                        runner.runAlgo(es, mainFolder + algo.name + "_" + problem + "_sq.csv", algo.lowerBoundConst / (n * n), getAlgoFactory(algo.name, problem));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static AlgoFactory getAlgoFactory(String algo, String problem) {
        return Params.methods.get(algo + problem);
    }

    public static Runner runByOptimum() {
        return (es, filename, lowerBound, factory) -> {
            es.execute(() -> {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println("gen, lambda");
                for (int lambda : Params.lambdas) {
                    System.out.println(filename + " " + lambda + " " + lowerBound);
                    for (int i = 0; i < Params.runCount; i++) {
                        Algorithm algo = factory.getInstance(new BoundedParameterSet(lambda, lowerBound, n));
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
            });
        };
    }

    public static Runner runABByOptimum() {
        return (es, filename, lowerBound, factory) -> {
            for (double a : Params.a) {
                for (double b : Params.b) {
                    for (boolean strict : Params.strict) {
                        es.execute(() -> {
                            String filenameNew;
                            if (filename.contains("sq")) {
                                    filenameNew = filename.substring(0, filename.length() - 6) + a + "_" + b + "_" + (strict ? "strict" : "") + filename.substring(filename.length() - 6);
                                } else {
                                    filenameNew = filename.substring(0, filename.length() - 4) + a + "_" + b + "_" + (strict ? "strict" : "") + filename.substring(filename.length() - 4);
                                }
                            PrintWriter pw = null;
                            try {
                                pw = new PrintWriter(filenameNew);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            pw.println("gen, lambda");
                            for (int lambda : Params.lambdas) {
                                    System.out.println(filenameNew + " " + lambda + " " + lowerBound);
                                    for (int i = 0; i < Params.runCount; i++) {
                                        Algorithm algo = factory.getInstance(new ABParameterSet(lambda, lowerBound, n, a, b, strict));
                                        while (!algo.isFinished()) {
                                            algo.makeIteration();
                                            //                    algo.printInfo();
                                        }
                                        pw.println(algo.getIterCount() + ", " + lambda);
                                        System.out.println(filenameNew + " " + lambda + " " + i);
                                    }
                                    pw.flush();
                                }
                            pw.close();
                        });
                    }
                }
            }
        };
    }

    public static Runner runLearningByOptimum() {
        return (es, filename, lowerBound, factory) -> {
            for (double alpha : Params.alphas) {
                for (double gamma : Params.gammas) {
                    for (double epsilon : Params.epsilon) {
                        for (double a : Params.a){
                            for (double b : Params.b) {
                                for (boolean strict : Params.strict) {
                                    es.execute(() -> {
                                        String filenameNew;
                                        if (filename.contains("sq")) {
                                            filenameNew = filename.substring(0, filename.length() - 6) + alpha + "_" + gamma + "_" + epsilon + "_" + a + "_" + b + "_" + (strict ? "strict" : "") + filename.substring(filename.length() - 6);
                                        } else {
                                            filenameNew = filename.substring(0, filename.length() - 4) + alpha + "_" + gamma + "_" + epsilon + "_" + a + "_" + b + "_" + (strict ? "strict" : "") + filename.substring(filename.length() - 4);
                                        }
                                        PrintWriter pw = null;
                                        try {
                                            pw = new PrintWriter(filenameNew);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        pw.println("gen, lambda");
                                        for (int lambda : Params.lambdas) {
                                            System.out.println(filenameNew + " " + lambda + " " + lowerBound);
                                            for (int i = 0; i < Params.runCount; i++) {
                                                Algorithm algo = factory.getInstance(new ABLearningParameterSet(lambda, lowerBound, n, a, b, strict, alpha, gamma, epsilon));
                                                while (!algo.isFinished()) {
                                                    algo.makeIteration();
//                                                    algo.printInfo();
                                                }
                                                pw.println(algo.getIterCount() + ", " + lambda);
                                                System.out.println(filenameNew + " " + lambda + " " + i + " " + algo.getIterCount());
                                            }
                                            pw.flush();
                                        }
                                        pw.close();

                                    });
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Runner runOnFixedBudget() {
        return (es, filename, lowerBound, factory) -> {
            es.execute(() -> {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println("fitness, lambda");
                for (int lambda : Params.lambdas) {
                    System.out.println(lambda + " " + lowerBound);
//            int iterCount = budget / lambda;
                    int iterCount = Params.budget;
                    for (int i = 0; i < Params.runCount; i++) {
                        int curIterCount = 0;
                        Algorithm algo = factory.getInstance(new BoundedParameterSet(lambda, lowerBound, n));
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
            });
        };
    }

    public static Runner runOnPoint() {
        return (es, filename, lowerBound, factory) -> {
            es.execute(()-> {
                for (int lambda : Params.lambdaPoints) {
                    String filenameNew;
                    if (filename.contains("sq")) {
                        filenameNew = filename.substring(0, filename.length() - 6) + "_" + lambda + filename.substring(filename.length() - 6);
                    } else {
                        filenameNew = filename.substring(0, filename.length() - 4) + "_" + lambda + filename.substring(filename.length() - 4);
                    }
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(filenameNew);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//        pw.println("fitness, mutation, iter, zero, one, two, three");
                    pw.println("fitness, mutation, iter");
//                int curIterCount = 0;
                    Algorithm algo = factory.getInstance(new BoundedParameterSet(lambda, lowerBound, n));
                    while (!algo.isFinished()) {
                        algo.makeIteration();
//            curIterCount++;
//            pw.println(algo.getFitness() + ", " + algo.getMutationRate()  + ", " + curIterCount + algo.getProblemInfo());
                        pw.println(algo.getFitness() + ", " + algo.getMutationRate() + ", " + algo.getIterCount());
//            algo.printInfo();
                    }
                    pw.close();
                }
            });
        };
    }

    public static Runner runOnPointGradientPlot() {
        return (es, filename, lowerBound, factory) -> {
            es.execute(() -> {
                for (int lambda : Params.lambdaPoints) {
                    String filenameNew;
                    if (filename.contains("sq")) {
                        filenameNew = filename.substring(0, filename.length() - 6) + "_" + lambda + filename.substring(filename.length() - 6);
                    } else {
                        filenameNew = filename.substring(0, filename.length() - 4) + "_" + lambda + filename.substring(filename.length() - 4);
                    }
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(filenameNew);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//        pw.println("fitness, mutation, iter, zero, one, two, three");
                    for (int i = 0; i < Params.runCount; i++) {
//                    int curIterCount = 0;
                        pw.println("\\addplot coordinates {");
                        Map<Integer, List<Double>> vals = new TreeMap<>();
                        Algorithm algo = factory.getInstance(new BoundedParameterSet(lambda, lowerBound, n));
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
            });
        };
    }

    public static Runner runABLearningOnPointGradientPlot() {
        return (es, filename, lowerBound, factory) -> {
            for (double alpha : Params.alphas) {
                for (double gamma : Params.gammas) {
                    for (double epsilon : Params.epsilon) {
                        for (double a : Params.a){
                            for (double b : Params.b) {
                                for (boolean strict : Params.strict) {
                                    es.execute(() -> {
                                        for (int lambda : Params.lambdaPoints) {
                                            String filenameNew;
                                            if (filename.contains("sq")) {
                                                filenameNew = filename.substring(0, filename.length() - 6) + alpha + "_" + gamma + "_" + epsilon + "_" + a + "_" + b + (strict ? "_strict" : "") + "_" + lambda + filename.substring(filename.length() - 6);
                                            } else {
                                                filenameNew = filename.substring(0, filename.length() - 4) + alpha + "_" + gamma + "_" + epsilon + "_" + a + "_" + b + (strict ? "_strict" : "") + "_" + lambda + filename.substring(filename.length() - 4);
                                            }
                                            PrintWriter pw = null;
                                            try {
                                                pw = new PrintWriter(filenameNew);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            for (int i = 0; i < Params.runCount; i++) {
                                                pw.println("\\addplot coordinates {");
                                                Map<Integer, List<Double>> vals = new TreeMap<>();
                                                Algorithm algo = factory.getInstance(new ABLearningParameterSet(lambda, lowerBound, n, a, b, strict, alpha, gamma, epsilon));
                                                while (!algo.isFinished()) {
                                                    algo.makeIteration();
                                                    if (!vals.containsKey(n - algo.getFitness())) {
                                                        vals.put(n - algo.getFitness(), new ArrayList<>());
                                                    }
                                                    vals.get(n - algo.getFitness()).add(algo.getMutationRate() * n);
                                                }
                                                for (int dist : vals.keySet()) {
                                                    pw.print("(" + dist + ", " + vals.get(dist).get(0) + ")" + "(" + dist + ", " + vals.get(dist).get(vals.get(dist).size() - 1) + ")");
                                                    pw.print("(" + dist + ", " + Collections.min(vals.get(dist)) + ")" + "(" + dist + ", " + Collections.max(vals.get(dist)) + ")");
                                                }
                                                System.out.println(filenameNew + " " + lambda + " " + i);
                                                pw.println("};");
                                            }
                                            pw.close();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        };
    }


    public static Runner runAlgoByFitnessCount() {
        return (es, filename, lowerBound, factory) -> {
            es.execute(() -> {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println("fitCou, lambda, iter");
                for (int lambda : Params.lambdas) {
                    System.out.println(lambda + " " + lowerBound);
                    double averageFitCou = 0;
                    double averageIterCou = 0;
                    for (int i = 0; i < Params.runCount; i++) {
                        Algorithm algo = factory.getInstance(new BoundedParameterSet(lambda, lowerBound, n));
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
            });
        };
    }


    public static void runHeavyTailAlgo(ExecutorService es, String mainFolder, HeavyTailAlgoRunner runner) {
        Params.Algos.stream().filter(a -> a.used).forEach(algo -> {
            for (String problem : Params.Problems.stream().filter(p -> p.used).map(p -> p.name).toArray(String[]::new)) {
                try {
                    if (problem.equals("Rug")) {
                        for (int r : rugs) {
                            runner.runAlgo(es, mainFolder + algo.name + problem + r + ".csv", beta, getAlgoFactory(algo.name, problem + r));
                        }
                    } else if (problem.equals("Plateau")) {
                        for (int k : plateaus) {
                            runner.runAlgo(es, mainFolder + algo.name + problem + k + ".csv", beta, getAlgoFactory(algo.name, problem + k));
                        }
                    } else {
                        runner.runAlgo(es, mainFolder + algo.name + problem + ".csv", beta, getAlgoFactory(algo.name, problem));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static HeavyTailAlgoRunner runHeavyTailAlgo() {
        return (es, filename, beta, factory) -> {
            es.execute(() -> {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println("gen, lambda");
                for (int lambda : Params.lambdas) {
                    System.out.println(lambda + " " + beta);
                    for (int i = 0; i < Params.runCount; i++) {
                        Algorithm algo = factory.getInstance(new HeavyTailParameterSet(lambda, n, beta));
                        while (!algo.isFinished()) {
                            algo.makeIteration();
//                    algo.printInfo();
                        }
                        System.out.println(i);
                        pw.println(algo.getFitness() + ", " + lambda);
                    }
                }
                pw.close();
            });
        };
    }

    public static HeavyTailAlgoRunner runHeavyTailAlgoOnFixedBudget() {
        return (es, filename, beta, factory) -> {
            es.execute(() -> {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pw.println("fitness, lambda");
                for (int lambda : Params.lambdas) {
                    System.out.println(lambda + " " + beta);
                    int iterCount = Params.budget / lambda;
                    for (int i = 0; i < Params.runCount; i++) {
                        int curIterCount = 0;
                        Algorithm algo = factory.getInstance(new HeavyTailParameterSet(lambda, n, beta));
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
            });
        };
    }
}