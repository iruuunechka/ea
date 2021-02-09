package irace;

import algo.AlgoFactory;
import algo.Algorithm;
import runners.newRunner.Params;
import runners.newRunner.parameterSets.ABLearningParameterSet;
import runners.newRunner.parameterSets.ABParameterSet;
import runners.newRunner.parameterSets.BoundedParameterSet;
import runners.newRunner.parameterSets.ParameterSet;

import java.util.Arrays;

public class MainIrace {
    private static final int runCount = 5;
    public static void main(String[] args) {
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
        int maxIter = 0;
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case ("--n") :
                    n = Integer.valueOf(args[i + 1]);
                    break;
                case ("--lambda") :
                    lambda = Integer.valueOf(args[i + 1]);
                    break;
                case ("--a") :
                    a = Double.valueOf(args[i + 1]);
                    break;
                case ("--b") :
                    b = Double.valueOf(args[i + 1]);
                    break;
                case ("--alpha") :
                    alpha = Double.valueOf(args[i + 1]);
                    break;
                case ("--gamma") :
                    gamma = Double.valueOf(args[i + 1]);
                    break;
                case ("--epsilon") :
                    epsilon = Double.valueOf(args[i + 1]);
                    break;
                case ("--problem") :
                    problem = args[i + 1];
                    break;
                case ("--algo") :
                    algo = args[i + 1];
                    break;
                case ("--lowerbound") :
                    lb = args[i + 1];
                    break;
                case ("--strict") :
                    strict = Boolean.valueOf(args[i + 1]);
                    break;
                case ("--maxIter") :
                    maxIter = Integer.valueOf(args[i + 1]);
                    break;
                case ("--r") :
                    r = Integer.valueOf(args[i + 1]);
            }
        }
        lowerBound = (lb.equals("sq") ? Params.Algos.valueOf(algo).lowerBoundConst / (n * n) : (Params.Algos.valueOf(algo).lowerBoundConst / n));
        int[] res = new int[runCount];
        for (int i = 0; i < runCount; ++i) {
            String algoProblem = Params.Algos.valueOf(algo).name + Params.Problems.valueOf(problem).name;
            if (problem.equals("RUG")) {
                algoProblem += r;
            }
            AlgoFactory factory = Params.methods.get(algoProblem);
//            ParameterSet ps = new ABLearningParameterSet(lambda, lowerBound, n, a, b, strict, alpha, gamma, epsilon);
            ParameterSet ps = new ABParameterSet(lambda, lowerBound, n, a, b, strict);
            try {
                Algorithm algorithm = factory.getInstance(ps);
                while (!algorithm.isFinished() && (algorithm.getIterCount() < maxIter)) {
                    algorithm.makeIteration();
                }
                res[i] = algorithm.getIterCount();
            } catch (NullPointerException e) {
                System.out.println(algoProblem);
                System.out.println(factory);
                System.out.println(lambda + " " + a + " " + b + " " + strict + " " + alpha + " " + gamma + " " + epsilon);
                System.out.println(ps);
            }
        }
        Arrays.sort(res);
        System.out.print(res[runCount / 2]);
    }
}
