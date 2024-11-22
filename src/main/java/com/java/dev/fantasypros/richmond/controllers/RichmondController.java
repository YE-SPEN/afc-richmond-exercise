package com.java.dev.fantasypros.richmond.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.java.dev.fantasypros.richmond.RichmondApplication;
import com.java.dev.fantasypros.richmond.exceptions.SerializationFailureException;
import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializer;

import jakarta.servlet.http.HttpServletResponse;

import com.java.dev.fantasypros.richmond.objects.Season;

import java.io.IOException;
import java.util.List;

@RestController
public class RichmondController {

    /* 
    *redirecting the main URL to the team output since we have no implementation for a home route,
    *both of the main endpoints will throw a 500 server error if data fails to load or serialize
    *to discuss error policy during code review
    */ 

    @GetMapping("/")
    public void getHome(HttpServletResponse response) throws IOException {
        response.sendRedirect("/team");
    }

    @GetMapping(path="/team", produces="application/json; charset=utf-8")
    public String getTeamDetails() {
        Team team = RichmondApplication.getTeam();
        try {
            String serializedTeamJson = JsonSerializer.serializeTeamToJson(team);
            return serializedTeamJson;
        } catch (SerializationFailureException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    @GetMapping(path="/player/{id}",  produces="application/json; charset=utf-8")
    public String getPlayerDetails(@PathVariable("id") String playerId) {
        Team team = RichmondApplication.getTeam(); // this assumes every player requested plays for AFC Richmond
        Player player = team.getPlayerById(playerId);
        List<Season> seasons = team.getSeasons();   
        Season season = seasons.get(0); // this logic would also need to be changed to support multiple seasons, could become a loop to return all seasons or take the requested season in a URL parameter

        try {
            String playerCardJson = JsonSerializer.serializePlayerCard(team, player, season);
            return playerCardJson;
        }  catch (SerializationFailureException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}



