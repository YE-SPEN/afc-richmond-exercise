package com.java.dev.fantasypros.richmond;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Season;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializor;
import com.java.dev.fantasypros.richmond.objects.Player;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class PlayerCardSerializationTest {

    private static final String EXPECTED_OUTPUT_FILE_PATH = "src\\test\\java\\com\\java\\dev\\fantasypros\\richmond\\json\\player-card-expected-output.json";
    private static final String PLAYER_ID = "JT9";

    @Test
    @SuppressWarnings("unchecked")
    void testSerializePlayerCardToJson() throws IOException {
        // Load the pre-initialized team from RichmondApplication
        Team richmond = TeamLoader.loadTeam();
        MatchLoader.loadMatches(richmond);
        Player jamieTartt = richmond.getPlayerById(PLAYER_ID);
        List<Season> seasons = richmond.getSeasons();
        Season season = seasons.get(0);

        // Call the method to serialize player card
        String actualJsonOutput = JsonSerializor.serializePlayerCard(richmond, jamieTartt, season);

        // Read the expected JSON output from the file
        String expectedJsonOutput = new String(Files.readAllBytes(Paths.get(EXPECTED_OUTPUT_FILE_PATH))).trim();

        // Create an ObjectMapper instance with pretty printing enabled
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Convert the expected and actual JSON strings into Maps
        Map<String, Object> expectedMap = objectMapper.readValue(expectedJsonOutput, Map.class);
        Map<String, Object> actualMap = objectMapper.readValue(actualJsonOutput, Map.class);

        // Pretty-print the expected and actual JSON strings
        String prettyExpectedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedMap);
        String prettyActualJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualMap);

        // Compare the actual and expected outputs
        try {
            assertEquals(expectedMap, actualMap, "The serialized player card JSON output does not match the expected output.");
        } catch (AssertionError e) {
            System.out.println("Expected JSON (Pretty-Printed):\n" + prettyExpectedJson);
            System.out.println("Actual JSON (Pretty-Printed):\n" + prettyActualJson);
            fail("JSON mismatch:\nExpected:\n" + prettyExpectedJson + "\nActual:\n" + prettyActualJson);
        }
    }
}
