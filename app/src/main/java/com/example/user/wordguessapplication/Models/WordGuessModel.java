package com.example.user.wordguessapplication.Models;

import java.io.Serializable;

/**
 * <h1><font color="orange">WordGuessModel</font></h1>
 * Model class to store the Word Guess data.
 * Created by Shubham Chauhan on 8/8/17.
 */

public class WordGuessModel implements Serializable{
    private String word;
    private String bull;
    private String cow;

    public WordGuessModel(String word, String bull, String cow) {
        this.word = word;
        this.bull = bull;
        this.cow = cow;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getBull() {
        return bull;
    }

    public void setBull(String bull) {
        this.bull = bull;
    }

    public String getCow() {
        return cow;
    }

    public void setCow(String cow) {
        this.cow = cow;
    }

}
