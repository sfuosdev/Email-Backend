package sfuosdev.com.emailbackend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfuosdev.com.emailbackend.Model.ExecFormRequest;
import sfuosdev.com.emailbackend.Model.QuestionRegistry;
import sfuosdev.com.emailbackend.Types.TeamType;
import sfuosdev.com.emailbackend.DTO.ResponseDTO;
import sfuosdev.com.emailbackend.DTO.ExecAppFormDTO;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@RestController
@RequestMapping("/api/submit")
@CrossOrigin(origins = "*")
public class ServerController {

    @PostMapping("/exec-form")
    public ResponseEntity<ResponseDTO> receiveExecForm(@RequestBody ExecAppFormDTO formDto) {
        try {
            String fullName = formDto.getFullName();
            String email = formDto.getEmail();

            Integer studentId = Integer.valueOf(formDto.getStudentId());
            Integer yearOfStudy = Integer.valueOf(formDto.getYearOfStudy());

            String major = formDto.getMajor();
            String faculty = formDto.getFaculty();
            String teamType = formDto.getFirstChoiceTeam();

            ExecFormRequest execFormRequest = new ExecFormRequest(fullName, email, studentId, yearOfStudy, faculty, major, teamType);

            if ("yes".equalsIgnoreCase(formDto.getCoopTerm()) || "true".equalsIgnoreCase(formDto.getCoopTerm())) {
                execFormRequest.setCoopToTrue();
            }

            // Translate question keys to sentences and validate team
            Map<String, String> rawQuestions = formDto.getQuestions();
            Map<String, String> processedQuestions = new LinkedHashMap<>();

            if (rawQuestions != null) {
                List<String> sortedKeys = new ArrayList<>(rawQuestions.keySet());
                Collections.sort(sortedKeys, new Comparator<String>() {
                    @Override
                    public int compare(String k1, String k2) {
                        return Integer.compare(extractQuestionNumber(k1), extractQuestionNumber(k2));
                    }
                });

                for (String key : sortedKeys) {

                    // Validate question type matches selected team
                    TeamType questionTeam = QuestionRegistry.detectTeamType(key);
                    if (questionTeam != null && questionTeam != execFormRequest.getTeamType()) {
                         return new ResponseEntity<>(new ResponseDTO("Invalid question matching: " + key + " does not belong to team " + teamType), HttpStatus.BAD_REQUEST);
                    }

                    String questionText = QuestionRegistry.getQuestionText(key);
                    processedQuestions.put(questionText, rawQuestions.get(key));
                }
            }
            execFormRequest.setQuestionsList(processedQuestions);

            printFormatter(fullName, email, studentId, yearOfStudy, major, teamType, processedQuestions);

            return new ResponseEntity<>(new ResponseDTO("Submission received successfully", execFormRequest), HttpStatus.OK);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Invalid number format: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Error processing request: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/project-proposal")
    public ResponseEntity<String> receiveProjectProposalForm(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>("Under Construction", HttpStatus.BAD_REQUEST);
    }

    private int extractQuestionNumber(String key) {
        try {
            // Assuming format like "team_q1", "event_q10"
            if (key.contains("_q")) {
                String numberPart = key.substring(key.lastIndexOf("_q") + 2);
                return Integer.parseInt(numberPart);
            }
        } catch (NumberFormatException e) {
            // fallback if format is unexpected
        }
        return Integer.MAX_VALUE; // Push unexpected formats to the end
    }

    public void printFormatter(String name, String email, Integer sid, Integer yearOfStudy, String major, String teamType, Map<String, String> questions) {
        System.out.println("Summary of Submission");
        System.out.println("\tFull name: " + name);
        System.out.println("\tSFU email: " + email);
        System.out.println("\tStudent ID: " + sid);
        System.out.println("\tYear of Study: " + yearOfStudy);
        System.out.println("\tMajor: " + major);
        System.out.println("\tTeam choice: " + teamType);
        if (questions != null && !questions.isEmpty()) {
            System.out.println("\tQuestions:");
            for (Map.Entry<String, String> entry : questions.entrySet()) {
                System.out.println("\t\t" + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
