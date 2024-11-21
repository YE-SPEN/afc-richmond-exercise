package com.java.dev.fantasypros.richmond.objects;

public class Score {
    private int home;
    private int away;

    // default constructor that can be called to create a match object that has not happened yet
    public Score() {
        this.home = 0;
        this.away = 0;
    }

    public Score(int homeScore, int awayScore) {
        this.home = homeScore;
        this.away = awayScore;
    }

    public int getHomeScore() {
        return this.home;
    }

    public void setHomeScore(int homeScore) {
        this.home = homeScore;
    }

    public void incrementHomeScore() {
        this.home++;
    }

    public int getAwayScore() {
        return this.away;
    }

    public void setAwayScore(int awayScore) {
        this.away = awayScore;
    }

    public void incrementAwayScore() {
        this.away++;
    }

    @Override
    public String toString() {
        return this.home + " - " + this.away;
    }
}
