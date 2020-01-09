package parserPack.PFR;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class Helper {

    private List<Path> files;
    private List<Map<String, Double>> parsedContent;
    private String diff;
    private int needToSkip;
    private List<Double> generalSumList;
    private static final Double eps = 0.00001;

    DecimalFormat df = new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.US));

    public Helper(List<Path> files, int needToSkip) {
        this.files = files;
        this.needToSkip = needToSkip;
        parsedContent = new ArrayList<>();
        generalSumList = new ArrayList<>();
    }

    public String parse() {
        for (Path filePath: files) {
            parse(filePath);
        }

        compareResults();
        return this.diff;
    }

    private void parse(Path filePath) {
        Map<String, Double> result = new HashMap<>();
        InputStream in;
        Workbook wb = null;
        try {
            in = new FileInputStream(filePath.toFile());
            switch (filePath.toString().substring(filePath.toString().lastIndexOf(".") + 1)) {
                case "xlsx":
                    wb = new XSSFWorkbook(in);
                    break;
                case "xls":
                    wb = new HSSFWorkbook(in);
                    break;
                default:
                    System.err.println("No XLS file chosen: " + filePath.toString());
                    return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);

        for (int i = needToSkip; i < sheet.getPhysicalNumberOfRows(); ++i) {
            double sumValue = 0;
            String fullName = "";

            if (sheet.getRow(i) == null)
                continue;

            for (Cell cell : sheet.getRow(i)) {
                int cellType = cell.getCellType();
                switch (cellType) {
                    case Cell.CELL_TYPE_STRING:
                        fullName = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        sumValue = cell.getNumericCellValue();
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        sumValue = cell.getCachedFormulaResultType();
                        break;
                    default:
                        break;
                }
            }

            fullName = fullName.toLowerCase();
            if (!fullName.matches("[а-я]+ [а-я]+ [а-я]+")) {
                continue;
            }

            if (result.containsKey(fullName))
                result.put(fullName, sumValue + result.get(fullName));
            else
                result.put(fullName, sumValue);
        }

        final double[] generalSum = {0};
        sheet.getRow(sheet.getPhysicalNumberOfRows() - 1).forEach(e -> {
            if (e.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                generalSum[0] = e.getNumericCellValue();
                return;
            }

            if (e.getCellType() == Cell.CELL_TYPE_FORMULA) {
                generalSum[0] = e.getCachedFormulaResultType();
            }
        });

        parsedContent.add(result);
        generalSumList.add(generalSum[0]);
    }

    private void compareResults() {
        StringBuilder result = new StringBuilder();

        if (parsedContent.isEmpty()) {
            result.append("Nothing to compare");
            return;
        }

        result
                .append("Below you can find all pairs <FULLNAME, SUMMA>, they have difference in lists")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (int i = 0; i < files.size(); ++i) {
            result.append("list").append(i + 1).append(" = ").append(files.get(i).getFileName()).append(System.lineSeparator());
        }
        result.append(System.lineSeparator());

        List<Map.Entry<String, Boolean>> diff1 = parsedContent.get(0).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> parsedContent.stream()
                                .map(i -> compare(e.getValue(), i.get(e.getKey())))
                                .reduce(true, (a, b) -> a && b)))
                .entrySet().stream()
                .filter(e -> !e.getValue())
                .collect(Collectors.toList());

        List<Map.Entry<String, Boolean>> diff2 = parsedContent.get(1).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> parsedContent.stream()
                                .map(i -> compare(e.getValue(), i.get(e.getKey())))
                                .reduce(true, (a, b) -> a && b)))
                .entrySet().stream()
                .filter(e -> !e.getValue())
                .collect(Collectors.toList());

        diff1.addAll(diff2);
        diff1 = diff1.stream().distinct().collect(Collectors.toList());

        for (Map.Entry<String, Boolean> pair: diff1) {
            result.append(pair.getKey()).append(" = ");
            for (int i = 0; i < parsedContent.size(); i++) {
                result
                        .append("list")
                        .append(i + 1)
                        .append(": ")
                        .append(parsedContent.get(i).get(pair.getKey()) == null? "absent in this list" : parsedContent.get(i).get(pair.getKey()))
                        .append(" ||| ");
            }
            result.append(System.lineSeparator());
        }
        result
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("GENERAL SUM: ")
                .append(System.lineSeparator());
        for (int i = 0; i < generalSumList.size(); i++) {
            result
                    .append("list")
                    .append(i + 1)
                    .append(": ")
                    .append(df.format(generalSumList.get(i)))
                    .append(" ||| ");
        }
        result
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("DIFFERENCE SUM: ")
                .append(System.lineSeparator())
                .append(df.format(generalSumList.get(0) - generalSumList.get(1)));

        this.diff = result.toString();
    }

    private boolean compare(Double lhs, Double rhs) {
        if (rhs == null && lhs - eps < 0 && lhs + eps > 0)
            return true;

        if (lhs == null || rhs == null)
            return false;

        return lhs - eps < rhs && lhs + eps > rhs;
    }

}
