package com.cgreen.ygocardtracker.deck;

public class Deck {
    private int id;
    private String name, note;
    
    public Deck() {
        name = "";
        note = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
