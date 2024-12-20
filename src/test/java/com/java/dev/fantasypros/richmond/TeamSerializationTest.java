package com.java.dev.fantasypros.richmond;

import com.java.dev.fantasypros.richmond.exceptions.SerializationFailureException;
import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TeamSerializationTest {

    private static final String EXPECTED_OUTPUT_FILE_PATH = "src\\test\\java\\com\\java\\dev\\fantasypros\\richmond\\json\\team-roster-expected-output.json";

    @Test
    void testSerializeTeamToJson() throws IOException, SerializationFailureException {

        Team team = TeamLoader.loadTeam();
        MatchLoader.fetchMatchData(team);

        String expectedJsonOutput = new String(Files.readAllBytes(Paths.get(EXPECTED_OUTPUT_FILE_PATH))).trim();

        String actualJsonOutput = JsonSerializer.serializeTeamToJson(team);

        JsonElement expectedJson = JsonParser.parseString(expectedJsonOutput);
        JsonElement actualJson = JsonParser.parseString(actualJsonOutput);

        assertEquals(expectedJson, actualJson, "The serialized JSON output does not match the expected output.");
    }
}
