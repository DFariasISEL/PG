package com.isel.pg;

public class Score {

    String score;

    public Score(String name, int tries, int minutes) {
        score = name + " - " + tries + " " + minutes;
    }

    public String toString()
    {
        return score;
    }

}
