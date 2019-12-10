package problem;

import java.util.List;
import java.util.Random;

public class OneMaxNeutral3 implements Problem {
    private boolean[] individual;
    private int fitness;
    private boolean[] chunks;
    private int[] chunksCount;
    private int tail;

    public OneMaxNeutral3(int n) {
        individual = new boolean[n];
        Random rand = new Random();
        tail = n % 3;
        chunks = new boolean[n / 3 + tail == 0 ? 0 : 1];
        chunksCount = new int[n / 3 + tail == 0 ? 0 : 1];
        fitness = 0;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
        }

        int count = 0;
        int curChunk = 0;
        for (int i = 0; i < n; i += 3) {
            for (int j = 0; j < 3; ++j) {
                if (individual[i + j]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            if (count >= 2) {
                chunks[curChunk] = true;
                fitness++;
            }
            curChunk++;
            count = 0;
        }
        if (n % 3 != 0) {
            for (int i = (n / 3) * 3; i < n; ++i) {
                if (individual[i]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            if (count >= tail / 2) {
                chunks[curChunk] = true;
                fitness++;
            }
        }
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = fitness;
        int curChunk = -1;
        int curChunkCount = -1;
        for (Integer i : patch) {
            if (curChunk == i / 3) {
                if (individual[i]) {
                    curChunkCount--;
                } else {
                    curChunkCount++;
                }
            } else {
                if (curChunk >= 0) {
                    if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) {
                        if (chunks[curChunk]) {
                            newFitness--;
                        }
                    } else {
                        if (!chunks[curChunk]) {
                            newFitness++;
                        }
                    }
                }

                curChunk = i / 3;
                curChunkCount = chunksCount[curChunk];
                if (individual[i]) {
                    curChunkCount--;
                } else {
                    curChunkCount++;
                }
            }
        }
        return newFitness;
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        for (Integer i : patch) {
            individual[i] = !individual[i];
        }
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public int getLength() {
        return individual.length;
    }

    @Override
    public boolean isOptimized() {
        return fitness == chunks.length;
    }
}
