from typing import Dict, Optional

import base64
import binascii

from pydantic import BaseModel, Field, field_validator


MAX_RESUME_BYTES = 5 * 1024 * 1024


class ResumeAttachment(BaseModel):
    filename: str = Field(min_length=1, max_length=200)
    content: str = Field(min_length=1, max_length=4 * ((MAX_RESUME_BYTES + 2) // 3))

    @field_validator("filename")
    @classmethod
    def validate_filename(cls, value: str) -> str:
        if not value.lower().endswith(".pdf") or any(c in value for c in ("/", "\\")) or any(ord(c) < 32 for c in value):
            raise ValueError("Resume must have a valid PDF filename.")
        return value

    @field_validator("content")
    @classmethod
    def validate_content(cls, value: str) -> str:
        try:
            data = base64.b64decode(value, validate=True)
        except (ValueError, binascii.Error):
            raise ValueError("Resume must contain valid Base64 data.")
        if not data.startswith(b"%PDF-"):
            raise ValueError("Please upload a PDF resume.")
        if len(data) > MAX_RESUME_BYTES:
            raise ValueError("Resume must be no larger than 5 MB.")
        return value


class ExecAppFormDTO(BaseModel):
    fullName: str
    email: str
    studentId: str
    yearOfStudy: str
    faculty: str
    major: str
    coopTerm: Optional[str] = None
    firstChoiceTeam: str
    questions: Optional[Dict[str, str]] = None
    resume: ResumeAttachment
