package com.openclassrooms.projet4_mareu.event;

import com.openclassrooms.projet4_mareu.di.DI;
import com.openclassrooms.projet4_mareu.model.Reunion;

public class AddReunionEvent {
    public Reunion reunion;

    public AddReunionEvent(Reunion reunion) {
        this.reunion = reunion;
        DI.getApiservice().addReunion(reunion);
    }
}