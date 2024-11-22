package com.java.dev.fantasypros.richmond.loaders;

import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.google.gson.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TeamLoader {

    private static final String PLAYERS_JSON_URL = "https://java-dev.fantasypros.com/richmond/players.json";

    public static Team loadTeam() {
        List<Player> players = new ArrayList<>();
        Team team = null;

        try {
            String jsonResponse = fetchPlayerData();
            JsonObject rootNode = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (!rootNode.has("team") || rootNode.get("team").getAsString().isEmpty()) {
                throw new RuntimeException("Missing or empty 'team' field in JSON response.");
            }

            String teamName = rootNode.get("team").getAsString();
            team = new Team(teamName);

            if (!rootNode.has("players") || !rootNode.get("players").isJsonArray()) {
                throw new RuntimeException("Missing or invalid 'players' array in JSON response.");
            }

            JsonArray playersArray = rootNode.getAsJsonArray("players");

            for (JsonElement playerNode : playersArray) {
                try {
                    Player player = loadPlayer(playerNode.getAsJsonObject());
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

    public static Player loadPlayer(JsonObject playerNode) {
        String id = playerNode.get("id").getAsString();
        String name = playerNode.get("name").getAsString();
        String position = playerNode.get("position").getAsString();
        int number = playerNode.get("number").getAsInt();
        String nationality = playerNode.get("nationality").getAsString();

        return new Player(id, name, position, number, nationality);
    }
}
