from datetime import datetime
from typing import Any, Optional

from pydantic import BaseModel, model_validator


class ResponseDTO(BaseModel):
    message: str
    content: Optional[Any] = None
    timestamp: Optional[datetime] = None

    @model_validator(mode="after")
    def set_timestamp(self) -> "ResponseDTO":
        if self.timestamp is None:
            self.timestamp = datetime.now()
        return self
