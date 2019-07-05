package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * utils
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class Constants {

    public static final String ATT_REGEX = "[А,а]ттестат";
    public static final List<List<String>> DOCUMENT_TYPE = Arrays.asList(
            Arrays.asList("да","нет"),
            Arrays.asList("оригинал","копия"),
            Arrays.asList("бви","особаяквота"));
    public static final String DOCUMENT = "оригинал";
    public static final String DIP_REGEX = "[Д,д]иплом";
    public static final String EMPTY_STRING = "";
    public static final String NUMERIC_REGEX = "\\d+";
    public static final String SHIFT_REGEX = "[\n]";
    public static final String SPACE_REGEX = "[\\p{Z}]";
    public static final String SPACES_REGEX = "[\\p{Z}]+";
    public static final String SPACE_SIGN = " ";
    public static final String SPLITTER = "[А-Я,Ё][а-я,ё]{1,20}[\\p{Z}][А-Я,Ё][а-я,ё]{1,20}[\\p{Z}][А-Я,Ё][а-я,ё]{1,20}";
    public static final Map<String, List<String>> SUBJECTS = Map.ofEntries(
            Map.entry("русский язык", Arrays.asList("рус","русс","русск","русски","русский","рус яз", "рус. язык")),
            Map.entry("математика", Arrays.asList("мат","мате","матем","матема","математ","математи","математик","математика")),
            Map.entry("биология", Arrays.asList("био","биол","биол","биоло","биолог","биологи","биология")),
            Map.entry("физика", Arrays.asList("физ","физи","физик","физика")),
            Map.entry("химия", Arrays.asList("хим","хими","химия")),
            Map.entry("география", Arrays.asList("гео","геог","геогр","геогра","географ","географи","география")),
            Map.entry("история", Arrays.asList("ист","исто","истор","истори","история")),
            Map.entry("литература", Arrays.asList("лит","лите","литер","литера","литерат","литерату","литератур","литература")),
            Map.entry("обществознание", Arrays.asList("общ","обще","общес","общест","обществ","общество","обществоз","обществозн","обществознание")),
            Map.entry("информатика и икт", Arrays.asList("инф","икт","инфо","инфор","информ","информа","информат","информати","информатик","информатика","информатика и икт")),
            Map.entry("английский язык", Arrays.asList("анг","англ","англи","англий","английс","английск","английски","английский","англ яз")),
            Map.entry("иностранный язык", Arrays.asList("ино","инос","иност","иностр","иностра","иностран","иностранн","иностранны","иностранный")));
    public static final String Z_CONSTANT = "математика русский язык литература обществознание";

}
