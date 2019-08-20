package algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final Random rand = new Random();
    public static List<Integer> createPatch (double mutation, int problemLength) {
        List<Integer> patch = new ArrayList<>(16);
        int i = getNextIndex(-1, mutation);
        while (i < problemLength) {
            patch.add(i);
            i = getNextIndex(i, mutation);
        }

        if (patch.isEmpty()) {
            patch.add(rand.nextInt(problemLength));
        }
        return patch;

    }

    private static int getNextIndex(int curIndex, double mutation) {
        return curIndex + 1 + (int) (Math.log(rand.nextDouble()) / Math.log(1.0 - mutation));
    }
}
