package com.java.dev.fantasypros.richmond.loaders;

import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.objects.Season;
import com.java.dev.fantasypros.richmond.objects.Match;
import com.java.dev.fantasypros.richmond.objects.Goal;
import com.java.dev.fantasypros.richmond.objects.Goal.GoalType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class MatchLoader {

    private static final String MATCHES_JSON_URL = "https://java-dev.fantasypros.com/richmond/matches.json";

    public static Team loadMatches(Team team) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MATCHES_JSON_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());

            JsonNode seasonNode = rootNode.get("season");
            Season season = new Season(seasonNode.asText());

            getMatchesBySeason(rootNode, season, team);

            return team;

        } catch (IOException | InterruptedException e) {
            System.err.println("Error loading matches: " + e.getMessage());
        }

        return team;
    }

    // Method to parse JSON into Match objects and add to the given season
    private static void getMatchesBySeason(JsonNode rootNode, Season season, Team team) {
        JsonNode matchesArray = rootNode.get("matches");

        if (matchesArray != null && matchesArray.isArray()) {
            for (JsonNode matchNode : matchesArray) {
                String id = matchNode.get("matchId").asText();
                String homeTeam = matchNode.get("homeTeam").asText();
                String awayTeam = matchNode.get("awayTeam").asText();
                LocalDate date = LocalDate.parse(matchNode.get("date").asText());

                // Check if the input team is involved in the match
                if (team.getTeamName().equals(homeTeam) || team.getTeamName().equals(awayTeam)) {
                    Match match = new Match(id, homeTeam, awayTeam, date);
                    incrementGamesPlayed(team);

                    JsonNode goalArray = matchNode.get("goals");
                    for (JsonNode goalNode : goalArray) {
                        Goal goal = processGoal(match, goalNode);
                        updatePlayerStats(team, goal);
                        match.recordGoal(goal);
                    }

                    season.scheduleMatch(match);
                }
            }
        }
        team.addSeason(season);
    }

    public static void incrementGamesPlayed(Team team) {
        team.getPlayers().forEach(player -> {
            player.recordGame();
        });
    }

    public static Goal processGoal(Match match, JsonNode goalNode) {
        String team = goalNode.get("team").asText();
        String scorer = goalNode.get("playerId").asText();
        int minute = goalNode.get("minute").asInt();
        String assist = goalNode.get("assistedBy").asText();
        GoalType type = resolveGoalType(goalNode.get("type").asText());

        Goal goal;
        if (assist != null) {
            goal = new Goal(team, scorer, minute, type, assist);
        }
        else {
            goal = new Goal(team, scorer, minute, type);
        }
        return goal;  
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

    public static void updatePlayerStats(Team team, Goal goal) {
        team.getPlayers().forEach(player -> {
            if (player.getId().equals(goal.getScorer())) {
                player.recordGoal();
            }
    
            if (goal.getAssist() != null && player.getId().equals(goal.getAssist())) {
                player.recordAssist();
            }
        });
    }

}
