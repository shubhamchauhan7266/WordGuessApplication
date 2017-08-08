package com.example.user.wordguessapplication.Models;

import java.io.Serializable;

/**
 * <h1><font color="orange">WordGuessModel</font></h1>
 * Model class to store the Word Guess data.
 * Created by Shubham Chauhan on 8/8/17.
 */

public class WordGuessModel implements Serializable {
    private String word;
    private int bull;
    private int cow;

    public WordGuessModel(String word, int bull, int cow) {
        this.word = word;
        this.bull = bull;
        this.cow = cow;
    }

    public String getWord() {
        return word;
    }

    public int getBull() {
        return bull;
    }

    public int getCow() {
        return cow;
    }

}
