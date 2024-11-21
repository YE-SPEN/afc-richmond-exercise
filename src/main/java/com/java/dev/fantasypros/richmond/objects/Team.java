package com.java.dev.fantasypros.richmond.objects;

import java.util.ArrayList;
import java.util.List;

import com.java.dev.fantasypros.richmond.objects.Goal.GoalType;

/*
 * For the purpose of this exercise there is only one season (2023-24) but this object type can support the addition of multiple seasons
 * each with its own match log
 */

public class Team {
    private String team;
    private List<Player> players;
    private List<Season> seasons;

    public Team(String name) {
        this.team = name;
        this.players = new ArrayList<Player>();
        this.seasons = new ArrayList<Season>();
    }

    public String getTeamName() {
        return this.team;
    }

    public void setTeamName(String name) {
        this.team = name;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<Player> roster) {
        this.players = roster;
    }

    public void addToRoster(Player player) {
        this.players.add(player);
    }

    public List<Season> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(List<Season> seasonList) {
        this.seasons = seasonList;
    }

    public void addSeason(Season year) {
        this.seasons.add(year);
    }

    public static Season findSeason(Team team, Season season) {
        for (Season teamSeason : team.getSeasons()) {
            if (teamSeason.getSeason().equals(season.getSeason())) {
                return season;
            }
        }
        return null;
    }

    public Player getPlayerById(String id) {
        for (Player player : this.players) {
            if (id.equals(player.getId())) {
                return player;
            }
        }
        return null;
    }

    public void updatePlayerStats(Goal goal) {
        this.getPlayers().forEach(player -> {
            if (player.getId().equals(goal.getScorer())) {
                player.recordGoal();
            }
            if (goal.getAssist() != null && player.getId().equals(goal.getAssist())) {
                player.recordAssist();
            }
        });
    }

    // we assume in this method that every oplayer on the team plays every game
    public void incrementGamesPlayed() {
        this.getPlayers().forEach(player -> {
            player.recordGame();
        });
    }

    public int countGoalsByType(Season season, String playerId, GoalType goalType) {
        int goalCount = 0;
        for (Match match : season.getMatches()) {
            for (Goal goal : match.getGoals()) {
                if (goal.getScorer().equals(playerId) && goal.getGoalType().equals(goalType)) {
                    goalCount++;
                }
            }
        }
        return goalCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team{\n")
          .append("teamName: '").append(team).append('\'');
    
        sb.append(", \nplayers: [\n");
        for (int i = 0; i < players.size(); i++) {
            sb.append(players.get(i).toString());
            if (i < players.size() - 1) {
                sb.append(",\n ");
            }
        }
        sb.append("]");
    
        sb.append(", seasons=[\n");
        for (int i = 0; i < seasons.size(); i++) {
            sb.append(seasons.get(i).toString());
            if (i < seasons.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
    
        sb.append('}');
        return sb.toString();
    }
    
}
