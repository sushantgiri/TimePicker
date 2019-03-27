package com.sushant.timepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sushant.timepicker.library.DateTimeFragment;

public class TimePickerActivity extends AppCompatActivity implements DateTimeFragment.TimePickerListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_main);


        DateTimeFragment dateTimeFragment = DateTimeFragment.newInstance();

        dateTimeFragment.setTimeHeaderText("Select Time !!!");
        dateTimeFragment.setDateHeaderText("Select Date !!!");



        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.dateContainer, DateTimeFragment.newInstance(), DateTimeFragment.class.getName())
                .disallowAddToBackStack()
                .commit();


    }


    private void hideSystemUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onDateTimeSelected(String selectedMonth, String selectedDay, String selectedHours, String selectedMinutes) {
        Toast.makeText(this, "Selected Month: " + selectedMonth + "\nSelected Day: " + selectedDay + " \nSelected Hour: " + selectedHours + " \nSelected Minutes: " + selectedMinutes, Toast.LENGTH_LONG).show();
    }
}
