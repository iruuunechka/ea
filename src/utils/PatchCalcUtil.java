package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PatchCalcUtil {
    private static final Random rand = ThreadLocalRandom.current();
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

    public static List<Integer> createPatchNoShift (double mutation, int problemLength) {
        List<Integer> patch = new ArrayList<>(16);
        int i = getNextIndex(-1, mutation);
        while (i < problemLength) {
            patch.add(i);
            i = getNextIndex(i, mutation);
        }
        return patch;

    }

    public static int getNextIndex(int curIndex, double mutation) {
        // casting the entire result to int correctly processes int overflows.
        return (int) (curIndex + 1 + Math.log(rand.nextDouble()) / Math.log(1.0 - mutation));
    }
}
