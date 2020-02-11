package com.openclassrooms.projet4_mareu.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.projet4_mareu.R;
import com.openclassrooms.projet4_mareu.di.DI;
import com.openclassrooms.projet4_mareu.event.AddReunionEvent;
import com.openclassrooms.projet4_mareu.event.DeleteReunionEvent;
import com.openclassrooms.projet4_mareu.model.Reunion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FilterCallbackInterface, DialogInterface.OnDismissListener {
    // Views
    @BindView(R.id.fab_main_activity)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Attributes
    private ArrayList<Reunion> reunionList = DI.getApiservice().getReunionList();
    private ArrayList<Reunion> filteredReunionList = new ArrayList<>();
    private ReunionListFragment reunionListFragment = ReunionListFragment.newInstance(this);
    private FilterModalBottomSheetDialogFragment dialog = FilterModalBottomSheetDialogFragment.newInstance();
    private boolean hide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        configureView();
        initSettings();
    }

    /** Filters methods **/
    @Override
    public void initSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.filterSettings), Context.MODE_PRIVATE).edit();
        editor.putInt("minHour", 0);
        editor.putInt("maxHour", 24);

        for(String room : DI.getApiservice().getRoomList())
            editor.putBoolean(room,true);
        editor.apply();
    }

    @Override
    public boolean isValid(Reunion reunion) {
        SharedPreferences settings = getSharedPreferences(getString(R.string.filterSettings), Context.MODE_PRIVATE);
        boolean visible = settings.getBoolean(reunion.getRoom(), true);
        int minHour = settings.getInt("minHour", -1);
        int maxHour = settings.getInt("maxHour", -1);
        int hour = Integer.parseInt(reunion.getDate().substring(0,2));
        return visible && (minHour <= hour) && (maxHour > hour);
    }

    public ArrayList<Reunion> getFilteredReunions(){
        updateFilteredReunions();
        return filteredReunionList;
    }

    private void updateFilteredReunions(){
        filteredReunionList.clear();
        for (Reunion reunion : reunionList)
            if (isValid(reunion))
                filteredReunionList.add(reunion);
    }

    /** Toolbar methods **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                showReunionListFragment();
                return true;
            case R.id.filter_menu_item:
                dialog.show(getSupportFragmentManager(), "MODAL");
                getSupportFragmentManager().executePendingTransactions();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_activity, menu);
        MenuItem item = menu.findItem(R.id.filter_menu_item);
        item.setVisible(!hide);
        return true;
    }

    /** Config views part **/
    private void configureView() {
        if (findViewById(R.id.fragment_container) == null) {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_1, reunionListFragment)
                    .replace(R.id.fragment_container_2, CreateReunionFragment.newInstance())
                    .commit();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            hide = false;
            invalidateOptionsMenu();
            fab.setVisibility(View.INVISIBLE);

        } else {
            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    showCreateReunionFragment();
                }

            });
            showReunionListFragment();
        }
    }
    /** portrait mode methods **/
    public void showReunionListFragment(){
        replaceFragment(reunionListFragment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        hide = false;
        invalidateOptionsMenu();
        fab.setVisibility(View.VISIBLE);
    }

    public void showCreateReunionFragment(){
        replaceFragment(CreateReunionFragment.newInstance());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        hide = true;
        invalidateOptionsMenu();
        fab.setVisibility(View.INVISIBLE);
    }

    private void replaceFragment(Fragment destFragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, destFragment)
                .commit();
    }

    /** Event method**/

    @Override
    public void onDismiss(final DialogInterface dialog) {
        updateFilteredReunions();
        reunionListFragment.update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onAddReunionEvent(AddReunionEvent addReunionEvent){
        updateFilteredReunions();
        reunionListFragment.update();
    }

    @Subscribe
    public void onDeleteReunionEvent(DeleteReunionEvent deleteReunionEvent){
        updateFilteredReunions();
        reunionListFragment.update();
    }

    public boolean getHide() {
        return hide;
    }
}