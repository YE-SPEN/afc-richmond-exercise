package com.java.dev.fantasypros.richmond.objects;

import java.util.ArrayList;
import java.util.List;

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

    public Player getPlayerById(String id) {
        for (Player player : this.players) {
            if (id.equals(player.getId())) {
                return player;
            }
        }
        return null;
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
