package com.java.dev.fantasypros.richmond.objects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private String matchId;
    private LocalDate date;
    private String homeTeam;
    private String awayTeam;
    private Score score;
    private List<Goal> goals;

    public Match(String id, String home, String away, LocalDate date) {
        this.matchId = id;
        this.homeTeam = home;
        this.awayTeam = away;
        this.date = date;
        this.score = new Score();
        this.goals = new ArrayList<Goal>();
    }

    public String getMatchId() {
        return this.matchId;
    }

    public void setMatchId(String id) {
        this.matchId = id;
    }

    public String getMatchDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.date.format(formatter);
    }

    public void setMatchDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = LocalDate.parse(dateStr, formatter);
    }

    public String getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(String team) {
        this.homeTeam = team;
    }

    public String getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(String team) {
        this.awayTeam = team;
    }

    public Score getScore() {
        return this.score;
    }

    public void setScore(int homeScore, int awayScore) {
        this.score.setHomeScore(homeScore);
        this.score.setAwayScore(awayScore);
    }

    public void recordGoal(Goal goal) {
        this.goals.add(goal);
    }

    public List<Goal> getGoals() {
        return this.goals;
    }
    
    public void updateScore(String scoringTeam) {
        if (scoringTeam.equals(this.homeTeam)) {
            this.score.incrementHomeScore();
        }
        else if (scoringTeam.equals(this.awayTeam)) {
            this.score.incrementAwayScore();
        }
    }

    public boolean homeTeamWon() {
        return this.score.getHomeScore() > this.score.getAwayScore();
    }

    public boolean awayTeamWon() {
        return this.score.getHomeScore() < this.score.getAwayScore();
    }

    public char getResult(Team team) {
        if (this.score.getHomeScore() == this.score.getAwayScore()) {
            return 'D';
        }
        if (team.getTeamName().equals(homeTeam) && this.homeTeamWon() || team.getTeamName().equals(awayTeam) && this.awayTeamWon()) {
            return 'W';
        }
        if (team.getTeamName().equals(homeTeam) && !this.homeTeamWon() || team.getTeamName().equals(awayTeam) && !this.awayTeamWon()) {
            return 'L';
        }
        return '-';
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
          .append("\n  homeTeam: '").append(homeTeam).append('\'')
          .append("\n  awayTeam: '").append(awayTeam).append('\'')
          .append("\n  score: '").append(score.toString()).append('\'');
    
        if (!goals.isEmpty()) {
            sb.append(", \n  goals: [");
            for (Goal goal : goals) {
                sb.append(goal.toString()).append(", ");
            }
            sb.setLength(sb.length() - 2); 
            sb.append(']');
        }
    
        sb.append('}');
        return sb.toString();
    }    


}
