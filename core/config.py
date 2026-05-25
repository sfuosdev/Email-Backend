import os


def _env_bool(name: str, default: str = "false") -> bool:
	return os.getenv(name, default).strip().lower() in {"1", "true", "yes", "on"}


SMTP_HOST = os.getenv("SMTP_HOST", "localhost")
SMTP_PORT = int(os.getenv("SMTP_PORT", "25"))
SMTP_USERNAME = os.getenv("SMTP_USERNAME", "")
SMTP_PASSWORD = os.getenv("SMTP_PASSWORD", "")
SMTP_FROM = os.getenv("SMTP_FROM", "")
SMTP_TO = os.getenv("SMTP_TO", "")
SMTP_USE_TLS = _env_bool("SMTP_USE_TLS")
SMTP_USE_SSL = _env_bool("SMTP_USE_SSL")
