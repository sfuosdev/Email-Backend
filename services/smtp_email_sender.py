from typing import Dict
from email.message import EmailMessage
import resend
from core.config import RESEND_API_KEY, EMAIL_FROM, EMAIL_TO

def build_email_message(
        full_name: str,
        sfu_email: str,
        student_id: int,
        year_of_study: int,
        faculty: str,
        major: str,
        co_op: bool,
        team_type: str,
        questions: Dict[str, str]
) -> EmailMessage:
    msg = EmailMessage()
    msg["Subject"] = f"New Executive Application: {full_name} - {team_type}"
    msg["From"] = EMAIL_FROM
    msg["To"] = EMAIL_TO
    msg["Reply-To"] = sfu_email
    question_answers = "\n".join([f"{question}: {answer}" for question, answer in questions.items()])

    msg.set_content(f"""\
Full Name: {full_name}
SFU Email: {sfu_email}
Student ID: {student_id}
Year of Study: {year_of_study}
Major: {major}
Faculty: {faculty}
Co-op: {"Yes" if co_op else "No"}
Team Type: {team_type}
Questions and Answers:
{question_answers}
""")
    
    return msg

def send_email_message(msg: EmailMessage) -> None:
    resend.api_key = RESEND_API_KEY
    resend.messages.send(
        from_email=msg["From"],
        to_email=msg["To"],
        subject=msg["Subject"],
        text=msg.get_content(),
        reply_to=msg["Reply-To"]
    )

def send_application_email(
        full_name: str,
        sfu_email: str,
        student_id: int,
        year_of_study: int,
        faculty: str,
        major: str,
        co_op: bool,
        team_type: str,
        questions: Dict[str, str]
) -> None:
    msg = build_email_message(
        full_name=full_name,
        sfu_email=sfu_email,
        student_id=student_id,
        year_of_study=year_of_study,
        faculty=faculty,
        major=major,
        co_op=co_op,
        team_type=team_type,
        questions=questions
    )
    send_email_message(msg)


