package com.java.dev.fantasypros.richmond.serialization;

import com.java.dev.fantasypros.richmond.exceptions.SerializationFailureException;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.objects.Match;
import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Goal;
import com.java.dev.fantasypros.richmond.objects.Season;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

    public static String serializePlayerCard(Team team, Player player, Season season) throws SerializationFailureException {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
            // Create a main JSON object to hold player and match information
            JsonObject playerCardJson = new JsonObject();
    
            // Serialize player details
            JsonObject playerJson = serializePlayerObjectToJson(player);
            playerCardJson.add("player", playerJson);
    
            // Serialize match objects related to the player
            JsonArray matchesArray = serializeMatchObjectsToJson(team, player, season);
            playerCardJson.add("matches", matchesArray);
    
            // Convert to JSON string using Gson
            return gson.toJson(playerCardJson);
        } catch (NullPointerException e) {
            throw new SerializationFailureException(e.getMessage());
        }
    }

    public static JsonArray serializeMatchObjectsToJson(Team team, Player player, Season season) {
        JsonArray matchesArray = new JsonArray();
    
        // Ensure matchedSeason is fetched correctly
        Season matchedSeason = findSeason(team, season);
        if (matchedSeason == null) {
            return matchesArray; 
        }
    
        // Iterate over each match in the team's list of matches for the correct season
        for (Match match : matchedSeason.getMatches()) {
    
            // Check if the team played in this match
            if (match.getHomeTeam().equals(team.getTeamName()) || match.getAwayTeam().equals(team.getTeamName())) {
                // Create a JSON object for the current match
                JsonObject matchJson = new JsonObject();
                matchJson.addProperty("homeTeam", match.getHomeTeam());
                matchJson.addProperty("awayTeam", match.getAwayTeam());
    
                // Add score details to the match JSON object with null check for score
                JsonObject scoreJson = new JsonObject();
                if (match.getScore() != null) {
                    scoreJson.addProperty("home", match.getScore().getHomeScore());
                    scoreJson.addProperty("away", match.getScore().getAwayScore());
                }
                matchJson.add("score", scoreJson);
    
                // Initialize arrays for goals and assists
                JsonArray goalsArray = new JsonArray();
                JsonArray assistsArray = new JsonArray();
    
                // Iterate over the goals in the current match
                for (Goal goal : match.getGoals()) {
                    String scorer = goal.getScorer();
                    String assist = goal.getAssist() != null ? goal.getAssist() : null;
    
                    // Check if the player scored or assisted the goal
                    if (scorer != null && scorer.equals(player.getId())) {
                        goalsArray.add(serializeGoalObjectToJson(goal, scorer));
                    }
    
                    if (assist != null && assist.equals(player.getId())) {
                        assistsArray.add(serializeAssistObjectToJson(goal, scorer, assist));
                    }
                }
    
                // add goal & assist arrays to Json object if non-empty
                if (goalsArray.size() > 0) {
                    matchJson.add("goals", goalsArray);
                }
                if (assistsArray.size() > 0) {
                    matchJson.add("assists", assistsArray);
                }
    
                // Add the match JSON object to the matches array
                matchesArray.add(matchJson);
            }   
        }
    
        return matchesArray;
    }

    public static String serializeTeamToJson(Team team) throws SerializationFailureException {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
    
            jsonObject.addProperty("team", team.getTeamName());
    
            JsonArray playersArray = new JsonArray();
            
            // Iterate through each player in the team and add their stats
            for (Player player : team.getPlayers()) {
                JsonObject playerJson = serializePlayerObjectToJson(player);
                playersArray.add(playerJson);
            }
    
            jsonObject.add("players", playersArray);
    
            return gson.toJson(jsonObject);
        } catch (NullPointerException e) {
            throw new SerializationFailureException(e.getMessage());
        }

    }

    // return a Json Object mapped to a single Player object
    public static JsonObject serializePlayerObjectToJson(Player player) {
        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("id", player.getId());
        playerJson.addProperty("name", player.getName());
        playerJson.addProperty("position", player.getPos());
        playerJson.addProperty("number", player.getNum());
        playerJson.addProperty("nationality", player.getNationality());
        playerJson.addProperty("games", player.getGamesPlayed());
        playerJson.addProperty("goals", player.getGoals());
        playerJson.addProperty("assists", player.getAssists());
        playerJson.addProperty("points", player.getPoints());
        return playerJson;
    }
    
        // return a Json Object mapped to a single Goal object
    private static JsonObject serializeGoalObjectToJson(Goal goal, String scorer) {
        JsonObject goalJson = new JsonObject();
        goalJson.addProperty("playerId", scorer);
        goalJson.addProperty("minute", goal.getGoalMinute());
        goalJson.addProperty("goalType", goal.getGoalType().getType());
            
        if (goal.getAssist() == null) {
            goalJson.add("assistedBy", JsonNull.INSTANCE);
        } else {
            goalJson.addProperty("assistedBy", goal.getAssist());
        }

        return goalJson;
    }

    // return a Json Object mapped to a single Goal object
    private static JsonObject serializeAssistObjectToJson(Goal goal, String scorer, String assist) {
        JsonObject assistJson = new JsonObject();
        assistJson.addProperty("playerId", scorer);
        assistJson.addProperty("minute", goal.getGoalMinute());
        assistJson.addProperty("goalType", goal.getGoalType().getType()); 

        if (goal.getAssist() == null) {
            assistJson.add("assistedBy", JsonNull.INSTANCE);
        } else {
            assistJson.addProperty("assistedBy", goal.getAssist());
        }
        return assistJson;
    }

    public static Season findSeason(Team team, Season season) {
        for (Season teamSeason : team.getSeasons()) {
            if (teamSeason.getSeason().equals(season.getSeason())) {
                return season;
            }
        }
        return null;
    }

}
