package com.example.finalhometest.ui.listrecord;

public class Model {
    private int id;
    private String word;
    private String mean;
    private String note;

    public Model(int id, String word, String mean, String note) {
        this.id = id;
        this.word = word;
        this.mean = mean;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
