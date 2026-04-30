from typing import Dict, Optional

from pydantic import BaseModel


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
