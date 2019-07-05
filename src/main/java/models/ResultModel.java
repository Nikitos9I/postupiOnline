package models;

import java.util.List;

/**
 * models
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class ResultModel {

    private String parsingDate;
    private String surname;
    private String name;
    private String patronymic;
    private String pointsSum;
    private List<String> fileName;
    private List<String> subjectsId;
    private List<String> points;
    private List<String> subjectsName;
    private String documentType;

    public String getParsingDate() {
        return parsingDate;
    }

    public void setParsingDate(String parsingDate) {
        this.parsingDate = parsingDate;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic.toLowerCase();
    }

    public String getPointsSum() {
        return pointsSum;
    }

    public void setPointsSum(String pointsSum) {
        this.pointsSum = pointsSum;
    }

    public List<String> getSubjectsId() {
        return subjectsId;
    }

    public void setSubjectsId(List<String> subjectsId) {
        this.subjectsId = subjectsId;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }

    public List<String> getSubjectsName() {
        return subjectsName;
    }

    public void setSubjectsName(List<String> subjectsName) {
        this.subjectsName = subjectsName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return ("\"" + parsingDate + "\"," +
                fileName.get(0) + "," +
                fileName.get(1) + "," +
                fileName.get(2) + "," +
                "\"" + surname + "\"," +
                "\"" + name + "\"," +
                "\"" + patronymic + "\"," +
                pointsSum + "," +
                "\'" + subjectsId + "\'," +
                "\'" + points + "\'," +
//                "\"" + subjectsName.stream().map(e -> "\'" + e + "\'").collect(Collectors.toList()) + "\"," +
                "\'" + documentType + "\'").replaceAll(", ", ",");
    }

}
