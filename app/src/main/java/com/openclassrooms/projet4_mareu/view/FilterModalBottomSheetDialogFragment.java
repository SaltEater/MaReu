package com.openclassrooms.projet4_mareu.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.openclassrooms.projet4_mareu.R;
import com.openclassrooms.projet4_mareu.di.DI;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterModalBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private ArrayList<String> rooms;
    private boolean update;
    private SharedPreferences settings;

    @BindView(R.id.text_min)
    TextView tvMin;
    @BindView(R.id.text_max)
    TextView tvMax;
    @BindView(R.id.filter_list_done)
    TextView tvDone;
    @BindView(R.id.filter_list_clear)
    TextView tvClear;
    @BindView(R.id.seek_bar)
    CrystalRangeSeekbar rangeSeekBar;
    @BindView(R.id.recycler_view_salles)
    RecyclerView roomRecyclerView;


    static FilterModalBottomSheetDialogFragment newInstance() {
        return new FilterModalBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_modal_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this, view);
        rooms = DI.getApiservice().getRoomList();
        update = true;
        settings = Objects.requireNonNull(getActivity()).getSharedPreferences(getString(R.string.filterSettings), Context.MODE_PRIVATE);

        configToolbarTextViews();
        configRangeSeekBar();
        configRecyclerView();
        return view;
    }

    @Override
    public void onDismiss(@NonNull final DialogInterface dialog) {
        super.onDismiss(dialog);
        updateData();
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener)
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
    }

    private void configRangeSeekBar() {
        rangeSeekBar.setMinStartValue(settings.getInt("minHour", -1)).setMaxStartValue(settings.getInt("maxHour", -1)).apply();
        rangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });
    }

    private void configRecyclerView(){
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        roomRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        roomRecyclerView.setAdapter(new RoomRecyclerViewAdapter(rooms, getContext()));
    }

    private void configToolbarTextViews(){
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                update = false;
                dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                dismiss();
            }
        });
    }
    private void updateData(){
        if (update) {
            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(getString(R.string.filterSettings), Context.MODE_PRIVATE).edit();
            editor.putInt("minHour", Integer.parseInt(tvMin.getText().toString()));
            editor.putInt("maxHour", Integer.parseInt(tvMax.getText().toString()));
            editor.apply();
        }
    }

    private void clearData(){
        SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(getString(R.string.filterSettings), Context.MODE_PRIVATE).edit();
        editor.putInt("minHour", 0);
        editor.putInt("maxHour", 24);

        for(String room : DI.getApiservice().getRoomList())
            editor.putBoolean(room,true);
        editor.apply();
    }
}
