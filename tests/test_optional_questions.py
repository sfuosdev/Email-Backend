from unittest.mock import patch

import pytest
from fastapi.testclient import TestClient

from main import app


@pytest.mark.parametrize(
    "team,required_answers,optional_key",
    [
        ("technology", {"tech_q9": "test-user", "tech_q10": "https://github.com/test"}, "tech_q8"),
        ("event", {"event_q4": "https://example.com/portfolio", "event_q10": "test-user"}, "event_q11"),
    ],
)
@pytest.mark.parametrize("optional_value", [None, "", "   "])
def test_optional_questions_can_be_left_blank(team, required_answers, optional_key, optional_value):
    questions = dict(required_answers)
    if optional_value is not None:
        questions[optional_key] = optional_value
    payload = {
        "fullName": "Test Applicant",
        "email": "applicant@example.com",
        "studentId": "12345678",
        "yearOfStudy": "3",
        "faculty": "Science",
        "major": "Computer Science",
        "coopTerm": "no",
        "firstChoiceTeam": team,
        "questions": questions,
    }
    with patch("routers.submit.send_application_email") as send_email:
        response = TestClient(app).post("/api/submit/exec-form", json=payload)
    assert response.status_code == 200
    send_email.assert_called_once()
