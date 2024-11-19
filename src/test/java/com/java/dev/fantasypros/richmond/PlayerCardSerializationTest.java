package com.java.dev.fantasypros.richmond;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.java.dev.fantasypros.richmond.exceptions.SerializationFailureException;
import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Season;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializer;
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
    void testSerializePlayerCardToJson() throws IOException, SerializationFailureException {
        
        // Load the pre-initialized team from RichmondApplication
        Team richmond = TeamLoader.loadTeam();
        MatchLoader.fetchMatchData(richmond);
        Player jamieTartt = richmond.getPlayerById(PLAYER_ID);
        List<Season> seasons = richmond.getSeasons();
        Season season = seasons.get(0);

        String expectedJsonOutput = new String(Files.readAllBytes(Paths.get(EXPECTED_OUTPUT_FILE_PATH))).trim();
        String actualJsonOutput = JsonSerializer.serializePlayerCard(richmond, jamieTartt, season);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Convert the expected and actual JSON strings into Maps
        Map<String, Object> expectedMap = objectMapper.readValue(expectedJsonOutput, Map.class);
        Map<String, Object> actualMap = objectMapper.readValue(actualJsonOutput, Map.class);

        String prettyExpectedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedMap);
        String prettyActualJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualMap);

        // Compare the actual and expected outputs
        try {
            assertEquals(expectedMap, actualMap, "The serialized player card JSON output  the expected output.");
        } catch (AssertionError e) {
            System.out.println("Expected JSON (Pretty-Printed):\n" + prettyExpectedJson);
            System.out.println("Actual JSON (Pretty-Printed):\n" + prettyActualJson);
            fail("JSON mismatch:\nExpected:\n" + prettyExpectedJson + "\nActual:\n" + prettyActualJson);
        }
    }
}
