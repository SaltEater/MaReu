package com.openclassrooms.projet4_mareu.service;

import com.openclassrooms.projet4_mareu.model.Reunion;

import java.util.ArrayList;

public interface ReunionApiService {

    ArrayList<Reunion> getReunionList();

    void deleteReunion(Reunion reunion);

    void addReunion(Reunion reunion);

}
