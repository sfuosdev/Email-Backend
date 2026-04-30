from typing import Dict

from pydantic import BaseModel

from enums.team_type import TeamType


class ExecFormRequest(BaseModel):
    fullName: str
    email: str
    studentId: int
    yearOfStudy: int
    faculty: str
    major: str
    isCoop: bool = False
    teamType: TeamType
    questionsList: Dict[str, str] = {}
