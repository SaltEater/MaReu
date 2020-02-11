package com.openclassrooms.projet4_mareu.model;

public class Reunion {

    private String name;
    private String room;
    private String date;
    private String participants;


    public Reunion(String name, String room, String date, String participants){
        this.name = name;
        this.room = room;
        this.date = date;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }
}