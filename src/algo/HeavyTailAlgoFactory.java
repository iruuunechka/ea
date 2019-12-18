package algo;

public interface HeavyTailAlgoFactory extends AlgoFactory{
    @Override
    Algorithm getInstance (int lambda, double beta, int problemLength);
}
