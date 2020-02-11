package com.openclassrooms.projet4_mareu.view;

import com.openclassrooms.projet4_mareu.model.Reunion;

import java.util.ArrayList;

public interface FilterCallbackInterface {

    /** Used to initialize the filters and also to reset them **/
    void initSettings();

    /** Returns true is the meeting should be visible **/
    boolean isValid(Reunion reunion);

    /** Returns the visible meeting list **/
    ArrayList<Reunion> getFilteredReunions();

}