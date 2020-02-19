package algo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataToPlotFormat {
    private static Map<Integer, List<Integer>> readDataToMap (String file) throws FileNotFoundException {
        Map<Integer, List<Integer>> data = new HashMap<>();
        Scanner sc = new Scanner(new File(file)).useDelimiter("\\D");
        sc.nextLine();
        while (sc.hasNextInt()) {
            int value = sc.nextInt();
            sc.next();
            int lambda = sc.nextInt();
            if (!data.containsKey(lambda)) {
                data.put(lambda, new ArrayList<>());
            }
            data.get(lambda).add(value);
        }
        return data;
    }

    /**
     * Creates file with median, 0.25 and 0.75 quartile and plot vals: 0.75 - median and median - 0.25 (lambda, med, q1, q2, top, bottom)
     * @param file file with the following format: value, lambda
     * @param out
     * @throws IOException
     */
    private static void createPlotFormat(String file, String out) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(out);
        pw.println("lambda, med, q1, q2, top, bottom");
        //List<Map<Integer, List<Integer>>> dataForFiles = new ArrayList<>();
        Map<Integer, List<Integer>> data = readDataToMap(file);
        for (int lambda : data.keySet()) {
            List<Integer> curData = data.get(lambda);
            Collections.sort(curData);
            int size = curData.size();
            int q1 = curData.get(size / 4);
            int med = curData.get(size / 2);
            int q2 = curData.get(3 * size / 4);
            pw.println(lambda + ", " + med + ", " + q1 + ", " + q2 + ", " + (q2 - med) + ", " + (med - q1));
        }
        pw.close();
    }


    public static void main(String[] args) throws IOException {
        Stream<Path> files = Files.walk(Paths.get(args[0])).filter(Files::isRegularFile);
        new File(args[0] + "_plots/").mkdir();
        files.forEach(x -> {
            try {
                System.out.println((x.subpath(1, x.getNameCount() - 1)));
                System.out.println(Paths.get(args[0] + "_plots/").resolve(x.subpath(1, x.getNameCount() - 1)));
                Files.createDirectories(Paths.get(args[0] + "_plots/").resolve(x.subpath(1, x.getNameCount() - 1)));
                createPlotFormat(String.valueOf(x),  args[0] + "_plots/" + String.valueOf(x.subpath(1, x.getNameCount())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
