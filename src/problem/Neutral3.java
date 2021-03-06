package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Neutral3 implements Problem {
    private boolean[] individual;
    private int fitness;
    private boolean[] chunks;
    private int[] chunksCount;
    private int[] chunksCountByOnesNumber;
    private int tail;

    public Neutral3(int n) {
        individual = new boolean[n];
        Random rand = ThreadLocalRandom.current();
        tail = n % 3;
        chunks = new boolean[n / 3 + (tail == 0 ? 0 : 1)];
        chunksCount = new int[n / 3 + (tail == 0 ? 0 : 1)];
        chunksCountByOnesNumber = new int[4];
        fitness = 0;
        for (int i = 0; i < n; ++i) {
            individual[i] = rand.nextBoolean();
        }

        int count = 0;
        int curChunk = 0;
        for (int i = 0; i < n - tail; i += 3) {
            for (int j = 0; j < 3; ++j) {
                if (individual[i + j]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            chunksCountByOnesNumber[count]++;
            if (count >= 2) {
                chunks[curChunk] = true;
                fitness++;
            }
            curChunk++;
            count = 0;
        }
        if (n % 3 != 0) {
            for (int i = n - tail; i < n; ++i) {
                if (individual[i]) {
                    count++;
                }
            }
            chunksCount[curChunk] = count;
            chunksCountByOnesNumber[count]++;
            if (count > tail / 2) {
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
            if (curChunk != i / 3) { // if a new item from the patch does not belong to the previous chunk
                //apply result for the previous chunk
                if (curChunk >= 0) { //not start
                    if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                        if (chunks[curChunk]) {
                            newFitness--;
                        }
                    } else {
                        if (!chunks[curChunk]) {
                            newFitness++;
                        }
                    }
                }
                //go to the next chunk
                curChunk = i / 3;
                curChunkCount = chunksCount[curChunk];
            }
            //count ones in the current chunk
            if (individual[i]) {
                curChunkCount--;
            } else {
                curChunkCount++;
            }
        }

        if (curChunk >= 0) { //apply last patch item
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

        return newFitness;
    }

    @Override
    public void applyPatch(List<Integer> patch, int fitness) {
        int curChunk = -1;
        int curChunkCount = -1;
        for (Integer i : patch) {
            if (curChunk != i / 3) { // if a new item from the patch does not belong to the previous chunk
                //apply result for the previous chunk
                if (curChunk >= 0) { //not start
                    if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                        chunks[curChunk] = false;
                    } else {
                        chunks[curChunk] = true;
                    }
                    chunksCountByOnesNumber[chunksCount[curChunk]]--;
                    chunksCount[curChunk] = curChunkCount;
                    chunksCountByOnesNumber[curChunkCount]++;
                }
                //go to the next chunk
                curChunk = i / 3;
                curChunkCount = chunksCount[curChunk];
            }
            //count ones in the current chunk
            if (individual[i]) {
                curChunkCount--;
            } else {
                curChunkCount++;
            }
            individual[i] = !individual[i];
        }

        if (curChunk >= 0) { //apply last patch item
            if (curChunkCount < (curChunk == chunks.length - 1 ? (tail > 0 ? tail : 2) : 2)) { //condition on the tail chunk
                chunks[curChunk] = false;
            } else {
                chunks[curChunk] = true;
            }
            chunksCountByOnesNumber[chunksCount[curChunk]]--;
            chunksCount[curChunk] = curChunkCount;
            chunksCountByOnesNumber[curChunkCount]++;
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

    @Override
    public String getInfo() {
        StringBuilder s = new StringBuilder();
        for (int i : chunksCountByOnesNumber) {
            s.append(", ").append(i);
        }
        return s.toString();
    }

    @Override
    public int getOptimum() {
        return chunks.length;
    }
}
