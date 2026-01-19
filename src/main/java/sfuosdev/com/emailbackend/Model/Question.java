package sfuosdev.com.emailbackend.Model;

import java.util.Map;

public class Question {
    public String id;
    public String teamType;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return this.id != null ? this.id : "";
    }

    public String getQuestion(String questionId, Map<String, String> questionsMap) {
        if (questionsMap != null && questionsMap.containsKey(questionId)) {
            return questionsMap.get(questionId); // Locates the Question
        }
        return "Question ID: " + this.id + " not found";
    }

    public String getAnswerForQuestion(Map<String, String> questionsMap) {
        if (questionsMap != null && questionsMap.containsKey(this.id)) {
            return questionsMap.get(this.id); // Locates what the pair is per question ID
        }
        return "Question ID: " + this.id + " not found";
    }
}