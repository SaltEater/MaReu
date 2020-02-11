package com.openclassrooms.projet4_mareu;


import android.app.TimePickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.openclassrooms.projet4_mareu.view.MainActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static com.openclassrooms.projet4_mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.object.HasToString.hasToString;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private MainActivity mainActivity;
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mainActivity = activityRule.getActivity();
        assertThat(mainActivity, notNullValue());

    }

    @Test
    public void createReunion_isCorrect() {
        try {
            onView(ViewMatchers.withId(R.id.fab_main_activity)).perform(click());
        } catch (NoMatchingViewException ignored) {}

        onView(ViewMatchers.withId(R.id.reunion_name_text)).perform(typeText("Reunion Test"));
        onView(ViewMatchers.withId(R.id.participants_list_text)).perform(typeText("Liste des Participants"));
        onView(ViewMatchers.withId(R.id.salle_spinner)).perform(click());
        onData(hasToString(startsWith("Peach"))).perform(click());
        onView(ViewMatchers.withId(R.id.et_choose_time_text)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(setTime(10,10));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(ViewMatchers.withId(R.id.add_reunion_button)).perform(click());
        try {
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        } catch (NoMatchingViewException ignored){}

        onView(ViewMatchers.withId(R.id.recycler_view_reunion)).check(withItemCount(1));


    }

    @Test
    public void deleteReunion_isCorrect() {
        RecyclerView recyclerView = mainActivity.findViewById(R.id.recycler_view_reunion);
        int itemCount = recyclerView.getAdapter().getItemCount();
        createReunion("Reunion Delete Test", "Test", "Mario", 4, 0);
        onView(ViewMatchers.withId(R.id.recycler_view_reunion)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.delete_image)));
        assertEquals(itemCount, recyclerView.getAdapter().getItemCount());
    }

    @Test
    public void filterRoom_isCorrect() {
        RecyclerView recyclerView = mainActivity.findViewById(R.id.recycler_view_reunion);
        int itemCount = recyclerView.getAdapter().getItemCount();
        createReunion("Peach 0", "Test", "Peach", 0, 0);
        createReunion("Mario 6", "Test", "Mario", 6, 0);
        createReunion("Luigi 12", "Test", "Luigi", 12, 0);
        createReunion("Toad 18", "Test", "Toad", 18, 0);

        onView(ViewMatchers.withId(R.id.filter_menu_item)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view_salles)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //onView(ViewMatchers.withId(R.id.seek_bar)).perform();
        onView(ViewMatchers.withId(R.id.filter_list_done)).perform(click());
        assertEquals(itemCount+3, recyclerView.getAdapter().getItemCount());
    }



    public static void createReunion(String reunionName, String participants, String room, int hour, int min){
        try {
            onView(ViewMatchers.withId(R.id.fab_main_activity)).perform(click());
        } catch (NoMatchingViewException ignored) {}

        onView(ViewMatchers.withId(R.id.reunion_name_text)).perform(typeText(reunionName));
        onView(ViewMatchers.withId(R.id.participants_list_text)).perform(typeText(participants));
        onView(ViewMatchers.withId(R.id.salle_spinner)).perform(click());
        onData(hasToString(startsWith(room))).perform(click());
        onView(ViewMatchers.withId(R.id.et_choose_time_text)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(setTime(hour,min));
        onView(ViewMatchers.withText("OK")).perform(click());
        onView(ViewMatchers.withId(R.id.add_reunion_button)).perform(click());
        try {
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        } catch (NoMatchingViewException ignored){}
    }


    public static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp = (TimePicker) view;
                tp.setHour(hour);
                tp.setMinute(minute);
            }
            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}
