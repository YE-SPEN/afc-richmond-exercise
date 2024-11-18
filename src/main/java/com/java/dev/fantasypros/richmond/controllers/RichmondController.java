package com.java.dev.fantasypros.richmond.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.java.dev.fantasypros.richmond.RichmondApplication;
import com.java.dev.fantasypros.richmond.loaders.PlayerLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Player;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.objects.Season;

import java.util.List;

@RestController
public class RichmondController {

    @GetMapping("/")
    public String getHello() {
        return "";
    }

    @GetMapping("/team")
    public String getTeamDetails() {
        Team richmond = RichmondApplication.getTeam();
        return TeamLoader.serializeTeamToJson(richmond);
    }

    @GetMapping("/player/{id}")
    public String getPlayerDetails(@PathVariable("id") String playerId) {
        Team richmond = RichmondApplication.getTeam();
        Player player = richmond.getPlayerById(playerId);
        List<Season> seasons = RichmondApplication.getSeasons();
        Season season = seasons.get(0);

        return PlayerLoader.serializePlayerCard(richmond, player, season);
    }
}

