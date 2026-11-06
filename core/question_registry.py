from typing import Optional

from enums.team_type import TeamType

QUESTION_MAP: dict[str, str] = {
    # Technology Team
    "tech_q8": "Do you have any questions for us? (Optional)",
    "tech_q9": "What is your Discord user name in our OS Dev Discord server?",
    "tech_q10": "Please provide a link to your GitHub.",

    # Event Team
    "event_q4": "Please share links to your portfolio or LinkedIn.",
    "event_q10": "Please tell us your Discord username in our club server.",
    "event_q11": "Do you have any questions for us? (Optional)",
}


def get_question_text(key: str) -> str:
    return QUESTION_MAP.get(key, key)


def detect_team_type(key: str) -> Optional[TeamType]:
    if key.startswith("event_"):
        return TeamType.EVENT
    if key.startswith("tech_"):
        return TeamType.TECHNOLOGY
    return None
