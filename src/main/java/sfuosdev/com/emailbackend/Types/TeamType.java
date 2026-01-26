package sfuosdev.com.emailbackend.Types;

public enum TeamType {
    EVENT,
    TECHNOLOGY,
    STRATEGY;

    public static TeamType getTeamType(String team) {
        return TeamType.valueOf(team.toUpperCase());
    }

    public static String getTeamTypeString(TeamType teamType) {
        return teamType.toString();
    }
}
