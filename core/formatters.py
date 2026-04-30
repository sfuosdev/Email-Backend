from typing import Dict


def extract_question_number(key: str) -> int:
    """Extract the numeric suffix from a question key like 'event_q1' or 'tech_q10'."""
    try:
        if "_q" in key:
            number_part = key[key.rfind("_q") + 2:]
            return int(number_part)
    except ValueError:
        pass
    return float("inf")  


def print_formatter(
    name: str,
    email: str,
    sid: int,
    year_of_study: int,
    major: str,
    team_type: str,
    questions: Dict[str, str],
) -> None:
    print("Summary of Submission")
    print(f"\tFull name: {name}")
    print(f"\tSFU email: {email}")
    print(f"\tStudent ID: {sid}")
    print(f"\tYear of Study: {year_of_study}")
    print(f"\tMajor: {major}")
    print(f"\tTeam choice: {team_type}")
    if questions:
        print("\tQuestions:")
        for question, answer in questions.items():
            print(f"\t\t{question}: {answer}")
