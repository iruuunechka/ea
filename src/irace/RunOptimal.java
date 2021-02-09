package irace;

import algo.AlgoFactory;
import algo.Algorithm;
import runners.newRunner.Params;
import runners.newRunner.parameterSets.ABLearningParameterSet;
import runners.newRunner.parameterSets.ABParameterSet;
import runners.newRunner.parameterSets.ParameterSet;

import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static runners.newRunner.Params.n;
import static runners.newRunner.Params.runCount;

public class RunOptimal {
    private static final int runCount = 100;
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String mainFolder = "OptimalAB_RUG/";
        new File(mainFolder).mkdir();
        br.lines().forEach(s -> {
            String[] params = s.split(" ");
            int n = 0;
            int lambda = 0;
            int r = 0;
            double a = 0;
            double b = 0;
            double alpha = 0;
            double gamma = 0;
            double epsilon = 0;
            String problem = null;
            String algo = null;
            boolean strict = false;
            double lowerBound;
            String lb = null;
            for (int i = 0; i < params.length; ++i) {
                switch (params[i]) {
                    case ("--n") :
                        n = Integer.valueOf(params[i + 1]);
                        break;
                    case ("--lambda") :
                        lambda = Integer.valueOf(params[i + 1]);
                        break;
                    case ("--a") :
                        a = Double.valueOf(params[i + 1]);
                        break;
                    case ("--b") :
                        b = Double.valueOf(params[i + 1]);
                        break;
                    case ("--alpha") :
                        alpha = Double.valueOf(params[i + 1]);
                        break;
                    case ("--gamma") :
                        gamma = Double.valueOf(params[i + 1]);
                        break;
                    case ("--epsilon") :
                        epsilon = Double.valueOf(params[i + 1]);
                        break;
                    case ("--problem") :
                        problem = params[i + 1];
                        break;
                    case ("--algo") :
                        algo = params[i + 1];
                        break;
                    case ("--lowerbound") :
                        lb = params[i + 1];
                        break;
                    case ("--strict") :
                        strict = Boolean.valueOf(params[i + 1]);
                        break;
                    case ("--r") :
                        r = Integer.valueOf(params[i + 1]);
                }
            }
            String filename = mainFolder + algo + "_" + problem + (strict ? "strict" : "")  + (lb.equals("sq") ? "sq" : "")  + ".csv";
            File f = new File(filename);
            Boolean exists = f.exists();
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(filename, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!exists) {
                pw.println("gen, lambda");
            }
            lowerBound = (lb.equals("sq") ? Params.Algos.valueOf(algo).lowerBoundConst / (n * n) : (Params.Algos.valueOf(algo).lowerBoundConst / n));
            int[] res = new int[runCount];
//            System.out.println(lambda);
            System.out.println(lambda + " " + a + " " + b + " " + (-Math.log(b) / Math.log(a)));
            for (int i = 0; i < runCount; ++i) {
                String algoProblem = Params.Algos.valueOf(algo).name + Params.Problems.valueOf(problem).name;
                if (problem.equals("RUG")) {
                    algoProblem += r;
                }
                AlgoFactory factory = Params.methods.get(algoProblem);
                //                ParameterSet ps = new ABParameterSet(lambda, lowerBound, n, a, b, strict);
                ParameterSet ps = new ABLearningParameterSet(lambda, lowerBound, n, a, b, strict, alpha, gamma, epsilon);
                Algorithm algorithm = factory.getInstance(ps);
                while (!algorithm.isFinished()) {
                    algorithm.makeIteration();
                }
                pw.println(algorithm.getIterCount() + ", " + lambda);
            }
            pw.close();
        });
    }
}
