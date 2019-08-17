import algo.ABalgo;
import algo.Algorithm;
import algo.TwoRate;
import problem.OneMax;
import problem.Problem;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println("two rate");
//        testTwoRate();
//        System.out.println("AB algorithm");
//        testAbalgo();
        System.out.println("two rate sq");
        testTwoRateSq();
        System.out.println("AB algorithm sq");
        testAbalgoSq();

    }

    private static void testTwoRate() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("tworate.csv");
        pw.println("gen, lambda");
        int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
        int n = 1000;
        for (int lambda : lambdas) {
            System.out.println(lambda);
            Problem om = new OneMax(n);
            Algorithm tr = new TwoRate(2, 2.0 / n, lambda, om);
            int iterCount = 0;
            while (om.getFitness() != n) {
                tr.makeIteration();
                iterCount++;
            }
            pw.println(iterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void testAbalgo() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("ab.csv");
        pw.println("gen, lambda");
        int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
        int n = 1000;
        for (int lambda : lambdas) {
            System.out.println(lambda);
            Problem om = new OneMax(n);
            Algorithm ab = new ABalgo(1.0 / n, 2, 0.5, 1.0 / n, lambda, om);
            int iterCount = 0;
            while (om.getFitness() != n) {
                ab.makeIteration();
                iterCount++;
            }
            pw.println(iterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void testTwoRateSq() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("tworatesq.csv");
        pw.println("gen, lambda");
        int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
        int n = 1000;
        for (int lambda : lambdas) {
            System.out.println(lambda);
            Problem om = new OneMax(n);
            Algorithm tr = new TwoRate(2, 2.0 / (n * n), lambda, om);
            int iterCount = 0;
            while (om.getFitness() != n) {
                tr.makeIteration();
                iterCount++;
            }
            pw.println(iterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void testAbalgoSq() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("absq.csv");
        pw.println("gen, lambda");
        int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800, 1600, 3200};
        int n = 1000;
        for (int lambda : lambdas) {
            System.out.println(lambda);
            Problem om = new OneMax(n);
            Algorithm ab = new ABalgo(1.0 / n, 2, 0.5, 1.0 / (n * n), lambda, om);
            int iterCount = 0;
            while (om.getFitness() != n) {
                ab.makeIteration();
                iterCount++;
            }
            pw.println(iterCount + ", " + lambda);
        }
        pw.close();
    }

}
