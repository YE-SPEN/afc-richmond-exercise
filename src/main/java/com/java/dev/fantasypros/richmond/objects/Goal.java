package com.java.dev.fantasypros.richmond.objects;

/*
 * Embedded in this Goal object file is a GoalType enum to restrict the goal types to only open play, free kick, or penalty
 * Goals objects constructed with an invalid GoalType default to open play 
 */

public class Goal {
    private String playerId;
    private int minute;
    private GoalType type;
    private String assistedBy;
    private String team;
    
    public Goal(String scoringTeam, String scorer, int time, GoalType goalType, String assist) {
        this.team = scoringTeam;
        this.playerId = scorer;
        this.minute = time;
        this.type = goalType;
        this.assistedBy = assist;
    }

    public Goal(String scoringTeam, String scorer, int time, GoalType goalType) {
        this.team = scoringTeam;
        this.playerId = scorer;
        this.minute = time;
        this.type = goalType;
        this.assistedBy = null;
    }

    public String getScorer() {
        return this.playerId;
    }

    public void setScorer(String scorerId) {
        this.playerId = scorerId;
    }

    public int getGoalMinute() {
        return this.minute;
    }
 
    public void setGoalMinute(int time) {
        this.minute = time;
    }

    public String getAssist() {
        if (this.assistedBy != null) {
            return this.assistedBy;
        }
        return null;
    }

    public void setAssist(String assistId) {
        this.assistedBy = assistId;
    }

    public String getTeam() {
        return this.team;
    }

    public void setTeam(String scoringTeam) {
        this.team = scoringTeam;
    }

    public enum GoalType {
        OPEN_PLAY("open play"),
        FREE_KICK("free kick"),
        PENALTY("penalty");

        private String type;

        GoalType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public static GoalType fromString(String type) {
            for (GoalType goalType : GoalType.values()) {
                if (goalType.getType().equalsIgnoreCase(type)) {
                    return goalType;
                }
            }
            throw new IllegalArgumentException("Unknown goal type: " + type);
        }
    }
    
    public GoalType getGoalType() {
        return this.type;
    }
    
    public static GoalType resolveGoalType(String type) {
        GoalType goalType;
        try {
            goalType = GoalType.fromString(type);
        } catch (IllegalArgumentException e) {
            goalType = GoalType.OPEN_PLAY;
            System.err.println("Unknown goal type: " + type + ", defaulting to open play.");
        }
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.type = goalType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n")
        .append("\n  scoringTeam: '").append(team)
        .append("\n  scorer: ").append(playerId)
        .append("\n  minute: ").append(minute)
        .append("\n  goalType: ").append(type.getType());

        if (assistedBy != null) {
            sb.append("\n  assist: ").append(assistedBy);
        }

        sb.append("\n}");
        return sb.toString();
    }

}
