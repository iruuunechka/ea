package runners.newRunner.parameterSets;

public class HeavyTailParameterSet extends ParameterSet {
    public double beta;
    public HeavyTailParameterSet(int lambda, int problemLength, double beta) {
        super(lambda, problemLength);
        this.beta = beta;
    }
}
