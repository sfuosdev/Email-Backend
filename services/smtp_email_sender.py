from email.message import EmailMessage
import smtplib
from core.config import SMTP_FROM, SMTP_TO
from core.config import SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD, SMTP_USE_TLS, SMTP_USE_SSL


def build_email_message(
        full_name: str,
        sfu_email: str,
        student_id: int,
        year_of_study: int,
        faculty: str,
        major: str,
        co_op: bool,
        team_type: str,
) -> EmailMessage:
    msg = EmailMessage()
    msg["Subject"] = f"New Executive Application: {full_name} - {team_type}"
    msg["From"] = SMTP_FROM
    msg["To"] = SMTP_TO
    msg["Reply-To"] = sfu_email

    msg.set_content(f"""\
Full Name: {full_name}
SFU Email: {sfu_email}
Student ID: {student_id}
Year of Study: {year_of_study}
Major: {major}
Faculty: {faculty}
Co-op: {"Yes" if co_op else "No"}
Team Type: {team_type}
""")
    
    return msg

def send_email_message(msg: EmailMessage) -> None:
    if SMTP_USE_SSL:
        with smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT) as server:
            if SMTP_USERNAME and SMTP_PASSWORD:
                server.login(SMTP_USERNAME, SMTP_PASSWORD)
            server.send_message(msg)
    else:
        with smtplib.SMTP(SMTP_HOST, SMTP_PORT) as server:
            if SMTP_USE_TLS:
                server.starttls()
            if SMTP_USERNAME and SMTP_PASSWORD:
                server.login(SMTP_USERNAME, SMTP_PASSWORD)
            server.send_message(msg)

def send_application_email(
        full_name: str,
        sfu_email: str,
        student_id: int,
        year_of_study: int,
        faculty: str,
        major: str,
        co_op: bool,
        team_type: str,
) -> None:
    msg = build_email_message(
        full_name=full_name,
        sfu_email=sfu_email,
        student_id=student_id,
        year_of_study=year_of_study,
        faculty=faculty,
        major=major,
        co_op=co_op,
        team_type=team_type
    )
    send_email_message(msg)


