package problem;

import java.util.List;

public interface Problem {

    int calculatePatchFitness(List<Integer> patch);

    void applyPatch(List<Integer> patch, int fitness);

    int getFitness();

    int getLength();
}
