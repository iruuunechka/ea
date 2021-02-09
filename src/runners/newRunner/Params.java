package runners.newRunner;

import algo.*;
import problem.*;
import reinforcement.HQEA;
import reinforcement.QEA;
import reinforcement.agent.GreedyQAgent2actions;
import reinforcement.reward.DivReward;
import reinforcement.state.BetterCountState;
import reinforcement.state.PairState;
import runners.newRunner.parameterSets.ABLearningParameterSet;
import runners.newRunner.parameterSets.ABParameterSet;
import runners.newRunner.parameterSets.BoundedParameterSet;
import runners.newRunner.parameterSets.HeavyTailParameterSet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static runners.newRunner.RunAlgorithm.*;

public class Params {
    /**
     * Ann's params
     * n = 10000;
     * a = 2;
     * b = 0.5;
     * alpha = 0.8
     * gamma = 0.2
     * epsilon = 0.0
     */
    /**
     * HQEA Gradient params
     * n = 5000;
     * runcount = 3
     * a = 2;
     * b = 0.5;
     * alpha = 0.9
     * gamma = 0.1
     * epsilon = 0.0
     */
    public static final int n = 100;
    public static final int[] lambdas = {2, 4, 8, 16, 32, 64, 128, 256, 512};//, 1024, 2048, 4096}; //{16, 512};//
    public static final int[] lambdaPoints = {512};// {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144};
    public static final int runCount = 100;
    public static final int[] rugs = {2};
    public static final int[] plateaus = {2};
    public static final int budget = 1000;
    public static final double beta = 2.5;
    public static final double[] a = {2}; //{1.3};//{1.7};//{1.5};//
    public static final double[] b = {0.5};//{0.94};//{0.88};//{0.9};//
    public static final double[] alphas = {0.8};//, 0.6, 0.4};//{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};////{0.1, 0.2, 0.3, 0.5, 0.7, 0.9};//
    public static final double[] gammas = {0.2};//, 0.4, 0.6};////{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};//{0.1, 0.3, 0.5, 0.7, 0.8, 0.9};//
    public static final double[] epsilon = {0.0};//, 0.1, 0.3};//{0.0};//{0.0, 0.01, 0.1, 0.3};//
    public static final boolean[] strict = {true, false};


    public static final Map<String, AlgoFactory<?>> methods = new HashMap<>();
    public static final Map<String, Double> methodLowerBound = new HashMap<>();

    public enum Algos {
        TWORATE("TwoRate", true, 2.0, runByOptimum()),//runByOptimumForStatisticsByIter()), //runOnPointGradientPlot()),//
        TWORATEEXP("TwoRateExp", false, 2.0, runByOptimum()),

        TWORATENOSHIFT("TwoRateNoShift", false, 2.0, runByOptimum()),
        TWORATENOSHIFTFC("TwoRateNoShiftFitnessCount", false, 2.0, runByOptimum()),
        TWORATENEW("TwoRateNew", false, 2.0, runByOptimum()),
        TWORATESTAGDETECT("TwoRateStagDetect", false, 2.0, runByOptimum()),
        ADAPTIVETWORATE("AdaptiveTwoRate", false, 2.0, runByOptimum()),
        ADAPTIVEDIVTWORATE("AdaptiveDivTwoRate", false, 2.0, runByOptimum()),
        AB("AB", true, 1.0, runABByOptimum()),//runABForStatisticsByIter()),//runABOnPointGradientPlot()),//
        ABEXP("ABExp", false, 1.0, runABByOptimum()),
        SIMPLE("SimpleEA", true, 1.0, runByOptimum()),
        HEAVYTAIL("HeavyTail", false, 2.0, runHeavyTailAlgo()),
        TWORATEFB("TwoRateByFlipBits", false, 1.0, runByOptimum()),
        HQEA("HQEA", true, 1.0, runLearningByOptimum()),//runLearningForStatisticsByIter()),//runABLearningOnPointGradientPlot()),//runLearningForStatistics()),//
        QEA("QEA", true, 1.0, runLearningByOptimum());//runABLearningOnPointGradientPlot());//

        public String name;
        public boolean used;
        public double lowerBoundConst;
        public Runner runner;
        Algos(String name, boolean used, double lowerBoundConst, Runner runner) {
            this.name = name;
            this.used = used;
            this.lowerBoundConst = lowerBoundConst;
            this.runner = runner;
        }

        public static Stream<Algos> stream() {
            return Stream.of(Algos.values());
        }
//Algos.stream().filter(d-> d.used.equals(false));
    }

    public enum Problems {
        OM("OM", false),
        LO("LO", false),
        NEUTRAL("Neutral", false),
        RUG("Rug", false),
        PLATEAU("Plateau", true);

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
        methods.put("ABExpOM", ((ABParameterSet abs) -> new ABalgoToExplore(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, new OneMax(abs.problemLength))));

        methods.put("ABOM", ((ABParameterSet abs) -> new ABalgo(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, abs.strict, new OneMax(abs.problemLength))));
        methods.put("ABLO", ((ABParameterSet abs) -> new ABalgo(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, abs.strict, new LeadingOnes(abs.problemLength))));
        methods.put("ABNeutral", ((ABParameterSet abs) -> new ABalgo(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, abs.strict, new Neutral3(abs.problemLength))));
        for (int r : rugs) {
            methods.put("ABRug" + r, ((ABParameterSet abs) -> new ABalgo(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, abs.strict, new Ruggedness(abs.problemLength, r))));
        }
        for (int k : plateaus) {
            methods.put("ABPlateau" + k, ((ABParameterSet abs) -> new ABalgo(1.0 / n, abs.a, abs.b, abs.lowerBound, abs.lambda, abs.strict, new Plateau(abs.problemLength, k))));
        }

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
                new OneMax(ls.problemLength), new DivReward(), () -> new PairState(ls.lambda), new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        methods.put("HQEALO", ((ABLearningParameterSet ls) -> new HQEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new LeadingOnes(ls.problemLength), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        methods.put("HQEANeutral", ((ABLearningParameterSet ls) -> new HQEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new Neutral3(ls.problemLength), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        for (int r : rugs) {
            methods.put("HQEARug" + r, ((ABLearningParameterSet ls) -> new HQEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                    new Ruggedness(ls.problemLength, r), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        }
        for (int k : plateaus) {
            methods.put("HQEAPlateau" + k, ((ABLearningParameterSet ls) -> new HQEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                    new Plateau(ls.problemLength, k), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        }

        //QEA
        methods.put("QEAOM", ((ABLearningParameterSet ls) -> new QEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new OneMax(ls.problemLength), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        methods.put("QEALO", ((ABLearningParameterSet ls) -> new QEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new LeadingOnes(ls.problemLength), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        methods.put("QEANeutral", ((ABLearningParameterSet ls) -> new QEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                new Neutral3(ls.problemLength), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        for (int r : rugs) {
            methods.put("QEARug" + r, ((ABLearningParameterSet ls) -> new QEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                    new Ruggedness(ls.problemLength, r), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        }
        for (int k : plateaus) {
            methods.put("QEAPlateau" + k, ((ABLearningParameterSet ls) -> new QEA(1.0 / n, ls.a, ls.b, ls.strict, ls.lowerBound, ls.lambda,
                    new Plateau(ls.problemLength, k), new DivReward(), BetterCountState::new, new GreedyQAgent2actions(ls.alpha, ls.gamma, ls.epsilon))));
        }
    }
}
