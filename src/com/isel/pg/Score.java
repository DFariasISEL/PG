package com.isel.pg;

public class Score {

    String score, name;
    int tries, minutes;

    public Score(String name, int tries, int minutes) {
        this.name = name;
        this.tries = tries;
        this.minutes = minutes;
        while(name.length() < 9)
            name = name + " ";
        score = name + "- " + tries + "  " + minutes;
    }

    public String toString(){
        return score;
    }

}
