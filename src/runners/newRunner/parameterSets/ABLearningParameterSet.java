package runners.newRunner.parameterSets;

public class ABLearningParameterSet extends ABParameterSet {
    public double alpha;
    public double gamma;
    public double epsilon;

    public ABLearningParameterSet(int lambda, double lowerBound, int problemLength, double a, double b, boolean strict, double alpha, double gamma, double epsilon) {
        super(lambda, lowerBound, problemLength, a, b, strict);
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
    }
}
