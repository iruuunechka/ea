import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataToMultiplot {
    private static TreeMap<Integer, List<Integer>> readDataToMap (String file) throws FileNotFoundException {
        TreeMap<Integer, List<Integer>> data = new TreeMap<>();
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
     * @param files files with the following format: value, lambda
     * @param out
     * @throws IOException
     */
    private static void createPlotFormat(Stream<Path> files, String out, int pos) throws FileNotFoundException {
        //List<Map<Integer, List<Integer>>> dataForFiles = new ArrayList<>();
        PrintWriter pw = new PrintWriter(out);
        pw.println("alpha, gamma, epsilon, a, b, lambda, med, q1, q2, top, bottom, average, dev");
        files.forEach(x -> {
            TreeMap<Integer, List<Integer>> data = null;
            double alpha = 0;
            double gamma = 0;
            double epsilon = 0;
            double a = 0;
            double b = 0;
            try {
                String filename = String.valueOf(x.subpath(1, x.getNameCount()));
                alpha = Double.valueOf(filename.substring(pos, pos + 3));
                gamma = Double.valueOf(filename.substring(pos + 4, pos + 7));
                epsilon = Double.valueOf(filename.substring(pos + 8, pos + 11));
                a = Double.valueOf(filename.substring(pos + 12, pos + 15));
                b = Double.valueOf(filename.substring(pos + 16, pos + 19));
                data = readDataToMap(String.valueOf(x));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int lambda : data.keySet()) {
                List<Integer> curData = data.get(lambda);
                Collections.sort(curData);
                int size = curData.size();
                int q1 = curData.get(size / 4);
                int med = curData.get(size / 2);
                int q2 = curData.get(3 * size / 4);
                double average = 0;
                double dev = 0;
                for (int gen : curData) {
                    average += (1.0 * gen) / curData.size();
                }
                for (int gen : curData) {
                    dev += (gen - average) * (gen - average) / curData.size();
                }
                dev = Math.sqrt(dev);
                pw.println(alpha + ", " + gamma + ", " + epsilon + ", " + a + ", " + b + ", " + lambda + ", " + med + ", " + q1 + ", " + q2 + ", " + (q2 - med) + ", " + (med - q1) + ", " + Math.round(average) + ", " + Math.round(dev));
            }
        });
        pw.close();
    }


    public static void main(String[] args) throws IOException {
        String outFolder = args[0] + "_plots/";
        new File(outFolder).mkdir();
        createPlotFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> String.valueOf(x.subpath(1, x.getNameCount())).contains("strict") && String.valueOf(x.subpath(1, x.getNameCount())).contains("sq")), outFolder + args[1] + "strictsq.csv", 7);
        createPlotFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> String.valueOf(x.subpath(1, x.getNameCount())).contains("strict") && !String.valueOf(x.subpath(1, x.getNameCount())).contains("sq")), outFolder + args[1] + "strict.csv", 7);
        createPlotFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> !String.valueOf(x.subpath(1, x.getNameCount())).contains("strict") && String.valueOf(x.subpath(1, x.getNameCount())).contains("sq")), outFolder + args[1] + "sq.csv", 7);
        createPlotFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> !String.valueOf(x.subpath(1, x.getNameCount())).contains("strict") && !String.valueOf(x.subpath(1, x.getNameCount())).contains("sq")), outFolder + args[1] + ".csv", 7);
    }
}
