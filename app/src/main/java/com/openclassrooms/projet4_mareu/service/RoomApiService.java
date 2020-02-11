package com.openclassrooms.projet4_mareu.service;

import java.util.List;

public interface RoomApiService {

    List<String> getRoomList();

    void addRoom(String room);

    void deleteRoom(String room);

}
