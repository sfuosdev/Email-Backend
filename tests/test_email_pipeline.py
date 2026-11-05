from unittest.mock import patch

from fastapi.testclient import TestClient

from main import app


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
    with patch("services.email_sender.EMAIL_FROM", "applications@example.com"), patch(
        "services.email_sender.EMAIL_TO", "executive@example.com"
    ), patch("services.email_sender.RESEND_API_KEY", "test-key"), patch(
        "services.email_sender.resend.api_key", ""
    ), patch(
        "services.email_sender.resend.Emails.send", return_value={"id": "test-email"}
    ) as resend_send:
        response = TestClient(app).post("/api/submit/exec-form", json=payload)

    assert response.status_code == 200
    resend_send.assert_called_once()
    message = resend_send.call_args.args[0]
    assert message["from"] == "applications@example.com"
    assert message["to"] == ["executive@example.com"]
    assert message["reply_to"] == payload["email"]
    assert message["subject"] == "New Executive Application: Connection Test - TECHNOLOGY"
    assert "Full Name: Connection Test" in message["text"]
    assert "Student ID: 12345678" in message["text"]
    assert "This answer must appear in the email." in message["text"]
    assert "Questions and Answers:" in message["text"]
