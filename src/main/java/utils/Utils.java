package utils;

import models.ResultModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * utils
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class Utils {

    private static class QueueImpl {
        private static List<String> subjects = null;
    }

    public static String[] getUniversityInfo(String title) {
        String[] info = title.split("-");
        return Arrays.stream(info)
                .map(e -> e.substring(1))
                .collect(Collectors.toList())
                .subList(0, 3)
                .toArray(new String[0]);
    }

    public static String getIdDiscByName(String nameD) {
        int res = -2;
        switch (nameD) {
            case "математика": {
                res = 1;
                break;
            }
            case "русский язык": {
                res = 10;
                break;
            }
            case "физика": {
                res = 11;
                break;
            }
            case "химия": {
                res = 12;
                break;
            }
            case "биология": {
                res = 3;
                break;
            }
            case "география": {
                res = 4;
                break;
            }
            case "иностранный язык": {
                res = 5;
                break;
            }
            case "английский язык": {
                res = 5;
                break;
            }
            case "информатика и икт": {
                res = 6;
                break;
            }
            case "история": {
                res = 7;
                break;
            }
            case "литература": {
                res = 8;
                break;
            }
            case "обществознание": {
                res = 9;
                break;
            }
        }

        return String.valueOf(res);
    }

    public static String getParsingDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static List<String> getSubjectList() {
        return QueueImpl.subjects;
    }

    public static boolean fillSubjects(List<String> subjects, boolean isHeader, Path file) {
        if (QueueImpl.subjects == null) {
            if (!isHeader) {
                System.out.println("Пожалуйста, введите предметы руками. Распарсить файл <" + file.getFileName()
                        + "> не удалось. (Ожидается количество предметов в 1 строке, в остальных строках сами предметы)");
                QueueImpl.subjects = readSubjectsFromUser();
            } else {
                QueueImpl.subjects = subjects;
            }
        } else {
            if (subjects != null)
                QueueImpl.subjects = subjects;
        }

        return !(QueueImpl.subjects == null);
    }

    private static List<String> readSubjectsFromUser() {
        List<String> subjects = new ArrayList<>();
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));

        try {
            int number = Integer.parseInt(buf.readLine());
            for (int i = 0; i < number; i++) {
                subjects.add(buf.readLine().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при вводе предметов: " + e.getMessage());
        }

        return subjects;
    }
}
