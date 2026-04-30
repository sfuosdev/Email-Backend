from enum import Enum


class TeamType(str, Enum):
    EVENT = "EVENT"
    TECHNOLOGY = "TECHNOLOGY"
    STRATEGY = "STRATEGY"

    @classmethod
    def from_string(cls, team: str) -> "TeamType":
        try:
            return cls[team.upper()]
        except KeyError:
            raise ValueError(
                f"Invalid team type: '{team}'. Must be one of: {[e.value for e in cls]}"
            )
