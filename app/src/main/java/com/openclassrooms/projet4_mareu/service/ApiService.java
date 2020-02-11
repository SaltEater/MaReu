package com.openclassrooms.projet4_mareu.service;

import com.openclassrooms.projet4_mareu.model.Reunion;

import java.util.ArrayList;

public class ApiService implements ReunionApiService, RoomApiService {
    private ArrayList<Reunion> reunionList = new ArrayList<Reunion>();
    private ArrayList<String> roomList = RoomGenerator.generateRooms();

    @Override
    public ArrayList<Reunion> getReunionList() {
        return reunionList;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        reunionList.remove(reunion);
    }

    @Override
    public void addReunion(Reunion reunion) {
        reunionList.add(reunion);
    }

    @Override
    public ArrayList<String> getRoomList() {
        return roomList;
    }

    @Override
    public void addRoom(String room) {
        roomList.add(room);
    }

    @Override
    public void deleteRoom(String room) {
        roomList.remove(room);
    }
}