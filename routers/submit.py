from typing import Any, Dict

from fastapi import APIRouter
from fastapi.responses import JSONResponse

from core.formatters import extract_question_number, print_formatter
from core.question_registry import detect_team_type, get_question_text
from enums.team_type import TeamType
from models.dto import ExecAppFormDTO
from models.request import ExecFormRequest
from models.response import ResponseDTO

router = APIRouter(prefix="/api/submit", tags=["submit"])


@router.post("/exec-form", response_model=ResponseDTO)
async def receive_exec_form(form_dto: ExecAppFormDTO):
    try:
        student_id = int(form_dto.studentId)
        year_of_study = int(form_dto.yearOfStudy)
    except ValueError as e:
        return JSONResponse(
            status_code=400,
            content=ResponseDTO(message=f"Invalid number format: {e}").model_dump(mode="json"),
        )

    try:
        team_type = TeamType.from_string(form_dto.firstChoiceTeam)
    except ValueError as e:
        return JSONResponse(
            status_code=400,
            content=ResponseDTO(message=str(e)).model_dump(mode="json"),
        )

    is_coop = form_dto.coopTerm is not None and form_dto.coopTerm.lower() in ("yes", "true")

    exec_form_request = ExecFormRequest(
        fullName=form_dto.fullName,
        email=form_dto.email,
        studentId=student_id,
        yearOfStudy=year_of_study,
        faculty=form_dto.faculty,
        major=form_dto.major,
        isCoop=is_coop,
        teamType=team_type,
    )

    processed_questions: Dict[str, str] = {}

    if form_dto.questions:
        sorted_keys = sorted(form_dto.questions.keys(), key=extract_question_number)

        for key in sorted_keys:
            question_team = detect_team_type(key)
            if question_team is not None and question_team != team_type:
                return JSONResponse(
                    status_code=400,
                    content=ResponseDTO(
                        message=f"Invalid question matching: {key} does not belong to team {form_dto.firstChoiceTeam}"
                    ).model_dump(mode="json"),
                )
            question_text = get_question_text(key)
            processed_questions[question_text] = form_dto.questions[key]

    exec_form_request.questionsList = processed_questions

    if exec_form_request.isCoop:
        return JSONResponse(
            status_code=400,
            content=ResponseDTO(
                message="Since you are on a co-op term, please apply to this position after your co-op term ends. "
                        "We look forward to receiving your application then!"
            ).model_dump(mode="json"),
        )
    
    team_type_str = exec_form_request.teamType.value if isinstance(exec_form_request.teamType, TeamType) else str(exec_form_request.teamType)

    print_formatter(
        exec_form_request.fullName,
        exec_form_request.email,
        exec_form_request.studentId,
        exec_form_request.yearOfStudy,
        exec_form_request.major,
        team_type_str,
        processed_questions,
    )

    return ResponseDTO(
        message="Submission received successfully",
        content=exec_form_request.model_dump(mode="json"),
    )


@router.post("/project-proposal")
async def receive_project_proposal_form(payload: Dict[str, Any]):
    return JSONResponse(status_code=400, content="Under Construction")
