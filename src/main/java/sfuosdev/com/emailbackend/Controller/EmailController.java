package sfuosdev.com.emailbackend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfuosdev.com.emailbackend.Model.ExecFormRequest;
import sfuosdev.com.emailbackend.DTO.ResponseDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/submit")
@CrossOrigin(origins = "*")
public class EmailController {

    @PostMapping("/exec-form")
    public ResponseEntity<ResponseDTO> receiveExecForm(@RequestBody Map<String, Object> payload) {
        try {
            String fullName = (String) payload.get("fullName");
            String email = (String) payload.get("email");

            Integer studentId = null;
            if (payload.get("studentId") != null) {
                studentId = Integer.valueOf(payload.get("studentId").toString());
            }

            Integer yearOfStudy = null;
            if (payload.get("yearOfStudy") != null) {
                yearOfStudy = Integer.valueOf(payload.get("yearOfStudy").toString());
            }

            String major = (String) payload.get("major");
            String teamType = (String) payload.get("firstChoiceTeam");

            ExecFormRequest execFormRequest = new ExecFormRequest(fullName, email, studentId, yearOfStudy, major, teamType);

            printFormatter(fullName, email, studentId, yearOfStudy, major, teamType);

            return new ResponseEntity<>(new ResponseDTO("Submission received successfully", execFormRequest), HttpStatus.OK);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Invalid number format"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            return new ResponseEntity<>(new ResponseDTO("Error processing request: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/project-proposal")
    public ResponseEntity<String> receiveProjectProposalForm(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>("Under Construction", HttpStatus.BAD_REQUEST);
    }

    public void printFormatter(String name, String email, Integer sid, Integer yearOfStudy, String major, String teamType) {
        System.out.println("Summary of Submission");
        System.out.println("\tFull name: " + name);
        System.out.println("\tSFU email: " + email);
        System.out.println("\tStudent ID: " + sid);
        System.out.println("\tYear of Study: " + yearOfStudy);
        System.out.println("\tMajor: " + major);
        System.out.println("\tTeam choice: " + teamType);
    }
}
