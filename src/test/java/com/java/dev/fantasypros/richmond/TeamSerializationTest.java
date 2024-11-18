package com.java.dev.fantasypros.richmond;

import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TeamSerializationTest {

    private static final String EXPECTED_OUTPUT_FILE_PATH = "src\\test\\java\\com\\java\\dev\\fantasypros\\richmond\\json\\team-roster-expected-output.json";

    @Test
    @SuppressWarnings("unchecked")
    void testSerializeTeamToJson() throws IOException {
        // Load the pre-initialized team from RichmondApplication
        Team team = TeamLoader.loadTeam();
        MatchLoader.loadMatches(team);

        // Call the serialization method
        String actualJsonOutput = JsonSerializor.serializeTeamToJson(team);

        // Read the expected JSON output from the file
        String expectedJsonOutput = new String(Files.readAllBytes(Paths.get(EXPECTED_OUTPUT_FILE_PATH))).trim();

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the expected and actual JSON strings into Maps
        Map<String, Object> expectedMap = objectMapper.readValue(expectedJsonOutput, Map.class);
        Map<String, Object> actualMap = objectMapper.readValue(actualJsonOutput, Map.class);

        assertEquals(expectedMap, actualMap, "The serialized JSON output does not match the expected output.");
    }
}

