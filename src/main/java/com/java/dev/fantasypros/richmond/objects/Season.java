package com.java.dev.fantasypros.richmond.objects;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private String season;
    private List<Match> matches;

    public Season(String season) {
        this.season = season;
        this.matches = new ArrayList<Match>();
    }

    public String getSeason() {
        return this.season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
 
    public void scheduleMatch(Match match) {
        this.matches.add(match);
    }    

    public List<Match> getMatches() {
        return this.matches;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Season: ").append(season).append("\nMatches:\n");

        for (Match match : matches) {
            result.append(match.toString()).append("\n"); 
        }
        
        return result.toString();
    }
}
