import algo.ABalgo;
import algo.Algorithm;
import algo.TwoRate;
import problem.OneMax;
import problem.Problem;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    private static final int[] lambdas = new int[] {6, 10, 50, 100, 200, 400, 800};//, 1600, 3200};
    private static final int n = 1000;
    private static final int avCount = 10;


    public static void main(String[] args) throws FileNotFoundException {
        double lowerBoundTwoRate = 2.0 / n;
        double lowerBoundTwoRateSq = 2.0 / (n * n);

        System.out.println("two rate");
        testTwoRate("tworate.csv", lowerBoundTwoRate);
        System.out.println("two rate sq");
        testTwoRate("tworatesq.csv", lowerBoundTwoRateSq);

//        System.out.println("AB algorithm");
//        testAbalgo();
//        System.out.println("AB algorithm sq");
//        testAbalgoSq();

    }

    private static void testTwoRate(String filename, double lowerBound) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filename);
        pw.println("gen, lambda");
        for (int lambda : lambdas) {
            System.out.println(lambda + " " + lowerBound);
            double averageIterCount = 0;
            for (int i = 0; i < avCount; i++) {
                int curIterCount = 0;
                Problem om = new OneMax(n);
                TwoRate tr = new TwoRate(2, lowerBound, lambda, om);
                while (om.getFitness() != n) {
                    tr.makeIteration();
                    curIterCount++;
                }
                averageIterCount = (i == 0) ? curIterCount : (averageIterCount * i + curIterCount) / (i + 1);
//                System.out.println("decCou: " + tr.decreaseCount + " incCou: " + tr.increaseCount + " eqCou: " + tr.equalCount);

//            for (String s : ((TwoRate) tr).decreaseCountInfo) {
//                System.out.print(s + " ");
//            }
//            System.out.println();
            }
            pw.println((int) averageIterCount + ", " + lambda);
        }
        pw.close();
    }

    private static void testAbalgo() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("ab.csv");
        pw.println("gen, lambda");
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

    private static void testAbalgoSq() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter("absq.csv");
        pw.println("gen, lambda");
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
