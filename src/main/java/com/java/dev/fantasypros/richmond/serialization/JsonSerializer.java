package com.java.dev.fantasypros.richmond.serialization;

import com.java.dev.fantasypros.richmond.exceptions.SerializationFailureException;
import com.java.dev.fantasypros.richmond.objects.*;
import com.java.dev.fantasypros.richmond.objects.Goal.GoalType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * This file stores a method to serialize each of my object types that are used in any endpoint. 
 * They are modular to facilitate writing additional unit tests
 */

public class JsonSerializer {

    public static String serializePlayerCard(Team team, Player player, Season season) throws SerializationFailureException {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        
            JsonObject playerCardJson = new JsonObject();
    
            JsonObject playerJson = serializePlayerObjectToJson(player);
            playerCardJson.add("player", playerJson);

            JsonObject goalBreakdownJson = serializeGoalBreakdownToJson(season, player, team);
            playerCardJson.add("goalBreakdown", goalBreakdownJson);
    
            JsonArray matchesArray = serializeMatchObjectsToJson(team, player, season);
            playerCardJson.add("matches", matchesArray);
    
            return gson.toJson(playerCardJson);

        } catch (NullPointerException e) {
            throw new SerializationFailureException(e.getMessage());
        }
    }

    public static JsonArray serializeMatchObjectsToJson(Team team, Player player, Season season) {
        JsonArray matchesArray = new JsonArray();
    
        Season matchedSeason = Team.findSeason(team, season);
        if (matchedSeason == null) {
            return matchesArray; 
        }
    
        for (Match match : matchedSeason.getMatches()) {
    
            if (match.getHomeTeam().equals(team.getTeamName()) || match.getAwayTeam().equals(team.getTeamName())) {

                JsonObject matchJson = new JsonObject();
                matchJson.addProperty("matchId", match.getMatchId());
                matchJson.addProperty("date", match.getMatchDate());
                matchJson.addProperty("homeTeam", match.getHomeTeam());
                matchJson.addProperty("awayTeam", match.getAwayTeam());
                matchJson.addProperty("result", match.getResult(team));
    
                JsonObject scoreJson = serializeScoreObjectToJson(match);
                matchJson.add("score", scoreJson);
    
                JsonArray goalsArray = new JsonArray();
                JsonArray assistsArray = new JsonArray();
    
                for (Goal goal : match.getGoals()) {
                    String scorer = goal.getScorer();
                    String assist = goal.getAssist();
    
                    if (scorer.equals(player.getId())) {
                        goalsArray.add(serializeGoalObjectToJson(goal));
                    }
                    else if (assist != null && assist.equals(player.getId())) {
                        assistsArray.add(serializeGoalObjectToJson(goal));
                    }    

                }
    
                if (goalsArray.size() > 0) {
                    matchJson.add("goals", goalsArray);
                }
                if (assistsArray.size() > 0) {
                    matchJson.add("assists", assistsArray);
                }
    
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

    public static JsonObject serializePlayerObjectToJson(Player player) {
        JsonObject playerJson = new JsonObject();
        playerJson.addProperty("id", player.getId());
        playerJson.addProperty("name", player.getName());
        playerJson.addProperty("position", player.getPos());
        playerJson.addProperty("number", player.getJerseyNum());
        playerJson.addProperty("nationality", player.getNationality());
        playerJson.addProperty("games", player.getGamesPlayed());
        playerJson.addProperty("goals", player.getGoals());
        playerJson.addProperty("assists", player.getAssists());
        playerJson.addProperty("points", player.getPoints());
        return playerJson;
    }
    
    private static JsonObject serializeGoalObjectToJson(Goal goal) {
        JsonObject goalJson = new JsonObject();
        goalJson.addProperty("playerId", goal.getScorer());
        goalJson.addProperty("minute", goal.getGoalMinute());
        goalJson.addProperty("goalType", goal.getGoalType().getType());
        goalJson.addProperty("assistedBy", goal.getAssist());

        return goalJson;
    }

    private static JsonObject serializeScoreObjectToJson(Match match) {
        JsonObject scoreJson = new JsonObject();
        if (match.getScore() != null) {
            scoreJson.addProperty("home", match.getScore().getHomeScore());
            scoreJson.addProperty("away", match.getScore().getAwayScore());
        }
        return scoreJson;
    }

    private static JsonObject serializeGoalBreakdownToJson(Season season, Player player, Team team) {
        JsonObject scoreJson = new JsonObject();

        for (GoalType goalType : GoalType.values()) {
            int goalCount = team.countGoalsByType(season, player.getId(), goalType);
            scoreJson.addProperty(goalType.getType(), goalCount);
        }

        return scoreJson;
    }

}
