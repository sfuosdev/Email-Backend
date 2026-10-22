from unittest.mock import patch

from fastapi.testclient import TestClient

from main import app
from services.smtp_email_sender import send_application_email


def test_application_answers_reach_email_body():
    payload = {
        "fullName": "Connection Test",
        "email": "applicant@example.com",
        "studentId": "12345678",
        "yearOfStudy": "3",
        "faculty": "Science",
        "major": "Computer Science",
        "coopTerm": "no",
        "firstChoiceTeam": "technology",
        "questions": {"tech_q1": "This answer must appear in the email."},
    }
    with patch("routers.submit.send_application_email", wraps=send_application_email), patch(
        "services.smtp_email_sender.send_email_message"
    ) as smtp_send:
        response = TestClient(app).post("/api/submit/exec-form", json=payload)
    assert response.status_code == 200
    smtp_send.assert_called_once()
    message = smtp_send.call_args.args[0]
    assert "This answer must appear in the email." in message.get_content()
    assert "Questions and Answers:" in message.get_content()
    assert message["Reply-To"] == payload["email"]
