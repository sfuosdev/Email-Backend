package sfuosdev.com.emailbackend.Model;

import sfuosdev.com.emailbackend.Types.TeamType;

import java.util.HashMap;
import java.util.Map;

public class QuestionRegistry {
    private static final Map<String, String> QUESTION_MAP = new HashMap<>();

    static {
        // Event Team Questions
        QUESTION_MAP.put("event_q1", "Can you tell us a little bit about yourself?");
        QUESTION_MAP.put("event_q2", "What makes you the best fit for this position?");
        QUESTION_MAP.put("event_q3", "What do you want to achieve or learn from this role?");
        QUESTION_MAP.put("event_q4", "Please share links to your portfolio or LinkedIn? ");
        QUESTION_MAP.put("event_q5", "What is your process in planning events? " +
                "What would be your approach to engage with participants before, during, and after the event?");
        QUESTION_MAP.put("event_q6", "How do you handle conflicts or issues that arise during the events? " +
                "What steps do you take to ensure a positive and inclusive atmosphere for all participants?");
        QUESTION_MAP.put("event_q7", "How do you tailor your content to different social media platforms (e.g Instagram, LinkedIn) to maximize engagement?");
        QUESTION_MAP.put("event_q8", "What kind of events would you like to organize for the club?");
        QUESTION_MAP.put("event_q9", "What is your availability?");
        QUESTION_MAP.put("event_q10", "Please tell us your Discord username in our club server?");

        // Tech Team Questions
        QUESTION_MAP.put("tech_q1", "Can you tell us a little bit about yourself?");
        QUESTION_MAP.put("tech_q2", "What interests you about this position?");
        QUESTION_MAP.put("tech_q3", "What programming languages and frameworks have you worked with previously?");
        QUESTION_MAP.put("tech_q4", "What is your experience with version control systems such as Git and GitHub / GitLab? " +
                "Can you provide examples of how you collaborated with others?");
        QUESTION_MAP.put("tech_q5", "How are you familar with cloud computing tools and platforms such as AWS, Supabase, and Firebase?");
        QUESTION_MAP.put("tech_q6", "What do you want to achieve or learn from this role?");
        QUESTION_MAP.put("tech_q7", "What is your availability for fall semester?");
        QUESTION_MAP.put("tech_q8", "Is there anything else you would like us to know?");
        QUESTION_MAP.put("tech_q9", "What is your Discord user name in our club server");
        QUESTION_MAP.put("tech_q10", "Please provide a link to your GitHub or Portfolio.");

        // Strategy Team Questions
        QUESTION_MAP.put("strategy_q1", "Can you tell us a little bit about yourself?");
        QUESTION_MAP.put("strategy_q2", "What interests you about this position?");
        QUESTION_MAP.put("strategy_q3", "What is your level of experience in project management? " +
                "Please describe any past experience if applicable (e.g class projects, Co-op)");
        QUESTION_MAP.put("strategy_q4", "What is your availability?");
        QUESTION_MAP.put("strategy_q5", "Can you be in-person if there is in-person events ongoing?");
        QUESTION_MAP.put("strategy_q6", "What is your Discord username in our club server?");
    }

    public static String getQuestionText(String key) {
        return QUESTION_MAP.getOrDefault(key, key);
    }

    public static TeamType detectTeamType(String key) {
        if (key == null) return null;
        if (key.startsWith("event_")) return TeamType.EVENT;
        if (key.startsWith("tech_")) return TeamType.TECHNOLOGY;
        if (key.startsWith("strategy_")) return TeamType.STRATEGY;
        return null;
    }
}
