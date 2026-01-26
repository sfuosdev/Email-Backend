package sfuosdev.com.emailbackend.DTO;

import java.util.Map;

public class ExecAppFormDTO {

    private String fullName;
    private String email;
    private String studentId;
    private String yearOfStudy;
    private String faculty;
    private String major;
    private String coopTerm;
    private String firstChoiceTeam;

    private Map<String, String> questions;

    public ExecAppFormDTO() {}

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCoopTerm() {
        return coopTerm;
    }

    public void setCoopTerm(String coopTerm) {
        this.coopTerm = coopTerm;
    }

    public String getFirstChoiceTeam() {
        return firstChoiceTeam;
    }

    public void setFirstChoiceTeam(String firstChoiceTeam) {
        this.firstChoiceTeam = firstChoiceTeam;
    }

    public Map<String, String> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, String> questions) {
        this.questions = questions;
    }
}

