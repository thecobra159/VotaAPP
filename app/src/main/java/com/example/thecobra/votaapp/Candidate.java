package com.example.thecobra.votaapp;

import android.graphics.Bitmap;

public class Candidate {
    private String name, party;
    private Bitmap photo;
    private int id;

    public Candidate(String name, String party, Bitmap photo) {
        this.name   = name;
        this.party  = party;
        this.photo  = photo;
    }

    public String getName() { return name; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
