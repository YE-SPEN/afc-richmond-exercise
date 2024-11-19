package com.java.dev.fantasypros.richmond.loaders;

import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
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
            // Fetch the JSON response from the endpoint
            String jsonResponse = fetchPlayerData();

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Validate presence of "team" field
            if (rootNode == null || !rootNode.has("team") || rootNode.get("team").asText().isEmpty()) {
                throw new RuntimeException("Missing or empty 'team' field in JSON response.");
            }

            // Initialize Team object with the team name from JSON
            String teamName = rootNode.get("team").asText();
            team = new Team(teamName);

            // Validate presence of "players" array
            if (!rootNode.has("players") || !rootNode.get("players").isArray()) {
                throw new RuntimeException("Missing or invalid 'players' array in JSON response.");
            }

            // Extract players array
            JsonNode playersArray = rootNode.get("players");

            // Process each player node
            for (JsonNode playerNode : playersArray) {
                try {
                    Player player = processPlayer(playerNode);
                    if (player != null) {
                        players.add(player);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing player: " + e.getMessage());
                }
            }

            team.setPlayers(players);

            if (players.isEmpty()) {
                System.err.println("Warning: No players were loaded.");
            }

        } catch (IOException e) {
            System.err.println("I/O Error while loading players: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Request interrupted: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Data Error: " + e.getMessage());
        }

        return team;
    }

    // Method to fetch player data from the endpoint
    private static String fetchPlayerData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PLAYERS_JSON_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch data. HTTP status code: " + response.statusCode());
        }

        return response.body();
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

    /*public static String serializeTeamToJson(Team team) {
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
    }*/
}


