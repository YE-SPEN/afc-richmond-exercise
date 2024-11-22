package com.java.dev.fantasypros.richmond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.java.dev.fantasypros.richmond.loaders.MatchLoader;
import com.java.dev.fantasypros.richmond.loaders.TeamLoader;
import com.java.dev.fantasypros.richmond.objects.Team;

@SpringBootApplication
public class RichmondApplication {
	static Team team;

	/*
	 I am calling the loaders directly on server start here as requested in the assessment description. 
	 In a real world setting we would probably prefer to load team data only when that data set is requested by the client or implement some sort of cache policy for aged data
	 */
	public static void main(String[] args) {
		SpringApplication.run(RichmondApplication.class, args);
        team = TeamLoader.loadTeam();
        MatchLoader.fetchMatchData(team);
	}
	
	public static Team getTeam() {
		return team;
	}

}
