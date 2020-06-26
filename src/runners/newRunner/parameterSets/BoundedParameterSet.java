package runners.newRunner.parameterSets;

public class BoundedParameterSet extends ParameterSet{
    public double lowerBound;


    public BoundedParameterSet(int lambda, double lowerBound, int problemLength) {
        super(lambda, problemLength);
        this.lowerBound = lowerBound;
    }


}
