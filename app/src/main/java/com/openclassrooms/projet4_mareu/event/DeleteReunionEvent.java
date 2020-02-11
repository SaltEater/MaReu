package com.openclassrooms.projet4_mareu.event;

import com.openclassrooms.projet4_mareu.di.DI;
import com.openclassrooms.projet4_mareu.model.Reunion;

public class DeleteReunionEvent {
    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion) {
        this.reunion = reunion;
        DI.getApiservice().deleteReunion(reunion);
    }
}