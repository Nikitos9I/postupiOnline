package mainPack;

import parserPack.PFR.Helper;
import readerPack.Reader;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

/**
 * mainPack
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class Main {

//    private static final String DIR = "/Users/nikita.savinov/Downloads/helper2/ежв 1с для программы.xlsx";
//    private static final String DIR = "/Users/nikita.savinov/Downloads/helper/кредит 1с для программы.xlsx";
    private static final String DIR = "/Users/nikita.savinov/Downloads/helper2";

    private void start() throws IOException {
        Reader reader = new Reader(DIR);
        PrintWriter writer = new PrintWriter("out.txt");
        List<Path> files = reader.getFiles();

//        int globalSize = files.size();
//        int counter = 0;

//        for (Path file : files) {
//            System.err.println("Сделано " + ((++counter * 1.0) / globalSize));
//            ParserPDF parserPDF = new ParserPDF(file);
//            List<ResultModel> resultModelList = parserPDF.parse();
//            for (ResultModel resultModel : resultModelList) {
//                writer.println(resultModel);
//            }
//        }
        Helper helper = new Helper(files, 0);
        writer.println(helper.parse());

        writer.close();
    }

    public static void main(String[] args) {
        try {
            new Main().start();
        } catch (IOException e) {
            System.err.println("Troubles with reading DIR: " + e.getMessage());
        }
    }

}
