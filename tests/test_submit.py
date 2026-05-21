# import pytest
from fastapi.testclient import TestClient

from main import app

client = TestClient(app)


def test_submit_exec_form_success():
    # Test data for a successful submission
    test_data = {
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "studentId": "12345678",
        "yearOfStudy": "3",
        "faculty": "Engineering",
        "major": "Computer Science",
        "coopTerm": "no",
        "firstChoiceTeam": "EVENT",
        "questions": {
        }
    }

    response = client.post("/api/submit/exec-form", json=test_data)

    assert response.status_code == 200
    data = response.json()
    assert "message" in data
    assert data["message"] == "Submission received successfully"
    assert "content" in data


def test_submit_exec_form_invalid_student_id():
    # Test with invalid student ID
    test_data = {
        "fullName": "Jane Doe",
        "email": "jane.doe@example.com",
        "studentId": "invalid",
        "yearOfStudy": "2",
        "faculty": "Science",
        "major": "Biology",
        "coopTerm": "no",
        "firstChoiceTeam": "TECHNOLOGY",
        "questions": {}
    }

    response = client.post("/api/submit/exec-form", json=test_data)

    assert response.status_code == 400
    data = response.json()
    assert "message" in data
    assert "Invalid number format" in data["message"]


def test_submit_exec_form_coop_term():
    # Test with coop term yes
    test_data = {
        "fullName": "Alice Smith",
        "email": "alice.smith@example.com",
        "studentId": "87654321",
        "yearOfStudy": "4",
        "faculty": "Arts",
        "major": "Psychology",
        "coopTerm": "yes",
        "firstChoiceTeam": "STRATEGY",
        "questions": {}
    }

    response = client.post("/api/submit/exec-form", json=test_data)

    assert response.status_code == 400
    data = response.json()
    assert "message" in data
    assert "co-op term" in data["message"]


def test_submit_exec_form_invalid_team():
    # Test with invalid team type
    test_data = {
        "fullName": "Bob Johnson",
        "email": "bob.johnson@example.com",
        "studentId": "11223344",
        "yearOfStudy": "1",
        "faculty": "Business",
        "major": "Finance",
        "coopTerm": "no",
        "firstChoiceTeam": "invalid_team",
        "questions": {}
    }

    response = client.post("/api/submit/exec-form", json=test_data)

    assert response.status_code == 400
    data = response.json()
    assert "message" in data
    assert "Invalid team type" in data["message"]

def test_submit_exec_form_missing_fields():
    # Test for when one component is missing
    test_data = {
        "fullName": "Charlie Brown",
        "email": "charlie.brown@example.com",
        "studentId": "330102020",
        "yearOfStudy": "1",
        "faculty": "  ",
        "major": "Finance",
        "coopTerm": "no",
        "firstChoiceTeam": "STRATEGY",
        "questions": {}
    }

    response = client.post("/api/submit/exec-form", json=test_data)
    assert response.status_code == 400
    data = response.json()
    assert "message" in data
    assert "Missing required fields" in data["message"]
