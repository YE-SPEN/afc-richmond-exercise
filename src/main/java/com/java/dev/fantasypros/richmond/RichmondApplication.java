package com.java.dev.fantasypros.richmond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Team;
import com.java.dev.fantasypros.richmond.objects.Season;

import java.util.List;

@SpringBootApplication
public class RichmondApplication {
	static Team team;

	public static void main(String[] args) {
		SpringApplication.run(RichmondApplication.class, args);
        team = TeamLoader.loadTeam();
        MatchLoader.loadMatches(team);
	}
	
	public static Team getTeam() {
		return team;
	}

	public static List<Season> getSeasons() {
		return team.getSeasons();
	}

}
