package com.java.dev.fantasypros.richmond.loaders;

import com.java.dev.fantasypros.richmond.objects.*;
import com.google.gson.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;

public class MatchLoader {
    private static final String MATCHES_JSON_URL = "https://java-dev.fantasypros.com/richmond/matches.json";

    public static Team fetchMatchData(Team team) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MATCHES_JSON_URL)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject rootNode = JsonParser.parseString(response.body()).getAsJsonObject();
            Season season = new Season(rootNode.get("season").getAsString());
            loadMatchesBySeason(rootNode, season, team);

            return team;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error loading matches: " + e.getMessage());
        }
        return team;
    }

    private static void loadMatchesBySeason(JsonObject rootNode, Season season, Team team) {
        JsonArray matchesArray = rootNode.getAsJsonArray("matches");
        if (matchesArray != null) {
            for (JsonElement matchElement : matchesArray) {
                JsonObject matchNode = matchElement.getAsJsonObject();
                String id = matchNode.get("matchId").getAsString();
                String homeTeam = matchNode.get("homeTeam").getAsString();
                String awayTeam = matchNode.get("awayTeam").getAsString();
                LocalDate date = LocalDate.parse(matchNode.get("date").getAsString());
    
                if (team.getTeamName().equals(homeTeam) || team.getTeamName().equals(awayTeam)) {
                    Match match = new Match(id, homeTeam, awayTeam, date);

                    team.incrementGamesPlayed();
                    
                    JsonObject scoreNode = matchNode.getAsJsonObject("score");
                    int homeScore = scoreNode != null ? scoreNode.get("home").getAsInt() : 0;
                    int awayScore = scoreNode != null ? scoreNode.get("away").getAsInt() : 0;
                    match.setScore(homeScore, awayScore);
    
                    JsonArray goalArray = matchNode.getAsJsonArray("goals");
                    for (JsonElement goalElement : goalArray) {
                        Goal goal = loadGoal(goalElement.getAsJsonObject());
                        team.updatePlayerStats(goal);
                        match.recordGoal(goal);
                    }
                    season.scheduleMatch(match);
                }
            }
        }
        team.addSeason(season);
    }    

    public static Goal loadGoal(JsonObject goalNode) {
        String team = goalNode.get("team").getAsString();
        String scorer = goalNode.get("playerId").getAsString();
        int minute = goalNode.get("minute").getAsInt();
        String assist = goalNode.has("assistedBy") && !goalNode.get("assistedBy").isJsonNull()
                ? goalNode.get("assistedBy").getAsString()
                : null;
        Goal.GoalType type = Goal.resolveGoalType(goalNode.get("type").getAsString());
        return assist != null ? new Goal(team, scorer, minute, type, assist) : new Goal(team, scorer, minute, type);
    }
    
}
