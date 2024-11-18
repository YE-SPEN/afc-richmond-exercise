package com.java.dev.fantasypros.richmond.objects;

public class Score {
    private int home;
    private int away;

    public Score() {
        this.home = 0;
        this.away = 0;
    }

    public Score(int homeScore, int awayScore) {
        this.home = homeScore;
        this.away = awayScore;
    }

    public String getScore() {
        return this.home + " - " + this.away;
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
        return getScore();
    }
}
