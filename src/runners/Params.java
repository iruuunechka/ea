package runners;

import algo.*;
import problem.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Params {
    public static final int n = 100;
    public static final int[] lambdas = {2, 4};//, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096};
    public static final int[] lambdaPoints = {2, 4};
    public static final int runCount = 3;
    public static final int[] rugs = {2, 5};
    public static final int[] plateaus = {2};
    public static final int budget = 1000;
    public static final double beta = 2.5;
    private static final double lowerBoundTwoRate = 2.0 / n;
    private static final double lowerBoundTwoRateSq = 2.0 / (n * n);
    private static final double lowerBoundTwoRateByFlipBits = 1.0 / n;
    private static final double lowerBoundTwoRateByFlipBitsSq = 1.0 / (n * n);
    private static final double lowerBoundAb = 1.0 / n;
    private static final double lowerBoundAbSq = 1.0 / (n * n);

    public static final Map<String, AlgoFactory> methods = new HashMap<>();
    public static final Map<String, Double> methodLowerBound = new HashMap<>();

    public enum Algos {
        TWORATE("TwoRate", false, 2.0),
        TWORATEEXP("TwoRateExp", false, 2.0),

        TWORATENOSHIFT("TwoRateNoShift", false, 2.0),
        TWORATENOSHIFTFC("TwoRateNoShiftFitnessCount", false, 2.0),
        TWORATENEW("TwoRateNew", false, 2.0),
        TWORATESTAGDETECT("TwoRateStagDetect", false, 2.0),
        ADAPTIVETWORATE("AdaptiveTwoRate", false, 2.0),
        ADAPTIVEDIVTWORATE("AdaptiveDivTwoRate", false, 2.0),
        AB("AB", false, 1.0),
        ABEXP("ABExp", false, 1.0),
        SIMPLE("SimpleEA", false, 1.0),
        HEAVYTAIL("HeavyTail", false, 2.0),
        TWORATEFB("TwoRateByFlipBits", false, 1.0);

        public String name;
        public boolean used;
        public double lowerBoundConst;
        Algos(String name, boolean used, double lowerBoundConst) {
            this.name = name;
            this.used = used;
            this.lowerBoundConst = lowerBoundConst;
        }

        public static Stream<Algos> stream() {
            return Stream.of(Algos.values());
        }
//Algos.stream().filter(d-> d.used.equals(false));
    }

    public enum Problems {
        OM("OM", true),
        LO("LO", false),
        NEUTRAL("Neutral", false),
        RUG("Rug", false),
        PLATEAU("Plateau", false);

        public String name;
        public boolean used;
        Problems(String name, boolean used) {
            this.name = name;
            this.used = used;
        }
        public static Stream<Problems> stream() {
            return Stream.of(Problems.values());
        }
    }


    static {
        //Two Rate No Shift Doerr
        methods.put("TwoRateNoShiftFitnessCountOM", (lambda, lowerBound, problemLength) -> new TwoRateNoShiftFitnessCount(2.0, lowerBound, lambda, new OneMax(problemLength)));

        //TwoRate
        methods.put("TwoRateOM", (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateLO", (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateNeutral", (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("TwoRateRug" + r, (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRatePlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRate(2.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Two Rate Exp
        methods.put("TwoRateExpOM", (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateExpLO", (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateExpNeutral",(lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("TwoRateExpRug" + r, (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRateExpPlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRateToExplore(2.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Two Rate NoShift
        methods.put("TwoRateNoShiftOM", (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateNoShiftLO", (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateNoShiftNeutral", (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("TwoRateNoShiftRug" + r, (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRateNoShiftPlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRateNoShift(2.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Two Rate New (Stagnation experiments1)
        methods.put("TwoRateNewOM", (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateNewLO", (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateNewNeutral", (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("TwoRateNewRug" + r, (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRateNewPlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRateNew(2.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Two Rate Stagnation Detection (ver.2)
        methods.put("TwoRateStagDetectOM", (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateStagDetectLO", (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateStagDetectNeutral", (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Neutral3(problemLength)));

        for (int r : rugs) {
            methods.put("TwoRateStagDetectRug" + r, (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRateStagDetectPlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRateStagnationDetection(2.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Two Rate Adaptive
        methods.put("AdaptiveTwoRateOM", (lambda, lowerBound, problemLength) -> new AdaptiveTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("AdaptiveDivTwoRateOM", (lambda, lowerBound, problemLength) -> new AdaptiveDivTwoRate(2.0, lowerBound, lambda, new OneMax(problemLength)));

        //AB
        methods.put("ABExpOM", (lambda, lowerBound, problemLength) -> new ABalgoToExplore(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength)));

        methods.put("ABOM", (lambda, lowerBound, problemLength) -> new ABalgo(1.0 / n, 2, 0.5, lowerBound, lambda, new OneMax(problemLength)));

        //Simple
        methods.put("SimpleEAOM", (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new OneMax(problemLength)));
        methods.put("SimpleEALO", (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("SimpleEANeutral", (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("SimpleEARug" + r, (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("SimpleEAPlateau" + k, (lambda, lowerBound, problemLength) -> new SimpleEA(1.0, lowerBound, lambda, new Plateau(problemLength, k)));
        }

        //Heavy Tail
        methods.put("HeavyTailOM", (lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new OneMax(problemLength)));
        methods.put("HeavyTailLO", (lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new LeadingOnes(problemLength)));
        methods.put("HeavyTailNeutral", (lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("HeavyTailRug" + r, (lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("HeavyTailPlateau" + k, (lambda, beta, problemLength) -> new HeavyTailAlgo(beta, lambda, new Plateau(problemLength, k)));
        }

        //TwoRateByFlipBits
        methods.put("TwoRateByFlipBitsOM", (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new OneMax(problemLength)));
        methods.put("TwoRateByFlipBitsLO", (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new LeadingOnes(problemLength)));
        methods.put("TwoRateByFlipBitsNeutral", (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new Neutral3(problemLength)));
        for (int r : rugs) {
            methods.put("TwoRateByFlipBitsRug" + r, (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new Ruggedness(problemLength, r)));
        }
        for (int k : plateaus) {
            methods.put("TwoRateByFlipBitsPlateau" + k, (lambda, lowerBound, problemLength) -> new TwoRateByFlipBits(lowerBound, lambda, new Plateau(problemLength, k)));
        }
    }
}
