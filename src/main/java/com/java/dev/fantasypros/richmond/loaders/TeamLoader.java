package com.java.dev.fantasypros.richmond.loaders;

import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TeamLoader {

    private static final String PLAYERS_JSON_URL = "https://java-dev.fantasypros.com/richmond/players.json";

    // Load players from JSON and return a Team object with the players
    public static Team loadTeam() {
        List<Player> players = new ArrayList<>();
        Team team = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PLAYERS_JSON_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            
            // Initialize Team object with the team name from JSON
            String teamName = rootNode.get("team").asText();
            team = new Team(teamName);

            // Extract players array
            JsonNode playersArray = rootNode.get("players");

            if (playersArray != null && playersArray.isArray()) {
                for (JsonNode playerNode : playersArray) {
                    Player player = processPlayer(playerNode);
                    players.add(player);
                }
            }

            // Add players to the team
            team.setPlayers(players);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error loading players: " + e.getMessage());
        }

        return team;
    }

    public static Player processPlayer(JsonNode playerNode) {
        String id = playerNode.get("id").asText();
        String name = playerNode.get("name").asText();
        String position = playerNode.get("position").asText();
        int number = playerNode.get("number").asInt();
        String nationality = playerNode.get("nationality").asText();

        Player player = new Player(id, name, position, number, nationality);

        return player;
    }

    public static String serializeTeamToJson(Team team) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("team", team.getTeamName());

        JsonArray playersArray = new JsonArray();
        
        // Iterate through each player in the team and add their stats
        for (Player player : team.getPlayers()) {
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

            playersArray.add(playerJson);
        }

        jsonObject.add("players", playersArray);

        return gson.toJson(jsonObject);
    }
}


