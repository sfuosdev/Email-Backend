from core.question_registry import QUESTION_MAP, get_question_text


def test_registry_contains_only_current_application_questions():
    assert set(QUESTION_MAP) == {
        "tech_q8", "tech_q9", "tech_q10",
        "event_q4", "event_q10", "event_q11",
    }


def test_event_discord_and_optional_question_have_distinct_labels():
    assert "Discord" in get_question_text("event_q10")
    assert get_question_text("event_q11") == "Do you have any questions for us? (Optional)"
