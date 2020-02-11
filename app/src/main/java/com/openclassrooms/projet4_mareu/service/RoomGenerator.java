package com.openclassrooms.projet4_mareu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomGenerator {

    public static List<String> ROOMS = Arrays.asList("Peach","Mario","Luigi","Toad");

    public static ArrayList<String> generateRooms(){
        return new ArrayList<String>(ROOMS);
    }

}