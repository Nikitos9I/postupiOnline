package parserPack;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import models.ResultModel;
import utils.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.Constants.*;
import static utils.Utils.*;

/**
 * parserPack
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class ParserPDF {

    private Path file;

    public ParserPDF(Path file) {
        this.file = file;
    }

    private class Pair implements Comparable<Pair> {
        private String key;
        private Integer value;

        Pair(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public int compareTo(Pair o) {
            return this.value.compareTo(o.value);
        }
    }

    public List<ResultModel> parse() {
        List<ResultModel> resultModelList = new ArrayList<>();

        try {
            PdfReader reader = new PdfReader(file.toString());

            for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
                System.out.println("Страница № " + i);
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                String text = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                String[] dividedText = preparation(text)
                        .split("(?=" + SPLITTER + ")");

                Pattern pattern = Pattern.compile(SPLITTER);
                Matcher matcher;

                for (String item : dividedText) {
                    matcher = pattern.matcher(item);
                    if (matcher.find()) {
                        ResultModel resultModel = getInfo(item, new ResultModel());
                        fillDateAndUniversityInfo(resultModel);
                        item = Arrays.stream(item.split(SPACE_REGEX)).skip(3).collect(Collectors.joining(" "));
                        fillSubjects(getHeadersIfExist(item), false, file);

                        resultModel.setSubjectsName(getSubjectList());
                        resultModel.setPoints(resultModel.getPoints().stream()
                                .limit(resultModel.getSubjectsName().size())
                                .collect(Collectors.toList()));

                        if (resultModel.getPoints() != null && resultModel.getPoints().size() != resultModel.getSubjectsName().size()) {
                            resultModel.setSubjectsName(new ArrayList<>());
                            resultModel.setPoints(new ArrayList<>());
                        }

                        if (!resultModel.getSurname().equals("копия") && !resultModel.getSurname().equals("итого"))
                            resultModelList.add(resultModel);
                    } else {
                        fillSubjects(getHeadersIfExist(item), true, file);
                    }
                }
            }
        } catch (IOException | StringIndexOutOfBoundsException e) {
            System.err.println("Do not readable: " + e.getMessage());
        }

        fillSubjectsId(resultModelList);
        return resultModelList;
    }

    private ResultModel getInfo(String item, ResultModel resultModel) {
        String[] literals = item.split(SPACE_REGEX);
        resultModel.setSurname(literals[0]);
        resultModel.setName(literals[1]);
        resultModel.setPatronymic(literals[2]);

        Supplier<Stream<String>> literalsWithoutCreeds = () -> Arrays.asList(literals).subList(3, literals.length).stream();

        resultModel.setDocumentType("0");
        String result = "NPE";
        for (List<String> currentDocumentType : DOCUMENT_TYPE) {
            result = getDocumentType(literalsWithoutCreeds.get(), currentDocumentType, result);
        }

        switch (result) {
            case "бви":
                resultModel.setDocumentType("3");
                break;
            case "особаяквота":
                resultModel.setDocumentType("2");
                break;
            case "копия":
                resultModel.setDocumentType("0");
                break;
            case "оригинал":
                resultModel.setDocumentType("1");
                break;
            case "да":
                resultModel.setDocumentType("1");
                break;
            case "нет":
                resultModel.setDocumentType("0");
                break;
            default:
                resultModel.setDocumentType("0");
                break;
        }

        resultModel.setPoints(getPoints(literalsWithoutCreeds.get()));
        resultModel.setPointsSum(getPointsSum(literalsWithoutCreeds.get(),
                resultModel.getPoints().stream().mapToInt(Integer::parseInt).sum()));

        return resultModel;
    }

    private String getDocumentType(Stream<String> literals, List<String> docTypes, String result) {
        return literals
                .map(String::toLowerCase)
                .filter(docTypes::contains)
                .findFirst()
                .orElse(result);
    }

    private List<String> getPoints(Stream<String> literals) {
        return literals
                .filter(e -> e.matches(NUMERIC_REGEX))
                .mapToInt(Integer::parseInt)
                .filter(e -> e <= 100 && e > 20)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
    }

    private String getPointsSum(Stream<String> literals, int sumInt) {
        return String.valueOf(literals
                .filter(e -> e.matches(NUMERIC_REGEX))
                .mapToInt(Integer::parseInt)
                .filter(e -> e > 10 && e > sumInt && e < sumInt + 11)
                .max()
                .orElse(sumInt));
    }

    private boolean searchInList(List<String> subjects, String literal) {
//        return subjects.stream().anyMatch(literal::contains);
        String lastIn = "";
        for (String subject : subjects) {
            if (literal.contains(subject))
                lastIn = subject;
        }

        return !lastIn.equals("") && literal.length() == lastIn.length();
    }

    private List<String> getSubjects(Map<String, List<String>> map, String[] literals) {
        List<Pair> subjects = new ArrayList<>();
        for (Map.Entry<String, List<String>> mmap: map.entrySet()) {
            for (int i = 0; i < literals.length; ++i) {
                if (searchInList(mmap.getValue(), literals[i])) {
                    subjects.add(new Pair(mmap.getKey(), i));
                }
            }
        }

        Collections.sort(subjects);
        return subjects.stream().map(Pair::getKey).collect(Collectors.toList());
    }

    private List<String> getHeadersIfExist(String item) {
        String[] literals = item.split(SPACE_REGEX);
        literals = Arrays.stream(literals).map(String::toLowerCase).toArray(String[]::new);

        List<String> subjects = getSubjects(SUBJECTS, literals);
        subjects = subjects.stream().distinct().collect(Collectors.toList());

        return subjects.size() >= 2? subjects : null;
    }

    private void fillDateAndUniversityInfo(ResultModel resultModel) {
        String date = getParsingDate();
        String[] universityInfo = getUniversityInfo(file.getFileName().toString());
        resultModel.setParsingDate(date);
        resultModel.setFileName(Arrays.asList(universityInfo));
    }

    private void fillSubjectsId(List<ResultModel> resultModelList) {
        resultModelList.forEach(e -> {
            List<String> subjects = e.getSubjectsName();
            e.setSubjectsId(subjects.stream()
                    .map(Utils::getIdDiscByName)
                    .collect(Collectors.toList()));
        });
    }

    private String preparation(String item) {
        return item.replaceAll(SHIFT_REGEX, SPACE_SIGN)
            .replaceAll(DIP_REGEX, DOCUMENT)
            .replaceAll(ATT_REGEX, DOCUMENT)
            .replaceAll(SPACES_REGEX, " ")
            .replaceAll("Биолог", "биолог")
            .replaceAll("Литератур", "литератур")
            .replaceAll("Физик", "физик")
            .replaceAll("Хими", "хими")
            .replaceAll("Обществознан", "обществознан")
            .replaceAll("Истори", "истори")
            .replaceAll("Математи", "математи")
            .replaceAll("Нет", "нет")
            .replaceAll("Да ", "да ")
            .replaceAll("Льгота", "льгота")
            .replaceAll("На ", "на")
            .replaceAll("Имя", "имя")
            .replaceAll("Экз", "экз")
            .replaceAll("Налич", "налич")
            .replaceAll("Индив", "индив")
            .replaceAll("матика и вычисл", "нахуй")
            .replaceAll("Без вступительных испытаний", "бви")
            .replaceAll("[О,о]собая квота", "особаяквота")
            .replaceAll("[О,о]собое право", "особаяквота");
    }

}