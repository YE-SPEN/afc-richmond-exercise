package com.java.dev.fantasypros.richmond.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.java.dev.fantasypros.richmond.RichmondApplication;
import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.serialization.JsonSerializor;
import com.java.dev.fantasypros.richmond.objects.Season;

import java.util.List;

@RestController
public class RichmondController {

    @GetMapping("/")
    public String getHome() {
        return "";
    }

    @GetMapping(path="/team", produces="application/json; charset=utf-8")
    public String getTeamDetails() {
        Team richmond = RichmondApplication.getTeam();
        return JsonSerializor.serializeTeamToJson(richmond);
    }

    @GetMapping(path="/player/{id}",  produces="application/json; charset=utf-8")
    public String getPlayerDetails(@PathVariable("id") String playerId) {
        Team richmond = RichmondApplication.getTeam();
        Player player = richmond.getPlayerById(playerId);
        List<Season> seasons = RichmondApplication.getSeasons();
        Season season = seasons.get(0);

        return JsonSerializor.serializePlayerCard(richmond, player, season);
    }
}

