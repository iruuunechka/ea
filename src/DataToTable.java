import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataToTable {

    private static final Set<Integer> lambdas = new HashSet<>(Arrays.asList(2, 1024));
    private static ResultsType resultType = ResultsType.MEDIAN;

    private enum ResultsType {
        MEDIAN("med", 2, 0),
        AVERAGE("average", 1, 5);

        ResultsType(String name, int additionInfLength, int pos) {
            this.name = name;
            this.additionInfLength = additionInfLength;
            this.pos = pos;
        }

        private String name;
        private int additionInfLength;
        private int pos; //position after lambda column

    }
    private static void readDataToMap (Map<Integer, Set<DataItem>> data, String file, ResultsType res, int lambdaPos) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            line = line.replace(",", "");
            Scanner sc = new Scanner(line);
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < lambdaPos; ++i) {
                name.append(sc.nextDouble());
                if (i != lambdaPos - 1) {
                    name.append("_");
                }
            }
            if (name.toString().equals("")) {
                name.append(file.substring(file.lastIndexOf("/") + 1, file.length() - 4));
            }
            int lambda = sc.nextInt();
            for (int i = 0; i < res.pos; ++i) {
                sc.next();
            }
            int value = sc.nextInt();
            StringBuilder additionInf = new StringBuilder();
            for (int i = 0; i < res.additionInfLength; ++i) {
                additionInf.append(sc.nextInt());
                if (i != res.additionInfLength - 1) {
                    additionInf.append(", ");
                }
            }
            if (lambdas.contains (lambda)) {
                if (!data.containsKey(lambda)) {
                    data.put(lambda, new TreeSet<DataItem>() {
                    });
                }
                data.get(lambda).add(new DataItem(value, name.toString(), additionInf.toString()));
            }
        }
        br.close();
    }

    private static class DataItem implements Comparable<DataItem>{
        int val;
        String name;
        String additionInf;

        public DataItem(int val, String name, String additionInf) {
            this.val = val;
            this.name = name;
            this.additionInf = additionInf;
        }

        @Override
        public int compareTo(DataItem dataItem) {
            return Double.compare(val, dataItem.val);
        }
    }

    private static void createTableFormat(Stream<Path> files, String out, ResultsType res) throws FileNotFoundException {
        //List<Map<Integer, List<Integer>>> dataForFiles = new ArrayList<>();
        TreeMap<Integer, Set<DataItem>> data = new TreeMap<>();
        PrintWriter pw = new PrintWriter(out);
        if (res == ResultsType.MEDIAN) {
            pw.println("name, lambda, med, q1, q2");
        } else {
            pw.println("name, lambda, med, average, dev");
        }
        files.forEach(x -> {
            try {
                readDataToMap(data, String.valueOf(x), res, String.valueOf(x).contains("HQEA") ? 5 : 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        for (int lambda : data.keySet()) {
            for (DataItem curData : data.get(lambda)) {
                pw.println(curData.name + ", " + lambda + ", " + curData.val + ", " + curData.additionInf);
            }
        }
        pw.close();
    }


    public static void main(String[] args) throws IOException {
        Locale.setDefault(new Locale("en"));
        String outFolder = args[0] + "_out/";
        new File(outFolder).mkdir();
        createTableFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> (String.valueOf(x.subpath(1, x.getNameCount()))
                .contains("strict") || String.valueOf(x.subpath(1, x.getNameCount())).contains("tworate"))
                && String.valueOf(x.subpath(1, x.getNameCount())).contains("sq") || String.valueOf(x.subpath(1, x.getNameCount())).contains("simple")),  outFolder + "strictsq.csv", resultType);
        createTableFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> (String.valueOf(x.subpath(1, x.getNameCount()))
                .contains("strict") || String.valueOf(x.subpath(1, x.getNameCount())).contains("tworate"))
                && !String.valueOf(x.subpath(1, x.getNameCount())).contains("sq") || String.valueOf(x.subpath(1, x.getNameCount())).contains("simple")),  outFolder + "strict.csv", resultType);
        createTableFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> (!String.valueOf(x.subpath(1, x.getNameCount()))
                .contains("strict") || String.valueOf(x.subpath(1, x.getNameCount())).contains("tworate"))
                && String.valueOf(x.subpath(1, x.getNameCount())).contains("sq") || String.valueOf(x.subpath(1, x.getNameCount())).contains("simple")),  outFolder + "nonstrictsq.csv", resultType);
        createTableFormat(Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).filter(x -> (!String.valueOf(x.subpath(1, x.getNameCount()))
                .contains("strict") || String.valueOf(x.subpath(1, x.getNameCount())).contains("tworate"))
                && !String.valueOf(x.subpath(1, x.getNameCount())).contains("sq") || String.valueOf(x.subpath(1, x.getNameCount())).contains("simple")),  outFolder + "nonstrict.csv", resultType);
    }
}
