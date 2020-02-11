package com.openclassrooms.projet4_mareu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.MenuItem;


import com.openclassrooms.projet4_mareu.di.DI;
import com.openclassrooms.projet4_mareu.model.Reunion;
import com.openclassrooms.projet4_mareu.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)

public class MainActivityUnitTest {
    private MainActivity activity;
    private ActivityController<MainActivity> controller;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        activity =controller.create().resume().get();
    }

    @Test
    public void initSettings_isCorrect() {
        activity.initSettings();
        SharedPreferences init = activity.getSharedPreferences("FILTERS", Context.MODE_PRIVATE);
        assertEquals(init.getInt("minHour", -1), 0);
        assertEquals(init.getInt("maxHour", -1), 24);
        for (String room : DI.getApiservice().getRoomList())
            assertTrue(init.getBoolean(room, false));
    }

    @Test
    public void previousButton_isCorrect() {
        assertFalse(activity.getHide());
        activity.showCreateReunionFragment();
        assertTrue(activity.getHide());
    }

    @Test
    public void isValid_isCorrect() {
        activity.initSettings();
        SharedPreferences.Editor edit = activity.getSharedPreferences(activity.getString(R.string.filterSettings), Context.MODE_PRIVATE).edit();
        edit.putInt("minHour", 4);
        edit.putInt("maxHour", 12);
        edit.putBoolean("Peach", false);
        edit.apply();
        Reunion reunion = new Reunion("ReunionTest", "Peach", "05h00", "None");
        assertFalse(activity.isValid(reunion));
        reunion.setRoom("Mario");
        assertTrue(activity.isValid(reunion));
        reunion.setDate("00h00");
        assertFalse(activity.isValid(reunion));
        reunion.setDate("20h00");
        assertFalse(activity.isValid(reunion));
    }

    @Test
    @Config(qualifiers = "sw600dp-land")
    public void sw600dpLandscapeView_isCorrect() {
        assertNull(activity.findViewById(R.id.fragment_container));
    }

    @Test
    @Config(qualifiers = "sw600dp-port")
    public void sw600dpPortraitView_isCorrect() {
        assertNotNull(activity.findViewById(R.id.fragment_container));
    }

}