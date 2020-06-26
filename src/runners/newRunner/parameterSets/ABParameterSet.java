package runners.newRunner.parameterSets;

public class ABParameterSet extends BoundedParameterSet {
    public double a;
    public double b;
    public boolean strict;

    public ABParameterSet(int lambda, double lowerBound, int problemLength, double a, double b, boolean strict) {
        super(lambda, lowerBound, problemLength);
        this.a = a;
        this.b = b;
        this.strict = strict;
    }
}
