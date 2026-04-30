from typing import Optional

from enums.team_type import TeamType

QUESTION_MAP: dict[str, str] = {
    # Event Team Questions
    "event_q1": "Can you tell us a little bit about yourself?",
    "event_q2": "What makes you the best fit for this position?",
    "event_q3": "What do you want to achieve or learn from this role?",
    "event_q4": "Please share links to your portfolio or LinkedIn?",
    "event_q5": (
        "What is your process in planning events? "
        "What would be your approach to engage with participants before, during, and after the event?"
    ),
    "event_q6": (
        "How do you handle conflicts or issues that arise during the events? "
        "What steps do you take to ensure a positive and inclusive atmosphere for all participants?"
    ),
    "event_q7": "How do you tailor your content to different social media platforms (e.g Instagram, LinkedIn) to maximize engagement?",
    "event_q8": "What kind of events would you like to organize for the club?",
    "event_q9": "What is your availability?",
    "event_q10": "Please tell us your Discord username in our club server?",

    # Tech Team Questions
    "tech_q1": "Can you tell us a little bit about yourself?",
    "tech_q2": "What interests you about this position?",
    "tech_q3": "What programming languages and frameworks have you worked with previously?",
    "tech_q4": (
        "What is your experience with version control systems such as Git and GitHub / GitLab? "
        "Can you provide examples of how you collaborated with others?"
    ),
    "tech_q5": "How are you familar with cloud computing tools and platforms such as AWS, Supabase, and Firebase?",
    "tech_q6": "What do you want to achieve or learn from this role?",
    "tech_q7": "What is your availability for fall semester?",
    "tech_q8": "Is there anything else you would like us to know?",
    "tech_q9": "What is your Discord user name in our club server",
    "tech_q10": "Please provide a link to your GitHub or Portfolio.",

    # Strategy Team Questions
    "strategy_q1": "Can you tell us a little bit about yourself?",
    "strategy_q2": "What interests you about this position?",
    "strategy_q3": (
        "What is your level of experience in project management? "
        "Please describe any past experience if applicable (e.g class projects, Co-op)"
    ),
    "strategy_q4": "What is your availability?",
    "strategy_q5": "Can you be in-person if there is in-person events ongoing?",
    "strategy_q6": "What is your Discord username in our club server?",
}


def get_question_text(key: str) -> str:
    return QUESTION_MAP.get(key, key)


def detect_team_type(key: str) -> Optional[TeamType]:
    if key.startswith("event_"):
        return TeamType.EVENT
    if key.startswith("tech_"):
        return TeamType.TECHNOLOGY
    if key.startswith("strategy_"):
        return TeamType.STRATEGY
    return None
