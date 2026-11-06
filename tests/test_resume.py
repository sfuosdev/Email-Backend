import base64
from unittest.mock import patch

import pytest
from fastapi.testclient import TestClient

from main import app
from models.dto import MAX_RESUME_BYTES


def encoded(data):
    return base64.b64encode(data).decode("ascii")


@pytest.mark.parametrize("resume", [
    None,
    {"filename": "resume.pdf", "content": ""},
    {"filename": "resume.pdf", "content": "not-base64"},
    {"filename": "resume.pdf", "content": encoded(b"not a PDF")},
    {"filename": "resume.docx", "content": encoded(b"%PDF-1.4\n%%EOF")},
    {"filename": "../resume.pdf", "content": encoded(b"%PDF-1.4\n%%EOF")},
    {"filename": "resume.pdf", "content": encoded(b"%PDF-" + b"x" * MAX_RESUME_BYTES)},
])
def test_invalid_or_missing_resume_never_sends_email(resume):
    payload = {
        "fullName": "Test Applicant", "email": "applicant@example.com",
        "studentId": "12345678", "yearOfStudy": "3",
        "faculty": "Science", "major": "Computer Science", "coopTerm": "no",
        "firstChoiceTeam": "technology", "questions": {},
    }
    if resume is not None:
        payload["resume"] = resume
    with patch("routers.submit.send_application_email") as send:
        response = TestClient(app).post("/api/submit/exec-form", json=payload)
    assert response.status_code == 422
    send.assert_not_called()
