import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataToRStats {

    private static Map<Integer, List<Integer>> readDataToMap (String file) throws FileNotFoundException {
        Map<Integer, List<Integer>> data = new HashMap<>();
        Scanner sc = new Scanner(new File(file)).useDelimiter("\\D");
        sc.nextLine();
        while (sc.hasNextInt()) {
//            String scn = sc.nextLine();
            int fitness = sc.nextInt();
            sc.next();
            int lambda = sc.nextInt();
            if (!data.containsKey(lambda)) {
                data.put(lambda, new ArrayList<>());
            }
            data.get(lambda).add(fitness);
        }
        return data;
    }

    /**
     * Compares two algorithms on the same lambda set.
     * @param files list of files with the following format: data, lambda
     * @param out
     * @throws IOException
     */
    private static void compareFitnessByLambda(Path[] files, String out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        //List<Map<Integer, List<Integer>>> dataForFiles = new ArrayList<>();
        Map<Integer, List<Integer>> data = null;
        List<String> fileNames = new ArrayList<>();
        for (Path file : files) {
            data = readDataToMap(String.valueOf(file));
            fileNames.add(file.subpath(file.getNameCount() - 1, file.getNameCount()).toString());
          //  dataForFiles.add(data);
            for (int lambda : data.keySet()) {

//                char algo = (char) ('a' + fileNumber);
//                pw.print(algo + "_" + lambda + " <- c(");
                pw.println(fileNames.get(fileNames.size() - 1) + "_" + lambda + " <- c(");

                for (int i = 0; i < data.get(lambda).size() - 1; ++i) {
                    pw.print(data.get(lambda).get(i) + ", ");
                }
                pw.println(data.get(lambda).get(data.get(lambda).size() - 1) + ")");
            }
//            pw.flush();

        }

        for (int lambda : data.keySet()) {
            for (int i = 0; i < fileNames.size(); ++i) {
                for (int j = i + 1; j < fileNames.size(); ++j) {
//                    char algo1 = (char) ('a' + i);
//                    char algo2 = (char) ('a' + j);
//                    pw.println("wilcox.test(" + algo1 + "_" + lambda + ", " + algo2 + "_" + lambda + ", paired = FALSE)");
                    pw.println("wilcox.test(" + fileNames.get(i) + "_" + lambda + ", " + fileNames.get(j) + "_" + lambda + ", paired = FALSE)");

                }
            }
        }
        pw.close();
    }
    public static void main(String[] args) throws IOException {
        Path[] files = Files.list(Paths.get(args[0])).filter(Files::isRegularFile).toArray(Path[]::new);
        compareFitnessByLambda(files, args[0] + args[1]);
    }
}
