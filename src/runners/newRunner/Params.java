package runners.newRunner;

import algo.*;
import problem.*;
import reinforcement.state.BetterCountState;
import reinforcement.reward.DivReward;
import reinforcement.agent.GreedyQAgent;
import reinforcement.HQEA;
import runners.newRunner.parameterSets.ABLearningParameterSet;
import runners.newRunner.parameterSets.BoundedParameterSet;
import runners.newRunner.parameterSets.HeavyTailParameterSet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Params {
    public static final int n = 10000;
    public static final int[] lambdas = {2, 4, 8, 16, 32, 64, 128, 256};//, 512, 1024, 2048, 4096};
    public static final int[] lambdaPoints = {2, 4};
    public static final int runCount = 100;
    public static final int[] rugs = {2, 5};
    public static final int[] plateaus = {2};
    public static final int budget = 1000;
    public static final double beta = 2.5;
    public static final double[] a = {2};
    public static final double[] b = {0.5};
    public static final double[] alphas = {0.8};
    public static final double[] gammas = {0.2};
    public static final double[] epsilon = {0.0};
    public static final boolean[] strict = {true, false};


    public static final Map<String, AlgoFactory<?>> methods = new HashMap<>();
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
        AB("AB", true, 1.0),
        ABEXP("ABExp", false, 1.0),
        SIMPLE("SimpleEA", false, 1.0),
        HEAVYTAIL("HeavyTail", false, 2.0),
        TWORATEFB("TwoRateByFlipBits", false, 1.0),
        HQEA("HQEA", false, 1.0);

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
        methods.put("TwoRateNoShiftFitnessCountOM", ((BoundedParameterSet bs) -> new TwoRateNoShiftFitnessCount(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));

        //TwoRate
        methods.put("TwoRateOM", ((BoundedParameterSet bs) -> new TwoRate(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateLO", ((BoundedParameterSet bs) -> new TwoRate(2.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateNeutral", ((BoundedParameterSet bs) -> new TwoRate(2.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("TwoRateRug" + r, ((BoundedParameterSet bs) -> new TwoRate(2.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRatePlateau" + k, ((BoundedParameterSet bs) -> new TwoRate(2.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Two Rate Exp
        methods.put("TwoRateExpOM", ((BoundedParameterSet bs) -> new TwoRateToExplore(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateExpLO", ((BoundedParameterSet bs) -> new TwoRateToExplore(2.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateExpNeutral",((BoundedParameterSet bs) -> new TwoRateToExplore(2.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("TwoRateExpRug" + r, ((BoundedParameterSet bs) -> new TwoRateToExplore(2.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRateExpPlateau" + k, ((BoundedParameterSet bs) -> new TwoRateToExplore(2.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Two Rate NoShift
        methods.put("TwoRateNoShiftOM", ((BoundedParameterSet bs) -> new TwoRateNoShift(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateNoShiftLO", ((BoundedParameterSet bs) -> new TwoRateNoShift(2.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateNoShiftNeutral", ((BoundedParameterSet bs) -> new TwoRateNoShift(2.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("TwoRateNoShiftRug" + r, ((BoundedParameterSet bs) -> new TwoRateNoShift(2.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRateNoShiftPlateau" + k, ((BoundedParameterSet bs) -> new TwoRateNoShift(2.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Two Rate New (Stagnation experiments1)
        methods.put("TwoRateNewOM", ((BoundedParameterSet bs) -> new TwoRateNew(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateNewLO", ((BoundedParameterSet bs) -> new TwoRateNew(2.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateNewNeutral", ((BoundedParameterSet bs) -> new TwoRateNew(2.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("TwoRateNewRug" + r, ((BoundedParameterSet bs) -> new TwoRateNew(2.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRateNewPlateau" + k, ((BoundedParameterSet bs) -> new TwoRateNew(2.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Two Rate Stagnation Detection (ver.2)
        methods.put("TwoRateStagDetectOM", ((BoundedParameterSet bs) -> new TwoRateStagnationDetection(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateStagDetectLO", ((BoundedParameterSet bs) -> new TwoRateStagnationDetection(2.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateStagDetectNeutral", ((BoundedParameterSet bs) -> new TwoRateStagnationDetection(2.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));

        for (int r : rugs) {
            methods.put("TwoRateStagDetectRug" + r, ((BoundedParameterSet bs) -> new TwoRateStagnationDetection(2.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRateStagDetectPlateau" + k, ((BoundedParameterSet bs) -> new TwoRateStagnationDetection(2.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Two Rate Adaptive
        methods.put("AdaptiveTwoRateOM", ((BoundedParameterSet bs) -> new AdaptiveTwoRate(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("AdaptiveDivTwoRateOM", ((BoundedParameterSet bs) -> new AdaptiveDivTwoRate(2.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));

        //AB
        methods.put("ABExpOM", ((BoundedParameterSet bs) -> new ABalgoToExplore(1.0 / n, 2, 0.5, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));

        methods.put("ABOM", ((BoundedParameterSet bs) -> new ABalgo(1.0 / n, 2, 0.5, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));

        //Simple
        methods.put("SimpleEAOM", ((BoundedParameterSet bs) -> new SimpleEA(1.0, bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("SimpleEALO", ((BoundedParameterSet bs) -> new SimpleEA(1.0, bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("SimpleEANeutral", ((BoundedParameterSet bs) -> new SimpleEA(1.0, bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("SimpleEARug" + r, ((BoundedParameterSet bs) -> new SimpleEA(1.0, bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("SimpleEAPlateau" + k, ((BoundedParameterSet bs) -> new SimpleEA(1.0, bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //Heavy Tail
        methods.put("HeavyTailOM", ((HeavyTailParameterSet hs) -> new HeavyTailAlgo(beta, hs.lambda, new OneMax(hs.problemLength))));
        methods.put("HeavyTailLO", ((HeavyTailParameterSet hs) -> new HeavyTailAlgo(beta, hs.lambda, new LeadingOnes(hs.problemLength))));
        methods.put("HeavyTailNeutral", ((HeavyTailParameterSet hs) -> new HeavyTailAlgo(beta, hs.lambda, new Neutral3(hs.problemLength))));
        for (int r : rugs) {
            methods.put("HeavyTailRug" + r, ((HeavyTailParameterSet hs) -> new HeavyTailAlgo(beta, hs.lambda, new Ruggedness(hs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("HeavyTailPlateau" + k, ((HeavyTailParameterSet hs) -> new HeavyTailAlgo(beta, hs.lambda, new Plateau(hs.problemLength, k))));
        }

        //TwoRateByFlipBits
        methods.put("TwoRateByFlipBitsOM", ((BoundedParameterSet bs) -> new TwoRateByFlipBits(bs.lowerBound, bs.lambda, new OneMax(bs.problemLength))));
        methods.put("TwoRateByFlipBitsLO", ((BoundedParameterSet bs) -> new TwoRateByFlipBits(bs.lowerBound, bs.lambda, new LeadingOnes(bs.problemLength))));
        methods.put("TwoRateByFlipBitsNeutral", ((BoundedParameterSet bs) -> new TwoRateByFlipBits(bs.lowerBound, bs.lambda, new Neutral3(bs.problemLength))));
        for (int r : rugs) {
            methods.put("TwoRateByFlipBitsRug" + r, ((BoundedParameterSet bs) -> new TwoRateByFlipBits(bs.lowerBound, bs.lambda, new Ruggedness(bs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("TwoRateByFlipBitsPlateau" + k, ((BoundedParameterSet bs) -> new TwoRateByFlipBits(bs.lowerBound, bs.lambda, new Plateau(bs.problemLength, k))));
        }

        //HQEA
        methods.put("HQEAOM", ((ABLearningParameterSet ls) -> new HQEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new OneMax(ls.problemLength), new DivReward(), new BetterCountState(), new GreedyQAgent(2, ls.alpha, ls.gamma, ls.epsilon))));

    }
}
