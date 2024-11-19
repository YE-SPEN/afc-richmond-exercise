package com.java.dev.fantasypros.richmond.objects;

/*
 *In this object type we are storing the player statistics directly on the player object... this is because this exercise only supports a single season
 *A potential upgrade here is to store player statistics in their own season object similar to how the Team matches are being handled
 */

public class Player {
    private String id;
    private String name;
    private String position;
    private int number;
    private String nationality;
    private int games;
    private int goals;
    private int assists;
    private int points;

    public Player(String playerId, String playerName, String playerPos, int jerseyNum, String country) {
        this.id = playerId;
        this.name = playerName;
        this.position = playerPos;
        this.number = jerseyNum;
        this.nationality = country;
        this.games = 0;
        this.goals = 0;
        this.assists = 0;
        this.points = 0; 
    }

    // second overloaded constructor to create a player for the first time and generate their playerId
    public Player(String playerName, String playerPos, int jerseyNum, String country) {
        this.id = getInitials(playerName) + jerseyNum;
        this.name = playerName;
        this.position = playerPos;
        this.number = jerseyNum;
        this.nationality = country;
        this.games = 0;
        this.goals = 0;
        this.assists = 0;
        this.points = 0; 
    }

    public String getInitials(String fullName) {
        String[] nameArr = fullName.split(" ");
        String initials = "";
        
        for (String name : nameArr) {
            String initial = name.substring(0, 1).toUpperCase();
            initials = initials + initial;
        }
        return initials;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getPos() {
        return this.position;
    }

    public void setPos(String newPos) {
        this.position = newPos;
    }

    public void setJerseyNum(int newNum) {
        this.number = newNum;
    }

    public int getNum() {
        return this.number;
    }

    public void setNationality(String newNat) {
        this.nationality = newNat;
    }

    public String getNationality() {
        return this.nationality;
    }

    public int getGamesPlayed() {
        return this.games;
    }

    public int getGoals() {
        return this.goals;
    }

    public int getAssists() {
        return this.assists;
    }

    public int getPoints() {
        return this.points;
    }

    public void recordGame() {
        this.games++;
    }

    public void recordGoal() {
        this.goals++;
        this.points++;
    }

    public void recordAssist() {
        this.assists++;
        this.points++;
    }

    @Override
    public String toString() {
        return "{" + 
               "\n  id: '" + id + '\'' +
               ",\n  name: '" + name + '\'' +
               ",\n  position: '" + position + '\'' +
               ",\n  number: " + number +
               ",\n  nationality: '" + nationality + '\'' +
               ",\n  games: " + games +
               ",\n  goals: " + goals +
               ",\n  assists: " + assists +
               ",\n  points: " + points +
               "\n}";
    }
    
}
