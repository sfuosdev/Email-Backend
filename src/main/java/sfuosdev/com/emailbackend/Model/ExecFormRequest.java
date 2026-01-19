package sfuosdev.com.emailbackend.Model;

import sfuosdev.com.emailbackend.Types.TeamType;

import java.util.HashMap;
import java.util.Map;

public class ExecFormRequest {
    private String fullName;
    private String email;
    private Integer studentId;
    private Integer yearOfStudy;
    private String major;
    private Boolean isCoop = false;   // isCoop is false by default
    private TeamType teamType;
    private Map<String, String> questionsList;

    public ExecFormRequest(String name, String email, Integer sid, Integer yearOfStudy, String major, String teamType) {
        this.fullName = name;
        this.email = email;
        this.studentId = sid;
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.teamType = TeamType.getTeamType(teamType);
        this.questionsList = new HashMap<>();
    }

    // getter functions
    public String getFullName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public Integer getYearOfStudy() {
        return this.yearOfStudy;
    }

    public String getMajor() {
        return this.major;
    }

    public TeamType getTeamType() {
        return this.teamType;
    }

    public Boolean getIsCoop() {
        return this.isCoop;
    }

    public Map<String, String> getQuestionsList() {
        return this.questionsList;
    }

    // setter function
    public void setCoopToTrue() {
        this.isCoop = true;
    }


}
