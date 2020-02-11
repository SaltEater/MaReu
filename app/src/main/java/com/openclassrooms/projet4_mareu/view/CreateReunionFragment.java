package com.openclassrooms.projet4_mareu.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.projet4_mareu.R;
import com.openclassrooms.projet4_mareu.di.DI;
import com.openclassrooms.projet4_mareu.event.AddReunionEvent;
import com.openclassrooms.projet4_mareu.model.Reunion;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateReunionFragment extends Fragment {
    @BindView(R.id.et_choose_time)
    TextInputLayout chooseTime;
    @BindView(R.id.add_reunion_button)
    Button addReunionButton;
    @BindView(R.id.salle_spinner)
    AppCompatSpinner roomSpinner;
    @BindView(R.id.reunion_name)
    TextInputLayout reunionName;
    @BindView(R.id.participants_list)
    TextInputLayout participantsList;


    static CreateReunionFragment newInstance() {
        return new CreateReunionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_reunion, container, false);
        ButterKnife.bind(this, view);
        configSpinner();
        configAddReunionButton();
        configCalendar();
        return view;
    }

    private void configSpinner() {
        List<String> rooms = new ArrayList<>();
        rooms.add("Salle");
        rooms.addAll(DI.getApiservice().getRoomList());

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),R.layout.spinner_item, rooms){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        roomSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void configCalendar() {
        Objects.requireNonNull(chooseTime.getEditText()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        chooseTime.getEditText().setText(String.format("%02dh%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
    }

    private void configAddReunionButton() {
        addReunionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                confirmInput();
                closeKeyboard();
            }
        });
    }


    private boolean validateReunionName(){
        String reunionString = Objects.requireNonNull(reunionName.getEditText()).getText().toString().trim();
        if (reunionString.isEmpty())
            reunionName.setError("Ce champ doit être rempli");
        else if (reunionString.length()>reunionName.getCounterMaxLength())
            reunionName.setError("Nom trop long");
        else{
            reunionName.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateParticipants(){
        String participantString = Objects.requireNonNull(participantsList.getEditText()).getText().toString().trim();
        if (participantString.isEmpty())
            participantsList.setError("Ce champ doit être rempli");
        else if (participantString.length() > participantsList.getCounterMaxLength())
            participantsList.setError("Nom trop long");
        else {
            participantsList.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateChooseTime(){
        String timeString = Objects.requireNonNull(chooseTime.getEditText()).getText().toString().trim();
        if (timeString.isEmpty()){
            chooseTime.setError("Ce champ doit être rempli");
            return false;
        }
        else{
            chooseTime.setError(null);
            return true;
        }
    }

    private void confirmInput(){
        if (validateParticipants() && validateReunionName() && validateChooseTime() && !(roomSpinner.getSelectedItem()== roomSpinner.getItemAtPosition(0))) {
            Reunion reunion = new Reunion(Objects.requireNonNull(reunionName.getEditText()).getText().toString(),
                    roomSpinner.getSelectedItem().toString(),
                    Objects.requireNonNull(chooseTime.getEditText()).getText().toString(),
                    Objects.requireNonNull(participantsList.getEditText()).getText().toString());
            EventBus.getDefault().post(new AddReunionEvent(reunion));
            Toast.makeText(getActivity(), "La réunion a été ajoutée", Toast.LENGTH_LONG).show();
        }
    }

    private void closeKeyboard(){
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


}